package com.sked.xibchat.xmpp;

import com.sked.xibchat.entities.Contact;

public interface OnContactStatusChanged {
    void onContactStatusChanged(final Contact contact, final boolean online);
}
