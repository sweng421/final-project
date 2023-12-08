package xyz.whisperchat.client.connection.messages.server;

public class ServerException extends Exception {
    public ServerException(String msg) {
        super("Error from chatroom server: " + msg);
    }
}
