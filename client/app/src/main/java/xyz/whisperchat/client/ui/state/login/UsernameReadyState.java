package xyz.whisperchat.client.ui.state.login;

public class UsernameReadyState extends NotReadyState {
    public UsernameReadyState(LoginState s) {
        super(s);
    }

    @Override
    protected LoginState nextState(Change c) {
        switch (c) {
            case USERNAME_NOT_READY:
                return new NotReadyState(this);
            case URL_READY:
                return new ConnectionReadyState(this);
            default:
                return null;
        }
    }
    
}
