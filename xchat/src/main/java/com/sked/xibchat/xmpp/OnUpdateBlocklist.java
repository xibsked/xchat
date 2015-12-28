package com.sked.xibchat.xmpp;

public interface OnUpdateBlocklist {
    @SuppressWarnings("MethodNameSameAsClassName")
    void OnUpdateBlocklist(final Status status);

    // Use an enum instead of a boolean to make sure we don't run into the boolean trap
    // (`onUpdateBlocklist(true)' doesn't read well, and could be confusing).
    enum Status {
        BLOCKED,
        UNBLOCKED
    }
}
