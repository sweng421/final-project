package xyz.whisperchat.client.connection.messages.client;

public class LoginRequest extends ClientRequest {
    public final static String KIND_KEY = "login";
    private char[] password;

    public char[] getPassword() {
        return password;
    }
    public void setPassword(char[] p) {
        password = p;
    }

    public void clearPassword() {
        if (password != null) {
            for (int i = 0; i < password.length; i++) {
                password[i] = '\0';
            }
        }
    }

    public LoginRequest(char[] password) {
        setKind(KIND_KEY);
        setPassword(password);
    }
}
