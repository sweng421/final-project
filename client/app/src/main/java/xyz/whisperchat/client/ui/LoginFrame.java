package xyz.whisperchat.client.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CompletableFuture;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;

import xyz.whisperchat.client.connection.ChatroomConnection;
import xyz.whisperchat.client.connection.ConnectionBuilder;
import xyz.whisperchat.client.ui.state.login.LoginState;
import xyz.whisperchat.client.ui.state.login.NotReadyState;
import xyz.whisperchat.client.ui.util.AnyDocListener;
import xyz.whisperchat.client.ui.util.FixFont;

public class LoginFrame extends ApplicationFrame implements ActionListener {
    private JTextField serverField = new JTextField(),
        usernameField = new JTextField();
    private JButton loginBtn = new JButton("Connect");

    private LoginState state = new NotReadyState(usernameField, serverField, loginBtn);
    private static final int INSET_SIZE = 7;
    private Insets insets = new Insets(INSET_SIZE, INSET_SIZE, INSET_SIZE, INSET_SIZE);

    public LoginFrame() {
        super("Connect to server");
        initializeComponents();
    }

    private void transition(LoginState.Change c) {
        state = state.processEvent(c);
    }

    private void initializeComponents() {
        JPanel pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        serverField.getDocument()
            .addDocumentListener(new AnyDocListener((DocumentEvent e) -> {
                if (serverField.getText().matches("^[a-zA-Z0-9\\-\\.]+(?:\\:[0-9]+)?$")) {
                    transition(LoginState.Change.URL_READY);
                } else {
                    transition(LoginState.Change.URL_NOT_READY);
                }
            }));
        
        usernameField.getDocument()
            .addDocumentListener(new AnyDocListener((DocumentEvent e) -> {
                if (usernameField.getText().matches("^\\w+$")) {
                    transition(LoginState.Change.USERNAME_READY);
                } else {
                    transition(LoginState.Change.USERNAME_NOT_READY);
                }
            }));

        serverField.setColumns(15);
        fixFont(serverField);
        usernameField.setColumns(15);
        fixFont(usernameField);
        c.insets = insets;

        JLabel title = new JLabel("WhisperChat");
        FixFont.fix(title, 50.0f);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 2; 
        pane.add(title, c);

        JLabel serverLbl = new JLabel("Server: ");
        fixFont(serverLbl);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.EAST;
        pane.add(serverLbl, c);

        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        pane.add(serverField, c);

        JLabel usernameLbl = new JLabel("Username: ");
        fixFont(usernameLbl);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        pane.add(usernameLbl, c);

        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        pane.add(usernameField, c);

        fixFont(loginBtn);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.CENTER;
        pane.add(loginBtn, c);
        loginBtn.addActionListener(this);

        pane.setBorder(new EmptyBorder(insets));
        add(pane);
        setLocationRelativeTo(null);
        pack();
        setMinimumSize(getSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void handleFailure(Throwable t) {
        t.printStackTrace(System.err);
        transition(LoginState.Change.END_CONNECT);

        JTextArea errorMessage = new JTextArea(5, 20);
        errorMessage.setForeground(java.awt.Color.RED);
        errorMessage.setEditable(false);
        errorMessage.setLineWrap(true);
        errorMessage.setWrapStyleWord(true);
        errorMessage.setText(t.getMessage());

        JComponent[] components = { errorMessage };
        
        JOptionPane.showMessageDialog(this, components, "Chatroom error", JOptionPane.ERROR_MESSAGE);
    } 

    @Override
    public void trySwitchState() {
        try {
            transition(LoginState.Change.START_CONNECT);
            CompletableFuture<ChatroomConnection> futureConnection = new ConnectionBuilder()
                .setServer(serverField.getText())
                .setUsername(usernameField.getText())
                .setPromptParent(this)
                .connect();
            futureConnection
                .thenAccept((ChatroomConnection conn) -> {
                    ChatroomFrame f = new ChatroomFrame(this, conn);
                    setVisible(false);
                    transition(LoginState.Change.END_CONNECT);
                    f.setVisible(true);
                })
                .exceptionally((Throwable t) -> {
                    handleFailure(t);
                    return null;
                });
        } catch (Exception ex) {
            handleFailure(ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(loginBtn)) {
            trySwitchState();
        }
    }
}
