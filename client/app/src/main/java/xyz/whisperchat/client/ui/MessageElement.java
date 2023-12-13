package xyz.whisperchat.client.ui;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.SimpleDateFormat;

import javax.swing.*;

import xyz.whisperchat.client.connection.messages.server.PostMessage;

public class MessageElement extends JPanel {
    public final PostMessage message;
    private final static SimpleDateFormat dateFmt = 
        new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    public MessageElement(PostMessage m) {
        message = m;
        initializeComponents();
    }

    public PostMessage getMessage() {
        return message;
    }

    private void initializeComponents() {
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        String postTime = dateFmt.format(message.getTimestamp());

        //Msg view variable definitions
        JLabel authorLbl = new JLabel(message.getAuthor() + " says: "),
            dateLbl = new JLabel(postTime);
        JTextArea msgArea = new JTextArea(message.getMessage());

        //Text properties
        msgArea.setLineWrap(true);
        msgArea.setWrapStyleWord(true);
        msgArea.setEnabled(false);
        msgArea.setBackground(this.getBackground());

        fixFont(authorLbl);
        fixFont(dateLbl);
        fixFont(msgArea);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(authorLbl, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(dateLbl, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(msgArea, 0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(authorLbl, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(dateLbl, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addComponent(msgArea, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        layout.linkSize(authorLbl, dateLbl);
        this.addComponentListener(new ComponentListener()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                setPreferredSize(getPreferredSize());
            }

            @Override
            public void componentMoved(ComponentEvent e) {}

            @Override
            public void componentShown(ComponentEvent e) {}

            @Override
            public void componentHidden(ComponentEvent e) {}
        });
    }
    protected void fixFont(JComponent c) {
        c.setFont(c.getFont().deriveFont(16.0f));
    }
}
