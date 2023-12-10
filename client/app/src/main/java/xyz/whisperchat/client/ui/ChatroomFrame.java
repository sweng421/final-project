package xyz.whisperchat.client.ui;

import java.awt.event.WindowEvent;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.*;

import xyz.whisperchat.client.connection.ChatroomConnection;
import xyz.whisperchat.client.connection.MessageListener;
import xyz.whisperchat.client.connection.messages.server.PostMessage;

public class ChatroomFrame extends StatefulFrame implements MessageListener {
    private ChatroomConnection connection;
    private LoginFrame backFrame;

    private JButton logoutBtn = new JButton("Log out"),
        loadPluginBtn = new JButton("Load plugin"),
        filterButton = new JButton("Filters");
    private JLabel pluginPath = new JLabel("");
    
    private JTextArea modelInput = new JTextArea(3, 20),
        messageInput = new JTextArea(3, 20);
    private JButton anonBtn = new JButton("Anonymize"),
        sendBtn = new JButton("Send");

    private static final int INSET_SIZE = 10;
    private Insets insets = new Insets(INSET_SIZE, INSET_SIZE, INSET_SIZE, INSET_SIZE);

    public ChatroomFrame(LoginFrame back, ChatroomConnection conn) {
        super();
        state = LOGGED_IN;
        backFrame = back;
        connection = conn;
        connection.addListener(this);
        initializeComponents();
    }

    private void initializeComponents() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                trySwitchState();
            }
        });

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.add(logoutBtn);
        sidebar.add(loadPluginBtn);
        sidebar.add(filterButton);
        sidebar.add(pluginPath);
        add(sidebar, BorderLayout.EAST);

        JScrollPane pane = new JScrollPane();
        pane.setPreferredSize(new Dimension(300, 300));
        add(pane, BorderLayout.CENTER);

        JPanel discussionArea = new JPanel();
        discussionArea.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        modelInput.setLineWrap(true);
        messageInput.setLineWrap(true);

        modelInput.setEnabled(false);
        anonBtn.setEnabled(false);
        
        c.insets = insets;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.7d;
        discussionArea.add(new JScrollPane(modelInput), c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.3d;
        c.fill = GridBagConstraints.NONE;
        discussionArea.add(anonBtn, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.7d;
        c.fill = GridBagConstraints.HORIZONTAL;
        discussionArea.add(new JScrollPane(messageInput), c);

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.3d;
        c.fill = GridBagConstraints.NONE;
        discussionArea.add(sendBtn, c);
        
        add(discussionArea, BorderLayout.SOUTH);

        pack();
        setMinimumSize(getSize());
    }

    @Override
    public StatefulFrame trySwitchState() {
        setVisible(false);
        connection.close();
        backFrame.setVisible(true);
        dispose();
        return backFrame;
    }

    @Override
    public void onMessage(PostMessage msg) {
        System.out.println(msg);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
