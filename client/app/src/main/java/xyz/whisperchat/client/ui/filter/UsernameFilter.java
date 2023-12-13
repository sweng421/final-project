package xyz.whisperchat.client.ui.filter;

import xyz.whisperchat.client.connection.messages.server.PostMessage;

public class UsernameFilter extends StringFilter {
    public UsernameFilter(String username) {
        super(username);
    }
    public UsernameFilter(Filter f, String username) {
        super(f, username);
    }
    
    @Override
    public boolean applies(PostMessage p) {
        return p.getAuthor().equalsIgnoreCase(getContent());
    }
}
