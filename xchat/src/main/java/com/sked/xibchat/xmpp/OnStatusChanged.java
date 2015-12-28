package com.sked.xibchat.xmpp;

import com.sked.xibchat.entities.Account;

public interface OnStatusChanged {
    void onStatusChanged(Account account);
}
