package com.sked.xibchat.http;

import com.sked.xibchat.entities.Message;
import com.sked.xibchat.services.AbstractConnectionManager;
import com.sked.xibchat.services.XmppConnectionService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HttpConnectionManager extends AbstractConnectionManager {

    private List<HttpConnection> connections = new CopyOnWriteArrayList<>();
    private List<HttpUploadConnection> uploadConnections = new CopyOnWriteArrayList<>();

    public HttpConnectionManager(XmppConnectionService service) {
        super(service);
    }

    public HttpConnection createNewConnection(Message message) {
        HttpConnection connection = new HttpConnection(this);
        connection.init(message);
        this.connections.add(connection);
        return connection;
    }

    public HttpUploadConnection createNewUploadConnection(Message message) {
        HttpUploadConnection connection = new HttpUploadConnection(this);
        connection.init(message);
        this.uploadConnections.add(connection);
        return connection;
    }

    public void finishConnection(HttpConnection connection) {
        this.connections.remove(connection);
    }

    public void finishUploadConnection(HttpUploadConnection httpUploadConnection) {
        this.uploadConnections.remove(httpUploadConnection);
    }
}
