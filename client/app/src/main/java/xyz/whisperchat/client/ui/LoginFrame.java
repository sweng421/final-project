package xyz.whisperchat.client.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import xyz.whisperchat.client.connection.ChatroomConnection;
import xyz.whisperchat.client.connection.ConnectionBuilder;

public class LoginFrame extends StatefulFrame implements ActionListener {
    private JTextField serverField = new JTextField(),
        usernameField = new JTextField();
    private JButton loginBtn = new JButton("Connect");

    private static final int INSET_SIZE = 7;
    private Insets insets = new Insets(INSET_SIZE, INSET_SIZE, INSET_SIZE, INSET_SIZE);

    public LoginFrame() {
        super("Connect to server");
        state = StatefulFrame.SIGNED_OUT;
        initializeComponents();
    }

    private void initializeComponents() {
        JPanel pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        serverField.setColumns(15);
        fixFont(serverField);
        usernameField.setColumns(15);
        fixFont(usernameField);
        c.insets = insets;

        JLabel serverLbl = new JLabel("Server: ");
        fixFont(serverLbl);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        pane.add(serverLbl, c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        pane.add(serverField, c);

        JLabel usernameLbl = new JLabel("Username: ");
        fixFont(usernameLbl); 
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        pane.add(usernameLbl, c);

        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        pane.add(usernameField, c);

        fixFont(loginBtn);
        c.gridx = 0;
        c.gridy = 2;
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

    @Override
    public StatefulFrame trySwitchState() {
        try {
            ChatroomConnection conn = new ConnectionBuilder()
                .setServer(serverField.getText())
                .setUsername(usernameField.getText())
                .setPromptParent(this)
                .connect()
                .get(); // todo make async?
            ChatroomFrame f = new ChatroomFrame(this, conn);
            setVisible(false);
            f.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(loginBtn)) {
            trySwitchState();
        }
    }
}
