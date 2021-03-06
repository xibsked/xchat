package com.sked.xibchat.parser;

import com.sked.xibchat.crypto.PgpEngine;
import com.sked.xibchat.entities.Account;
import com.sked.xibchat.entities.Contact;
import com.sked.xibchat.entities.Conversation;
import com.sked.xibchat.entities.MucOptions;
import com.sked.xibchat.entities.Presences;
import com.sked.xibchat.generator.PresenceGenerator;
import com.sked.xibchat.services.XmppConnectionService;
import com.sked.xibchat.xml.Element;
import com.sked.xibchat.xmpp.OnPresencePacketReceived;
import com.sked.xibchat.xmpp.jid.Jid;
import com.sked.xibchat.xmpp.pep.Avatar;
import com.sked.xibchat.xmpp.stanzas.PresencePacket;

import java.util.ArrayList;

public class PresenceParser extends AbstractParser implements
        OnPresencePacketReceived {

    public PresenceParser(XmppConnectionService service) {
        super(service);
    }

    public void parseConferencePresence(PresencePacket packet, Account account) {
        PgpEngine mPgpEngine = mXmppConnectionService.getPgpEngine();
        final Conversation conversation = packet.getFrom() == null ? null : mXmppConnectionService.find(account, packet.getFrom().toBareJid());
        if (conversation != null) {
            final MucOptions mucOptions = conversation.getMucOptions();
            boolean before = mucOptions.online();
            int count = mucOptions.getUsers().size();
            final ArrayList<MucOptions.User> tileUserBefore = new ArrayList<>(mucOptions.getUsers().subList(0, Math.min(mucOptions.getUsers().size(), 5)));
            mucOptions.processPacket(packet, mPgpEngine);
            final ArrayList<MucOptions.User> tileUserAfter = new ArrayList<>(mucOptions.getUsers().subList(0, Math.min(mucOptions.getUsers().size(), 5)));
            if (!tileUserAfter.equals(tileUserBefore)) {
                mXmppConnectionService.getAvatarService().clear(conversation);
            }
            if (before != mucOptions.online() || (mucOptions.online() && count != mucOptions.getUsers().size())) {
                mXmppConnectionService.updateConversationUi();
            } else if (mucOptions.online()) {
                mXmppConnectionService.updateMucRosterUi();
            }
        }
    }

    public void parseContactPresence(PresencePacket packet, Account account) {
        PresenceGenerator mPresenceGenerator = mXmppConnectionService.getPresenceGenerator();
        final Jid from = packet.getFrom();
        if (from == null) {
            return;
        }
        final String type = packet.getAttribute("type");
        final Contact contact = account.getRoster().getContact(from);
        if (type == null) {
            String presence = from.isBareJid() ? "" : from.getResourcepart();
            contact.setPresenceName(packet.findChildContent("nick", "http://jabber.org/protocol/nick"));
            Avatar avatar = Avatar.parsePresence(packet.findChild("x", "vcard-temp:x:update"));
            if (avatar != null && !contact.isSelf()) {
                avatar.owner = from.toBareJid();
                if (mXmppConnectionService.getFileBackend().isAvatarCached(avatar)) {
                    if (contact.setAvatar(avatar)) {
                        mXmppConnectionService.getAvatarService().clear(contact);
                        mXmppConnectionService.updateConversationUi();
                        mXmppConnectionService.updateRosterUi();
                    }
                } else {
                    mXmppConnectionService.fetchAvatar(account, avatar);
                }
            }
            int sizeBefore = contact.getPresences().size();
            contact.updatePresence(presence, Presences.parseShow(packet.findChild("show")));
            PgpEngine pgp = mXmppConnectionService.getPgpEngine();
            Element x = packet.findChild("x", "jabber:x:signed");
            if (pgp != null && x != null) {
                Element status = packet.findChild("status");
                String msg = status != null ? status.getContent() : "";
                contact.setPgpKeyId(pgp.fetchKeyId(account, msg, x.getContent()));
            }
            boolean online = sizeBefore < contact.getPresences().size();
            updateLastseen(packet, account, false);
            mXmppConnectionService.onContactStatusChanged.onContactStatusChanged(contact, online);
        } else if (type.equals("unavailable")) {
            if (from.isBareJid()) {
                contact.clearPresences();
            } else {
                contact.removePresence(from.getResourcepart());
            }
            mXmppConnectionService.onContactStatusChanged.onContactStatusChanged(contact, false);
        } else if (type.equals("subscribe")) {
            if (contact.getOption(Contact.Options.PREEMPTIVE_GRANT)) {
                mXmppConnectionService.sendPresencePacket(account,
                        mPresenceGenerator.sendPresenceUpdatesTo(contact));
            } else {
                contact.setOption(Contact.Options.PENDING_SUBSCRIPTION_REQUEST);
            }
        }
        mXmppConnectionService.updateRosterUi();
    }

    @Override
    public void onPresencePacketReceived(Account account, PresencePacket packet) {
        if (packet.hasChild("x", "http://jabber.org/protocol/muc#user")) {
            this.parseConferencePresence(packet, account);
        } else if (packet.hasChild("x", "http://jabber.org/protocol/muc")) {
            this.parseConferencePresence(packet, account);
        } else {
            this.parseContactPresence(packet, account);
        }
    }

}
