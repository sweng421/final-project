package xyz.whisperchat.client.ui.state.login;

import javax.swing.JButton;
import javax.swing.JTextField;

public class NotReadyState extends LoginState {
    public NotReadyState(JTextField urlFld, JTextField usrFld, JButton login) {
        super(urlFld, usrFld, login);
        enter();
    }

    public NotReadyState(LoginState s) {
        super(s);
    }

    @Override
    protected void enter() {
        loginBtn.setText(LOGIN_DEFAULT);
        loginBtn.setEnabled(false);
        urlInput.setEnabled(true);
        usernameInput.setEnabled(true);
    }

    @Override
    protected LoginState nextState(Change c) {
        switch (c) {
            case URL_READY:
                return new UrlReadyState(this);
            case USERNAME_READY:
                return new UsernameReadyState(this);
            default:
                return null;
        }
    }
}
