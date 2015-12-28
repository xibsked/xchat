package com.sked.xibchat.xmpp;

import com.sked.xibchat.entities.Account;

public interface OnMessageAcknowledged {
    void onMessageAcknowledged(Account account, String id);
}
