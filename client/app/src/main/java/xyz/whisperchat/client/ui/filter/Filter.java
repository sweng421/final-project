/**
 * PATTERN: Filter
 * 
 * Filter is used in our project to filter
 * cached messages. Filters are composable
 * for more powerful searches
*/

package xyz.whisperchat.client.ui.filter;

import xyz.whisperchat.client.connection.messages.server.PostMessage;
import xyz.whisperchat.client.ui.MessageElement;

public interface Filter {
    public void updateMessages(Iterable<MessageElement> msgs);
    public boolean valid(PostMessage msg);
}
