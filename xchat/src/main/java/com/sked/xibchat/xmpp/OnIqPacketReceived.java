package com.sked.xibchat.xmpp;

import com.sked.xibchat.entities.Account;
import com.sked.xibchat.xmpp.stanzas.IqPacket;

public interface OnIqPacketReceived extends PacketReceived {
    void onIqPacketReceived(Account account, IqPacket packet);
}
