package xyz.whisperchat.client.connection;

import xyz.whisperchat.client.connection.messages.server.PostMessage;

public interface MessageListener {
    void onMessage(PostMessage msg);
    void onError(Exception e);
}
