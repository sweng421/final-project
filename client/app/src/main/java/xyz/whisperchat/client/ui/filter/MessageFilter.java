package xyz.whisperchat.client.ui.filter;

import xyz.whisperchat.client.connection.messages.server.PostMessage;

public class MessageFilter extends AbstractFilter {
    private String message;
    public MessageFilter(String m) {
        super();
        message = m;
    }
    public MessageFilter(String m, Filter f) {
        super(f);
        message = m;
    }
    
    @Override
    public boolean applies(PostMessage p) {
        return p.getMessage().contains(message);
    }
}
