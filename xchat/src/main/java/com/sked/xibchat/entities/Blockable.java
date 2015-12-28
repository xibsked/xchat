package com.sked.xibchat.entities;

import com.sked.xibchat.xmpp.jid.Jid;

public interface Blockable {
    boolean isBlocked();

    boolean isDomainBlocked();

    Jid getBlockedJid();

    Jid getJid();

    Account getAccount();
}
