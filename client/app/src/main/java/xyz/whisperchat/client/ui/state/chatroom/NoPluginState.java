package xyz.whisperchat.client.ui.state.chatroom;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class NoPluginState extends ChatroomState {
    public NoPluginState(JButton loadPlugin, JButton sendMsg, JButton anonMsg, 
            JTextArea pluginName, JTextArea sendField, JTextArea anonField,
            JScrollPane sendPane, JScrollPane anonPane) 
    {
        super(loadPlugin, sendMsg, anonMsg, pluginName, 
                sendField, anonField, sendPane, anonPane);
        enter();
    }

    @Override
    protected void enter() {
        loadPluginBtn.setText(ChatroomState.LOAD_PLUGIN_DEFAULT);
        loadPluginBtn.setEnabled(true);
        anonMsgPane.setVisible(false);
        anonMsgInput.setVisible(false);
        anonMsgBtn.setVisible(false);
        pluginNameFld.setVisible(false);
    }

    @Override
    protected ChatroomState nextState(Change c) {
        switch (c) {
            case START_LOAD_PLUGIN:
                return new LoadingPluginState(this);
            default:
                return null;
        }
    }
}
