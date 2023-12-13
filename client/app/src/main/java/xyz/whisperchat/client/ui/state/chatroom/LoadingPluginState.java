package xyz.whisperchat.client.ui.state.chatroom;

public class LoadingPluginState extends ChatroomState {
    public LoadingPluginState(ChatroomState s) {
        super(s);
    }

    @Override
    protected void enter() {
        anonMsgInput.setEnabled(false);
        anonMsgBtn.setEnabled(false);
        loadPluginBtn.setEnabled(false);
        loadPluginBtn.setText(ChatroomState.LOAD_PLUGIN_BUSY);
    }

    @Override
    protected ChatroomState nextState(Change c) {
        switch (c) {
            case STOP_LOAD_PLUGIN:
                return new PluginReadyState(this);
            default:
                return null;
        }
    }
}
