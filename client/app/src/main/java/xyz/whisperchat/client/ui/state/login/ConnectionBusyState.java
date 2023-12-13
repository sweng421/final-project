package xyz.whisperchat.client.ui.state.login;

public class ConnectionBusyState extends LoginState {
    public ConnectionBusyState(LoginState s) {
        super(s);
    }

    @Override
    protected void enter() {
        loginBtn.setText(LOGIN_BUSY);
        loginBtn.setEnabled(false);
        urlInput.setEnabled(true);
        usernameInput.setEnabled(true);
    }

    @Override
    protected LoginState nextState(Change c) {
        switch (c) {
            case END_CONNECT:
                return new ConnectionReadyState(this);
            default:
                return null;
        }
    }
    
}
