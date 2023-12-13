package xyz.whisperchat.client.ui;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import xyz.whisperchat.client.connection.MessageListener;
import xyz.whisperchat.client.connection.messages.server.PostMessage;
import xyz.whisperchat.client.ui.filter.Filter;

public class MessageView extends JScrollPane implements MessageListener {
    private ArrayList<MessageElement> elements = new ArrayList<>();
    private JPanel messagePanel = new JPanel();
    private Filter filter = null;

    public MessageView() {
        initializeComponents();
    }

    private void initializeComponents() {
        setViewportView(messagePanel);
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setPreferredSize(getPreferredSize());
        messagePanel.setMinimumSize(getMinimumSize());
    }

    public void setFilter(Filter f) {
        filter = f;
        if (filter != null) {
            f.updateMessages(elements);
        }
    }
    public void clearFilter() {
        filter = null;
        for (MessageElement el : elements) {
            el.setVisible(true);
        }
    }

    @Override
    public void onMessage(PostMessage msg) {
        MessageElement msgEl = new MessageElement(msg);
        msgEl.setVisible(filter == null || filter.valid(msg));
        messagePanel.add(msgEl, 0);
        messagePanel.revalidate();
        elements.add(msgEl);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }    
}
