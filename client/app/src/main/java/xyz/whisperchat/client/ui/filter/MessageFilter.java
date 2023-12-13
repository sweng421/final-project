package xyz.whisperchat.client.ui.filter;

import xyz.whisperchat.client.connection.messages.server.PostMessage;

public class MessageFilter extends StringFilter {
    public MessageFilter(String msg) {
        super(msg);
    }
    public MessageFilter(Filter f, String msg) {
        super(f, msg);
    }
    
    @Override
    public boolean applies(PostMessage p) {
        return p.getMessage().contains(getContent());
    }
}
