package com.sked.xibchat.parser;


import com.sked.xibchat.entities.Account;
import com.sked.xibchat.entities.Contact;
import com.sked.xibchat.services.XmppConnectionService;
import com.sked.xibchat.xml.Element;
import com.sked.xibchat.xmpp.jid.Jid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class AbstractParser {

    protected XmppConnectionService mXmppConnectionService;

    protected AbstractParser(XmppConnectionService service) {
        this.mXmppConnectionService = service;
    }

    public static Long getTimestamp(Element element, Long defaultValue) {
        Element delay = element.findChild("delay", "urn:xmpp:delay");
        if (delay != null) {
            String stamp = delay.getAttribute("stamp");
            if (stamp != null) {
                try {
                    return AbstractParser.parseTimestamp(delay.getAttribute("stamp")).getTime();
                } catch (ParseException e) {
                    return defaultValue;
                }
            }
        }
        return defaultValue;
    }

    public static Date parseTimestamp(String timestamp) throws ParseException {
        timestamp = timestamp.replace("Z", "+0000");
        SimpleDateFormat dateFormat;
        timestamp = timestamp.substring(0, 19) + timestamp.substring(timestamp.length() - 5, timestamp.length());
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        return dateFormat.parse(timestamp);
    }

    protected long getTimestamp(Element packet) {
        return getTimestamp(packet, System.currentTimeMillis());
    }

    protected void updateLastseen(final Element packet, final Account account, final boolean presenceOverwrite) {
        updateLastseen(packet, account, packet.getAttributeAsJid("from"), presenceOverwrite);
    }

    protected void updateLastseen(final Element packet, final Account account, final Jid from, final boolean presenceOverwrite) {
        final String presence = from == null || from.isBareJid() ? "" : from.getResourcepart();
        final Contact contact = account.getRoster().getContact(from);
        final long timestamp = getTimestamp(packet);
        if (timestamp >= contact.lastseen.time) {
            contact.lastseen.time = timestamp;
            if (!presence.isEmpty() && presenceOverwrite) {
                contact.lastseen.presence = presence;
            }
        }
    }

    protected String avatarData(Element items) {
        Element item = items.findChild("item");
        if (item == null) {
            return null;
        }
        return item.findChildContent("data", "urn:xmpp:avatar:data");
    }
}
