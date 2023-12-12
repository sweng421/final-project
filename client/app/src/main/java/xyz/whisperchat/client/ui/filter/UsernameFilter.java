package xyz.whisperchat.client.ui.filter;

import xyz.whisperchat.client.connection.messages.server.PostMessage;

public class UsernameFilter extends AbstractFilter {
    private String username;
    public UsernameFilter(String u) {
        super();
        username = u;
    }
    public UsernameFilter(String u, Filter f) {
        super(f);
        username = u;
    }
    
    @Override
    public boolean applies(PostMessage p) {
        return p.getAuthor().equalsIgnoreCase(username);
    }
}
