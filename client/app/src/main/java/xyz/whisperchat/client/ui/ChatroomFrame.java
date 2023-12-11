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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ChatroomFrame extends StatefulFrame implements MessageListener, UI_Interface,
        ActionListener
{
    int changeEventHandler = 0;    //Check this value before playing incoming alert beep to ensure
    //change events were not made locally
    private ChatroomConnection connection;
    private LoginFrame backFrame;
    String userName;    //Used to refer sender to the user in populated message view
    private JTextArea msgInputField = new JTextArea();
    private JTextArea pluginType = new JTextArea();
    private JTextArea populatedMessages = new JTextArea(); //Message view
    private JTextArea anonMsgInput = new JTextArea();     //Anonymizer text input
    private JButton sendMsg = new JButton("Send");
    private JButton backButton = new JButton("Logout");
    private JButton loadPlugin = new JButton("Load Plugin");
    private JButton filter = new JButton("Filter");
    private JButton anon = new JButton("Anonymize");
    JCheckBox alertListener = new JCheckBox();
    private static final float FONT_SIZE = 17.0f;

    public ChatroomFrame(LoginFrame back, ChatroomConnection conn, String userName) {
        super();
        state = LOGGED_IN;
        backFrame = back;
        connection = conn;
        connection.addListener(this);
        this.userName = userName;
        initializeComponents();
    }

    private void initializeComponents() {
        connection.sendMessage("Success!");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                trySwitchState();
            }
        });

        this.setVisible(true);
        this.setSize(new Dimension(700, 800));
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(populatedMessages);
        final JScrollPane scrollPane2 = new JScrollPane(anonMsgInput);
        JScrollPane scrollPane3 = new JScrollPane(msgInputField);
        alertListener.setToolTipText("Subscribe to incoming message alerts");

        GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setAutoCreateGaps(true);
        panelLayout.setAutoCreateContainerGaps(true);
        panelLayout.setHorizontalGroup(
                panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(alertListener)
                                        .addComponent(backButton)
                                        .addComponent(loadPlugin)
                                        .addComponent(pluginType, 130, 130, 130)
                                        .addComponent(filter)))
                        .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE - 130)
                                .addComponent(anon))
                        .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(scrollPane3, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE - 130)
                                .addComponent(sendMsg))
        );
        System.out.println(filter.getX());
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
        populatedMessages.setLineWrap(true);
        populatedMessages.setEnabled(false);
        pluginType.setEnabled(false);
        pluginType.setVisible(false);

        fixFont(anonMsgInput);
        fixFont(msgInputField);
        fixFont(populatedMessages);
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
                populatedMessages.getDocument().addDocumentListener(new DocumentListener()
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
                });

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
                    catch (IOException ex)
                    {
                        throw new RuntimeException(ex);
                    }
                }
                changeEventHandler = 0;
            }
        });

        sendMsg.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                changeEventHandler = 1;
                String msg = msgInputField.getText();

                if(populatedMessages.getText().equals(""))
                    populatedMessages.setText(userName + ":\n" + msg);
                else
                {
                    populatedMessages.setText(populatedMessages.getText() +
                            "\n" + userName + ":\n" + msg);
                }
                msgInputField.setText("");
                changeEventHandler = 0;
            }
        });
        msgInputField.setFocusable(true);
        msgInputField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
            }
            @Override
            public void keyReleased(KeyEvent e)
            {
                changeEventHandler = 1;
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    String msg = msgInputField.getText();

                    if(!msg.equals("") && !msg.equals("\n") && !msg.equals(" "))
                    {
                        if(populatedMessages.getText().equals(""))
                        {
                            populatedMessages.setText(userName + ":\n" + msg);
                        }
                        else
                        {
                            populatedMessages.setText(populatedMessages.getText() +
                                    "\n" + userName + ":\n" + msg);
                        }
                    }
                    msgInputField.setText("");
                }
                changeEventHandler = 0;
            }

            @Override
            public void keyPressed(KeyEvent e)
            {
            }
        });
        add(panel);
        pack();
        setLocationRelativeTo(null);
        panel.setMinimumSize(getMinimumSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public StatefulFrame trySwitchState() {
        setVisible(false);
        connection.close();
        backFrame.setVisible(true);
        dispose();
        return backFrame;
    }

    @Override
    public void onMessage(PostMessage msg) {
        System.out.println(msg);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
    @Override
    public void fixFont(JComponent c)
    {
        c.setFont(c.getFont().deriveFont(FONT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(backButton)) {
            trySwitchState();
        }
    }
}