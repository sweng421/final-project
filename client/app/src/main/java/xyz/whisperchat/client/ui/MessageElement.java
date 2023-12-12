package xyz.whisperchat.client.ui;

import javax.swing.JPanel;

import xyz.whisperchat.client.connection.messages.server.PostMessage;

public class MessageElement extends JPanel {
    public final PostMessage message;
    public MessageElement(PostMessage m) {
        message = m;
        initializeComponents();
    }

    private void initializeComponents() {
        //
    }
}
