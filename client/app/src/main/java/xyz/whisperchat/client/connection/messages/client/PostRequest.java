package xyz.whisperchat.client.connection.messages.client;

public class PostRequest extends ClientRequest {
    public final static String KIND_KEY = "post";
    private String message;

    public String getMessage() {
        return message;
    }
    public void setMessage(String m) {
        message = m;
    }

    public PostRequest(String message) {
        setKind(KIND_KEY);
        setMessage(message);
    }
}
