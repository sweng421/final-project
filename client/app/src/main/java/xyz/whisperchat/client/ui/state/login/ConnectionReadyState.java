package xyz.whisperchat.client.ui.state.login;

public class ConnectionReadyState extends LoginState {
    public ConnectionReadyState(LoginState s) {
        super(s);
    }

    @Override
    protected void enter() {
        loginBtn.setText(LOGIN_DEFAULT);
        loginBtn.setEnabled(true);
        urlInput.setEnabled(true);
        usernameInput.setEnabled(true);
    }

    @Override
    protected LoginState nextState(Change c) {
        switch (c) {
            case USERNAME_NOT_READY:
                return new UrlReadyState(this);
            case URL_NOT_READY:
                return new UsernameReadyState(this);
            case START_CONNECT:
                return new ConnectionBusyState(this);
            default:
                return null;
        }
    }
}
