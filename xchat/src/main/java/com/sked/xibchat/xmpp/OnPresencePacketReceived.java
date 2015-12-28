package com.sked.xibchat.xmpp;

import com.sked.xibchat.entities.Account;
import com.sked.xibchat.xmpp.stanzas.PresencePacket;

public interface OnPresencePacketReceived extends PacketReceived {
    void onPresencePacketReceived(Account account, PresencePacket packet);
}
