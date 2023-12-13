package xyz.whisperchat.client.ui.filter;

public abstract class StringFilter extends AbstractFilter {
    private final String content;
    public StringFilter(Filter f, String s) {
        super(f);
        content = s;
    }
    public StringFilter(String s) {
        super();
        content = s;
    }
    
    public String getContent() {
        return content;
    }
}
