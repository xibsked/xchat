package com.sked.xibchat.xmpp.jingle;

import com.sked.xibchat.entities.Account;
import com.sked.xibchat.xmpp.PacketReceived;
import com.sked.xibchat.xmpp.jingle.stanzas.JinglePacket;

public interface OnJinglePacketReceived extends PacketReceived {
    void onJinglePacketReceived(Account account, JinglePacket packet);
}
