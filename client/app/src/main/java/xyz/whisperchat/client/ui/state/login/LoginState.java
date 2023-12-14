/**
 * PATTERN: State
 * 
 * Used to manage complex UI states
 * Hides and shows buttons depending on
 * current state and transition
*/

package xyz.whisperchat.client.ui.state.login;

import javax.swing.JButton;
import javax.swing.JTextField;

public abstract class LoginState {
    public enum Change {
        USERNAME_READY,
        USERNAME_NOT_READY,
        URL_READY,
        URL_NOT_READY,
        START_CONNECT,
        END_CONNECT,
    }

    public static final String LOGIN_DEFAULT = "Connect", LOGIN_BUSY = "Connecting...";

    protected final JTextField urlInput, usernameInput;
    protected final JButton loginBtn;

    public LoginState(JTextField urlFld, JTextField usrFld, JButton login) {
        urlInput = urlFld;
        usernameInput = usrFld;
        loginBtn = login;
    }

    public LoginState(LoginState s) {
        urlInput = s.urlInput;
        usernameInput = s.usernameInput;
        loginBtn = s.loginBtn;
    }

    protected abstract void enter();
    protected abstract LoginState nextState(Change c);

    public LoginState processEvent(Change c) {
        LoginState s = nextState(c);
        if (s == null) {
            return this;
        }
        s.enter();
        return s;
    }
}
