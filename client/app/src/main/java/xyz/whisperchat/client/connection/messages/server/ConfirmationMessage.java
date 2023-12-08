package xyz.whisperchat.client.connection.messages.server;

public class ConfirmationMessage extends ServerMessage {
    private ConfirmationStage message;
    public ConfirmationStage getMessage() {
        return message;
    }
    public void setMessage(ConfirmationStage m) {
        message = m;
    }

    @Override
    public String toString() {
        return "Confirmation(" + message + ")";
    }
}
