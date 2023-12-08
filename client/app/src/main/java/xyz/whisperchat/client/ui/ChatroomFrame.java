package xyz.whisperchat.client.ui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

import xyz.whisperchat.client.connection.ChatroomConnection;
import xyz.whisperchat.client.connection.MessageListener;
import xyz.whisperchat.client.connection.messages.server.PostMessage;

public class ChatroomFrame extends StatefulFrame implements MessageListener {
    private ChatroomConnection connection;
    private LoginFrame backFrame;

    public ChatroomFrame(LoginFrame back, ChatroomConnection conn) {
        super();
        state = LOGGED_IN;
        backFrame = back;
        connection = conn;
        connection.addListener(this);
        initializeComponents();
    }

    private void initializeComponents() {
        connection.sendMessage("Test message");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                trySwitchState();
            }
        });
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
