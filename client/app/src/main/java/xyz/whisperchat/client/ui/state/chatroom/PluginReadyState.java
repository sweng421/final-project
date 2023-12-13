package xyz.whisperchat.client.ui.state.chatroom;

public class PluginReadyState extends ChatroomState {
    public PluginReadyState(ChatroomState s) {
        super(s);
    }

    @Override
    protected void enter() {
        anonMsgPane.setVisible(true);
        anonMsgInput.setVisible(true);
        anonMsgBtn.setVisible(true);
        loadPluginBtn.setEnabled(true);
        loadPluginBtn.setText(ChatroomState.LOAD_PLUGIN_DEFAULT);
        anonMsgBtn.setEnabled(true);
        anonMsgInput.setEnabled(true);
        anonMsgBtn.setText(ChatroomState.ANON_MSG_DEFAULT);
        pluginNameFld.setVisible(true);
    }

    @Override
    protected ChatroomState nextState(Change c) {
        switch (c) {
            case START_USING_PLUGIN:
                return new PluginBusyState(this);
            case START_LOAD_PLUGIN:
                return new LoadingPluginState(this);
            default:
                return null;
        }
    }
}
