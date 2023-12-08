package xyz.whisperchat.client.connection.messages.server;

public class ErrorMessage extends ServerMessage {
    private String error;
    public String getError() {
        return error;
    }
    public void setError(String e) {
        error = e;
    }

    @Override
    public String toString() {
        return "Error(" + error + ")";
    }
}
