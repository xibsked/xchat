package com.sked.xibchat.xmpp.jingle;

public interface OnPrimaryCandidateFound {
    void onPrimaryCandidateFound(boolean success,
                                 JingleCandidate canditate);
}
