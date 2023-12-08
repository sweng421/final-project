package xyz.whisperchat.client.connection;

interface ConnectionListener {
    void onConnect(ChatroomConnection conn);
    void onError(Exception e);
}
