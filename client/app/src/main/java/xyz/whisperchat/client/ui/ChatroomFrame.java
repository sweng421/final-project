package xyz.whisperchat.client.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ServiceLoader;
import org.pf4j.*;
import xyz.whisperchat.client.connection.ChatroomConnection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ChatroomFrame extends StatefulFrame implements ActionListener {
    private final ChatroomConnection connection;
    private LoginFrame backFrame;
    private JTextArea msgInputField = new JTextArea(),
        pluginType = new JTextArea(),
        anonMsgInput = new JTextArea();
    private JScrollPane anonPane = new JScrollPane(anonMsgInput),
        msgPane = new JScrollPane(msgInputField);
    private MessageView messages = new MessageView();
    private JButton sendMsg = new JButton("Send"),
        backButton = new JButton("Logout"),
        loadPlugin = new JButton("Load Plugin"),
        filter = new JButton("Filter"),
        anon = new JButton("Anonymize");
    JCheckBox alertListener = new JCheckBox();

    public ChatroomFrame(LoginFrame back, ChatroomConnection conn) {
        super("Chatroom at " + conn.getHost());
        state = LOGGED_IN;
        backFrame = back;
        connection = conn;
        connection.addListener(messages);
        initializeComponents();
    }

    private void initializeComponents() {
        this.setVisible(true);
        this.setSize(new Dimension(600, 600));
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        alertListener.setToolTipText("Subscribe to incoming message alerts");

        GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setAutoCreateGaps(true);
        panelLayout.setAutoCreateContainerGaps(true);

        panelLayout.setHorizontalGroup(
                panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, true)
                                .addComponent(messages, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                                .addComponent(anonPane, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                                .addComponent(msgPane, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))
                        .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(alertListener)
                                .addComponent(backButton)
                                .addComponent(loadPlugin)
                                .addComponent(pluginType, 130, 130, 130)
                                .addComponent(filter)
                                .addComponent(sendMsg)
                                .addComponent(anon))
        );

        panelLayout.setVerticalGroup(
                panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(messages, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                                .addGroup(panelLayout.createSequentialGroup()
                                        .addComponent(alertListener)
                                        .addComponent(backButton)
                                        .addComponent(loadPlugin)
                                        .addComponent(pluginType, 60, 60, 60)
                                        .addComponent(filter)))
                        .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(anonPane, 60, 60, 60)
                                .addComponent(anon))
                        .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(msgPane, 60, 60, 60)
                                .addComponent(sendMsg))
        );

        sendMsg.setAlignmentX(backButton.getAlignmentX());
        anonPane.setVisible(false);
        anon.setVisible(false);
        anonMsgInput.setVisible(false);
        anonMsgInput.setLineWrap(true);
        msgInputField.setLineWrap(true);
        pluginType.setEnabled(false);
        pluginType.setVisible(false);

        fixFont(anonMsgInput);
        fixFont(msgInputField);

        alertListener.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (alertListener.isSelected()) {
                    alertListener.setToolTipText("Unsubscribe from incoming message alerts");
                } else {
                    alertListener.setToolTipText("Subscribe to incoming message alerts");
                }
            }
        });

        backButton.addActionListener(this);
        loadPlugin.addActionListener(this);
        sendMsg.addActionListener(this);

        msgInputField.setFocusable(true);
        
        add(panel);
        pack();
        setLocationRelativeTo(null);
        panel.setMinimumSize(getSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public StatefulFrame trySwitchState() {
        setVisible(false);
        connection.close();
        backFrame.setVisible(true);
        this.dispose();
        return backFrame;
    }

    private void sendAction() {
        connection.sendMessage(msgInputField.getText());
    }

    private void loadPluginAction() {
        String pluginName;
        File plugin;

        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new FileNameExtensionFilter("JAR Files", "jar"));
        int returnVal = jFileChooser.showDialog(loadPlugin, "Ok");

        if(returnVal == 0) {
            plugin = jFileChooser.getSelectedFile();
            try {
                URL jarUrl = new URL("file://" + plugin.getPath());
                URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl});
                ServiceLoader<Plugin> serviceLoader = ServiceLoader.load(Plugin.class, classLoader);

                for (Plugin p : serviceLoader) {
                    p.start();
                }
                // Close the class loader
                classLoader.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            //Display the plugin loaded
            pluginName = plugin.getName();
            pluginType.setLineWrap(true);
            pluginType.setText("Plugin type:\n" + pluginName);
            pluginType.setEnabled(false);
            pluginType.setVisible(true);

            //Display the anonymizer button and text area
            anonPane.setVisible(true);
            anonMsgInput.setVisible(true);
            anon.setVisible(true);
            anon.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {   /*****
                    To Do
                    *****/
                }
            });
        }
    }

    private void filterAction() {

    }

    private void anonAction() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(backButton)) {
            trySwitchState();
        } else if (e.getSource().equals(sendMsg)) {
            sendAction();
        } else if (e.getSource().equals(loadPlugin)) {
            loadPluginAction();
        } else if (e.getSource().equals(filter)) {
            filterAction();
        } else if (e.getSource().equals(anon)) {
             anonAction();
        }
    }
}