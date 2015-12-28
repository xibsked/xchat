package com.sked.xibchat.xmpp.stanzas.streammgmt;

import com.sked.xibchat.xmpp.stanzas.AbstractStanza;

public class RequestPacket extends AbstractStanza {

    public RequestPacket(int smVersion) {
        super("r");
        this.setAttribute("xmlns", "urn:xmpp:sm:" + smVersion);
    }

}
