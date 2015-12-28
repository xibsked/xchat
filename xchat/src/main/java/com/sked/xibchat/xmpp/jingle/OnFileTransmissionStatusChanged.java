package com.sked.xibchat.xmpp.jingle;

import com.sked.xibchat.entities.DownloadableFile;

public interface OnFileTransmissionStatusChanged {
    void onFileTransmitted(DownloadableFile file);

    void onFileTransferAborted();
}
