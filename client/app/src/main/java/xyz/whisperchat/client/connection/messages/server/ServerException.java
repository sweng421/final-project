package xyz.whisperchat.client.connection.messages.server;

public class ServerException extends Exception {
    private final String message;
    public ServerException(String msg) {
        super("Error from chatroom server: " + msg);
        message = msg;
    }
    public String getServerError() {
        return message;
    }
}
