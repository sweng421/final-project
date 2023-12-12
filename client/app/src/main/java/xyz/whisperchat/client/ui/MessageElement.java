package xyz.whisperchat.client.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import org.apache.commons.text.StringEscapeUtils;

import javax.swing.*;

import xyz.whisperchat.client.connection.messages.server.PostMessage;

public class MessageElement extends JPanel {
    public final PostMessage message;
    private final static SimpleDateFormat dateFmt = 
        new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss"); 
    public MessageElement(PostMessage m) {
        message = m;
        initializeComponents();
    }

    public PostMessage getMessage() {
        return message;
    }

    private void initializeComponents() {    
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        String htmlMsg = "<html>" + StringEscapeUtils.escapeHtml4(message.getMessage())
            .replaceAll("\r\n", "<br />")
            .replaceAll("\n", "<br />") + "</html>";
        String postTime = dateFmt.format(message.getTimestamp());
        
        JLabel authorLbl = new JLabel(message.getAuthor() + " says: "),
            dateLbl = new JLabel(postTime),
            postLbl = new JLabel(htmlMsg);
        
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 1.0f;
        add(authorLbl, c);

        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 0.0f;
        add(dateLbl, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weightx = 1.0f;
        c.anchor = GridBagConstraints.LINE_START;
        add(postLbl, c);
    }
}
