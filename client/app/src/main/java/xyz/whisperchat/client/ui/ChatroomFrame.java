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
import xyz.whisperchat.client.connection.MessageListener;
import xyz.whisperchat.client.connection.messages.server.PostMessage;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ChatroomFrame extends StatefulFrame implements MessageListener,
        ActionListener
{
    int changeEventHandler = 0;    //Check this value before playing incoming alert beep to ensure
    //change events were not made locally
    private final ChatroomConnection connection;
    private LoginFrame backFrame;
    private JTextArea msgInputField = new JTextArea();
    private JTextArea pluginType = new JTextArea();
    private MessageView messages = new MessageView();
    //private JTextArea populatedMessages = new JTextArea(); //Message view
    private JTextArea anonMsgInput = new JTextArea();     //Anonymizer text input
    private JButton sendMsg = new JButton("Send");
    private JButton backButton = new JButton("Logout");
    private JButton loadPlugin = new JButton("Load Plugin");
    private JButton filter = new JButton("Filter");
    private JButton anon = new JButton("Anonymize");
    JCheckBox alertListener = new JCheckBox();

    public ChatroomFrame(LoginFrame back, ChatroomConnection conn) {
        super("Chatroom at " + conn.getHost());
        state = LOGGED_IN;
        backFrame = back;
        connection = conn;
        connection.addListener(this);
        connection.addListener(messages);
        initializeComponents();
    }

    private void initializeComponents() {
        //connection.sendMessage("Success!");

        this.setVisible(true);
        this.setSize(new Dimension(600, 600));
        this.setMinimumSize(this.getSize());
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JScrollPane scrollPane = messages;
        JScrollPane scrollPane2 = new JScrollPane(anonMsgInput);
        JScrollPane scrollPane3 = new JScrollPane(msgInputField);
        alertListener.setToolTipText("Subscribe to incoming message alerts");

        GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setAutoCreateGaps(true);
        panelLayout.setAutoCreateContainerGaps(true);

        panelLayout.setHorizontalGroup(
                panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, true)
                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                                .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                                .addComponent(scrollPane3, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))
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
                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                                .addGroup(panelLayout.createSequentialGroup()
                                        .addComponent(alertListener)
                                        .addComponent(backButton)
                                        .addComponent(loadPlugin)
                                        .addComponent(pluginType, 60, 60, 60)
                                        .addComponent(filter)))
                        .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(scrollPane2, 60, 60, 60)
                                .addComponent(anon))
                        .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(scrollPane3, 60, 60, 60)
                                .addComponent(sendMsg))
        );

        scrollPane2.setVisible(false);
        anon.setVisible(false);
        anonMsgInput.setVisible(false);
        anonMsgInput.setLineWrap(true);
        msgInputField.setLineWrap(true);
        pluginType.setEnabled(false);
        pluginType.setVisible(false);

        fixFont(anonMsgInput);
        fixFont(msgInputField);

        alertListener.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                if(alertListener.isSelected())
                {
                    alertListener.setToolTipText("Unsubscribe from incoming message alerts");
                }
                else {
                    alertListener.setToolTipText("Subscribe to incoming message alerts");
                }
                /*populatedMessages.getDocument().addDocumentListener(new DocumentListener()
                {
                    @Override
                    public void insertUpdate(DocumentEvent e)
                    {
                        if(changeEventHandler != 1)
                        {
                            Toolkit.getDefaultToolkit().beep();
                        }
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e)
                    {
                        if(changeEventHandler != 1)
                            Toolkit.getDefaultToolkit().beep();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e)
                    {
                        if(changeEventHandler != 1)
                            Toolkit.getDefaultToolkit().beep();
                    }
                });*/

            }
        });

        backButton.addActionListener(this);
        loadPlugin.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                changeEventHandler = 1;
                String pluginName;
                File plugin;

                JFileChooser jFileChooser = new JFileChooser();

                jFileChooser.setFileFilter(new FileNameExtensionFilter("JAR Files", "jar"));
                int returnVal = jFileChooser.showDialog(loadPlugin, "Ok");

                if(returnVal == 0)
                {
                    plugin = jFileChooser.getSelectedFile();
                    try
                    {
                        URL jarUrl = new URL("file://" + plugin.getPath());
                        URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl});
                        ServiceLoader<Plugin> serviceLoader = ServiceLoader.load(Plugin.class, classLoader);

                        for (Plugin p : serviceLoader) {
                            p.start();
                        }
                        // Close the class loader
                        classLoader.close();
                    }
                    catch (IOException ex)
                    {
                        throw new RuntimeException(ex);
                    }

                    //Display the plugin loaded
                    pluginName = plugin.getName();
                    pluginType.setLineWrap(true);
                    pluginType.setText("Plugin type:\n" + pluginName);
                    pluginType.setEnabled(false);
                    pluginType.setVisible(true);

                    //Display the anonymizer button and text area
                    scrollPane2.setVisible(true);
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
                changeEventHandler = 0;
            }
        });

        sendMsg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = msgInputField.getText();
                if(!msg.equals("") && !msg.equals("\n") && !msg.equals(" "))
                {
                    connection.sendMessage(msg);
                }
                msgInputField.setText("");
                panel.revalidate();
            }
        });

        msgInputField.setFocusable(true);
        msgInputField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e)
            {
                changeEventHandler = 1;
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    String msg = msgInputField.getText();

                    if(!msg.equals("") && !msg.equals("\n") && !msg.equals(" "))
                    {
                        connection.sendMessage(msg);
                    }
                    msgInputField.setText("");
                    panel.revalidate();
                }
                changeEventHandler = 0;
            }

            @Override
            public void keyPressed(KeyEvent e) {}
        });
        add(panel);
        setLocationRelativeTo(null);
        panel.setPreferredSize(getSize());
        panel.setMinimumSize(getMinimumSize());
        panel.setMaximumSize(getMaximumSize());
        pack();
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

    @Override
    public void onMessage(PostMessage msg) {
        messages.validate();
        this.revalidate();
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(backButton)) {
            trySwitchState();
        }
    }
}