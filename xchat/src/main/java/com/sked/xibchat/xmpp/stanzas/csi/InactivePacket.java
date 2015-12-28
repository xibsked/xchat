package com.sked.xibchat.xmpp.stanzas.csi;

import com.sked.xibchat.xmpp.stanzas.AbstractStanza;

public class InactivePacket extends AbstractStanza {
    public InactivePacket() {
        super("inactive");
        setAttribute("xmlns", "urn:xmpp:csi:0");
    }
}
