package xyz.whisperchat.client.connection.messages.server;

import java.util.Date;

public class PostMessage extends ServerMessage {
    private String author = null,
        message = null;
    private Date timestamp = null;

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String a) {
        author = a;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String m) {
        message = m;
    }

    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date d) {
        timestamp = d;
    }

    @Override
    public String toString() {
        return String.format("Post(%s, \"%s\", %s)", 
            author, message, timestamp);
    }
}
