package xyz.whisperchat.client.connection.messages.client;

public class UsernameRequest extends ClientRequest {
    public final static String KIND_KEY = "username";
    private String username;

    public String getUsername() {
        return username;
    }
    public void setUsername(String u) {
        username = u;
    }

    public UsernameRequest(String username) {
        setKind(KIND_KEY);
        setUsername(username);
    }
}
