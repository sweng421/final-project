package xyz.whisperchat.client.ui.state.chatroom;

public class PluginBusyState extends ChatroomState {
    public PluginBusyState(ChatroomState s) {
        super(s);
    }

    @Override
    protected void enter() {
        anonMsgBtn.setEnabled(false);
        anonMsgBtn.setText(ChatroomState.ANON_MSG_BUSY);
        anonMsgInput.setEnabled(false);
        loadPluginBtn.setEnabled(false);
    }

    @Override
    protected ChatroomState nextState(Change c) {
        switch (c) {
            case STOP_USING_PLUGIN:
                return new PluginReadyState(this);
            default:
                return null;
        }
    }
    
}
