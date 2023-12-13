package xyz.whisperchat.client.ui.state.chatroom;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public abstract class ChatroomState {
    public enum Change {
        START_LOAD_PLUGIN,
        STOP_LOAD_PLUGIN,
        START_USING_PLUGIN,
        STOP_USING_PLUGIN,
    }

    protected final static String LOAD_PLUGIN_DEFAULT = "Load Plugin",
            LOAD_PLUGIN_BUSY = "Loading...", ANON_MSG_DEFAULT = "Anonymize",
            ANON_MSG_BUSY = "Anonymizing...";

    protected final JButton loadPluginBtn, sendMsgBtn, anonMsgBtn;
    protected final JTextArea pluginNameFld, sendMsgInput, anonMsgInput;
    protected final JScrollPane sendMsgPane, anonMsgPane;

    public ChatroomState(JButton loadPlugin, JButton sendMsg, JButton anonMsg, 
            JTextArea pluginName, JTextArea sendField, JTextArea anonField,
            JScrollPane sendPane, JScrollPane anonPane) 
    {
        // Buttons
        loadPluginBtn = loadPlugin;
        sendMsgBtn = sendMsg;
        anonMsgBtn = anonMsg;

        // Textareas
        pluginNameFld = pluginName;
        sendMsgInput = sendField;
        anonMsgInput = anonField;

        // Scrollbars
        sendMsgPane = sendPane;
        anonMsgPane = anonPane;
    }

    public ChatroomState(ChatroomState s) {
        loadPluginBtn = s.loadPluginBtn;
        sendMsgBtn = s.sendMsgBtn;
        anonMsgBtn = s.anonMsgBtn;
        pluginNameFld = s.pluginNameFld;
        sendMsgInput = s.sendMsgInput;
        anonMsgInput = s.anonMsgInput;
        sendMsgPane = s.sendMsgPane;
        anonMsgPane = s.anonMsgPane;
    }

    protected abstract void enter();
    protected abstract ChatroomState nextState(Change c);

    public ChatroomState processEvent(Change c) {
        ChatroomState s = nextState(c);
        if (s == null) {
            return this;
        }
        s.enter();
        return s;
    }
}


/*
// Runs a plugin action in a seperate thread
    private void runPluginAction(Runnable task) {
        if (pluginExecutor == null) {
            pluginExecutor = Executors.newSingleThreadExecutor();
        }
        pluginExecutor.submit(task);
    }
    // Display and hide plugin UI
    private void showPluginArea() {
        if (plugin != null) {
            anonPane.setVisible(true);
            anonMsgInput.setVisible(true);
            anon.setVisible(true);
        }
    }
    private void hidePluginArea() {
        anonPane.setVisible(false);
        anonMsgInput.setVisible(false);
        anon.setVisible(false);
    }

    // Protects UI state while plugin is loading
    private void lockPluginLoader() {
        hidePluginArea();
        loadPlugin.setEnabled(false);
        loadPlugin.setText("Loading...");
    }
    private void unlockPluginLoader() {
        showPluginArea();
        loadPlugin.setEnabled(true);
        loadPlugin.setText("Load Plugin");
    }

    // Startup and shutdown functions to guard UI while
    // plugin anonymizes text
    private void startAnonProcess() {
        loadPlugin.setEnabled(false);
        anon.setText("Anonymizing...");
        anon.setEnabled(false);
        anonMsgInput.setEnabled(false);
        
    }
    private void closeAnonProcess() {
        loadPlugin.setEnabled(true);
        anon.setText("Anonymize");
        anon.setEnabled(true);
        anonMsgInput.setEnabled(true);
    }
*/