package com.sked.xibchat.xmpp.jingle.stanzas;

import com.sked.xibchat.xml.Element;

public class Reason extends Element {
    private Reason(String name) {
        super(name);
    }

    public Reason() {
        super("reason");
    }
}
