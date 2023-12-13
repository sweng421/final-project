package xyz.whisperchat.client.ui.state.login;

public class UrlReadyState extends NotReadyState {
    public UrlReadyState(LoginState s) {
        super(s);
    }

    @Override
    protected LoginState nextState(Change c) {
        switch (c) {
            case URL_NOT_READY:
                return new NotReadyState(this);
            case USERNAME_READY:
                return new ConnectionReadyState(this);
            default:
                return null;
        }
    }
    
}
