package xyz.whisperchat.client.ui.filter;

import xyz.whisperchat.client.connection.messages.server.PostMessage;
import xyz.whisperchat.client.ui.MessageElement;

public abstract class AbstractFilter implements Filter {
    private Filter prevFilter = null;
    public AbstractFilter(Filter f) {
        prevFilter = f;
    }
    public AbstractFilter() {}

    public boolean valid(PostMessage msg) {
        if (prevFilter != null) {
            return prevFilter.valid(msg) && applies(msg);
        }
        return applies(msg);
    }
    public void updateMessages(Iterable<MessageElement> msgs) {
        for (MessageElement el : msgs) {
            el.setVisible(valid(el.getMessage()));
        }
    }
    public void setParent(Filter f) {
        prevFilter = f;
    }
    public abstract boolean applies(PostMessage p);
}
