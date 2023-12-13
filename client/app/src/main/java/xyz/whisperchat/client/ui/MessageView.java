package xyz.whisperchat.client.ui;

import javax.swing.*;
import java.util.*;

import xyz.whisperchat.client.connection.MessageListener;
import xyz.whisperchat.client.connection.messages.server.PostMessage;
import xyz.whisperchat.client.ui.filter.Filter;
import xyz.whisperchat.client.ui.filter.UsernameFilter;

public class MessageView extends JScrollPane implements MessageListener {
    private ArrayList<MessageElement> elements = new ArrayList<>();
    private JPanel messagePanel = new JPanel();
    private Filter filter = null;
    private int currNumElements;
    boolean checkNotifications = false;

    public MessageView() {
        initializeComponents();
    }

    private void initializeComponents() {
        setViewportView(messagePanel);
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    }

    public Filter getFilter() {
        return filter;
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
        synchronized (this){
            elements.add(msgEl);
        }
        currNumElements = elements.size();
        checkNotifications = true;
        messagePanel.revalidate();
    }

    synchronized boolean checkMessageAlert(AlertsAdapter adapter){
        boolean found = false;
        if(checkNotifications) {
            String msg = elements.get(currNumElements - 1).getMessage().getMessage().trim();
            if (msg.contains("@")) {
                for (MessageElement element : elements) {
                    //Check for lone mentions(@name), mentions within a string
                    // of text, and mentions at the end of a string.
                    if (msg.contains(" @" + element.getMessage().getAuthor() + " ")
                    || msg.equals("@" + element.getMessage().getAuthor())
                    ||msg.contains(" @" + element.getMessage().getAuthor())) {
                        //Alert user only if another user mentions them. Self mentions
                        // do not generate alerts.
                        if (!adapter.checkMentions(element.getMessage().getAuthor())) {
                            found = true;
                            checkNotifications = false;
                            break;
                        }
                    }
                }
            }
        }
        return found;
    }
    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }    
}
