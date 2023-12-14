package xyz.whisperchat.client.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import xyz.whisperchat.client.connection.ChatroomConnection;
import xyz.whisperchat.client.connection.MessageListener;
import xyz.whisperchat.client.connection.messages.server.PostMessage;
import xyz.whisperchat.client.plugin.ChatPluginLoader;
import xyz.whisperchat.client.plugin.StylometricAnonymizer;
import xyz.whisperchat.client.plugin.UtilImpl;
import xyz.whisperchat.client.ui.filter.Filter;
import xyz.whisperchat.client.ui.state.chatroom.ChatroomState;
import xyz.whisperchat.client.ui.state.chatroom.NoPluginState;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ChatroomFrame extends ApplicationFrame implements ActionListener, MessageListener {
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
        clearFilter = new JButton("Clear Filter"),
        anon = new JButton("Anonymize");
    private JCheckBox alertListener = new JCheckBox();
    private StylometricAnonymizer plugin = null;
    private ExecutorService pluginExecutor = null;
    private ChatroomState pluginState = null;
    private JPanel panel = new JPanel();

    public ChatroomFrame(LoginFrame back, ChatroomConnection conn) {
        super("Chatroom at " + conn.getHost());
        backFrame = back;
        connection = conn;
        connection.addListener(messages);
        initializeComponents();
    }

    // Runs a plugin action in a seperate thread
    private void runPluginAction(Runnable task) {
        if (pluginExecutor == null) {
            pluginExecutor = Executors.newSingleThreadExecutor();
        }
        pluginExecutor.submit(task);
    }

    private void transition(ChatroomState.Change c) {
        pluginState = pluginState.processEvent(c);
    }

    private void initializeComponents() {
        this.setVisible(true);
        this.setSize(new Dimension(700, 700));
        this.setPreferredSize(getSize());
        this.setMinimumSize(this.getSize());
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
                                .addComponent(clearFilter)
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
                                        .addComponent(filter)
                                        .addComponent(clearFilter)))
                        .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(anonPane, 60, 60, 60)
                                .addComponent(anon))
                        .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(msgPane, 60, 60, 60)
                                .addComponent(sendMsg))
        );

        pluginState = new NoPluginState(loadPlugin, sendMsg, anon, pluginType, msgInputField, 
                anonMsgInput, msgPane, anonPane);

        anonPane.setVisible(false);
        anon.setVisible(false);
        anonMsgInput.setVisible(false);
        anonMsgInput.setLineWrap(true);
        msgInputField.setLineWrap(true);
        pluginType.setEnabled(false);
        pluginType.setVisible(false);
        pluginType.setLineWrap(true);

        fixFont(anonMsgInput);
        fixFont(msgInputField);

        alertListener.addActionListener(this);
        backButton.addActionListener(this);

        msgInputField.setFocusable(true);
        setupEnterKeystrokes();

        loadPlugin.addActionListener(this);
        sendMsg.addActionListener(this);
        anon.addActionListener(this);
        filter.addActionListener(this);
        clearFilter.addActionListener(this);
        clearFilter.setEnabled(false);

        msgInputField.setFocusable(true);

        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                setMinimumSize(getPreferredSize());
            }
        });

        add(panel);
        setLocationRelativeTo(null);

        panel.setPreferredSize(getSize());
        panel.setMaximumSize(getMaximumSize());
        panel.setMinimumSize(getSize());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setupEnterKeystrokes() {
        String INSERT_NEWLINE = "ins-newline",
            SEND_POST = "send-post";

        InputMap imap = msgInputField.getInputMap();
        imap.put(KeyStroke.getKeyStroke("shift ENTER"), INSERT_NEWLINE);
        imap.put(KeyStroke.getKeyStroke("ENTER"), SEND_POST);

        ActionMap amap = msgInputField.getActionMap();
        amap.put(SEND_POST, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendAction();
            }
        });
        amap.put(INSERT_NEWLINE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int caret = msgInputField.getCaretPosition();
                String start = msgInputField.getText().substring(0, caret);
                String end = msgInputField.getText().substring(caret);
                msgInputField.setText(start + "\n" + end);
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();
        connection.close();
        if (pluginExecutor != null) {
            pluginExecutor.shutdownNow();
        }
        killPlugin();
    }

    @Override
    public void trySwitchState() {
        setVisible(false);
        backFrame.setVisible(true);
        this.dispose();
    }

    private void sendAction() {
        String msg = msgInputField.getText().trim();
        if (msg.length() > 0 && msg.length() <= connection.getSettings().getMaxMsgLen()) {
            connection.sendMessage(msg);
            msgInputField.setText("");
            revalidate();
        }
    }

    private void showErrorDialog(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Chatroom error", JOptionPane.ERROR_MESSAGE);
    }

    private void loadPluginAction() {
        JFileChooser ofd = new JFileChooser();
        ofd.setFileFilter(new FileNameExtensionFilter("JAR Files", "jar"));

        if(ofd.showDialog(this, "Load plugin") == JFileChooser.APPROVE_OPTION) {
            // Get file dialog data
            File pluginFile = ofd.getSelectedFile();
            final String fileName = pluginFile.getName();
            try {
                // Load plugin from jar
                ChatPluginLoader loader = new ChatPluginLoader();
                final StylometricAnonymizer plugin = loader.loadChatPlugin(pluginFile);

                // Init util object
                final UtilImpl util = new UtilImpl(connection.getSettings().getMaxMsgLen());
                util.setParent(this);

                // Attempt to run setup function
                runPluginAction(() -> {
                    transition(ChatroomState.Change.START_LOAD_PLUGIN);
                    try {
                        plugin.setup(util);
                        setPlugin(plugin, fileName);
                    } catch (Exception ex) {
                        showErrorDialog("Could not set up plugin from " + fileName);
                        ex.printStackTrace(System.err);
                    } finally {
                        transition(ChatroomState.Change.STOP_LOAD_PLUGIN);
                    }
                });
            } catch (Exception ex) {
                showErrorDialog("Could not load plugin from " + fileName);
                ex.printStackTrace(System.err);
            }
        }
    }

    private void killPlugin() {
        if (plugin != null) {
            try {
                plugin.close();
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            plugin = null;
        }
    }

    private void setPlugin(StylometricAnonymizer p, String name) {
        // Unload current plugin
        killPlugin();

        // Add new plugin (assumed to be already be setup)
        plugin = p;
        if (plugin != null) {
            pluginType.setText("Plugin loaded:\n" + name);
        }
    }

    private void filterAction() {
        FilterUI fd = new FilterUI(this);
        Filter filter = fd.newFilter(messages.getFilter());
        if (filter != null) {
            messages.setFilter(filter);
            clearFilter.setEnabled(true);
        }
    }

    private void clearFilterAction() {
        messages.clearFilter();
        clearFilter.setEnabled(false);
    }

    private void anonAction() {
        String anonText = anonMsgInput.getText().trim();
        if (plugin != null && anonText.length() > 0) {
            runPluginAction(() -> {
                transition(ChatroomState.Change.START_USING_PLUGIN);
                try {
                    msgInputField.setText(plugin.anonymize(anonText));
                } catch (Exception ex) {
                    showErrorDialog("Anonymizer plugin failed");
                    ex.printStackTrace(System.err);
                } finally {
                    transition(ChatroomState.Change.STOP_USING_PLUGIN);
                }
            });
        }
    }

    private void messageAlertAction() {
        if (alertListener.isSelected()) {
            alertListener.setToolTipText("Unsubscribe from incoming message alerts");
            connection.addListener(this);
        } else {
            alertListener.setToolTipText("Subscribe to incoming message alerts");
            connection.removeListener(this);
        }
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
        } else if (e.getSource().equals(clearFilter)) {
            clearFilterAction();
        } else if (e.getSource().equals(alertListener)) {
            messageAlertAction();
        }
    }

    @Override
    public void onMessage(PostMessage msg) {
        if (msg.mentions(connection.getUsername())) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void onError(Exception e) {}
}
