package com.sked.xibchat.xmpp;

import com.sked.xibchat.entities.Account;
import com.sked.xibchat.xmpp.stanzas.MessagePacket;

public interface OnMessagePacketReceived extends PacketReceived {
    void onMessagePacketReceived(Account account, MessagePacket packet);
}
