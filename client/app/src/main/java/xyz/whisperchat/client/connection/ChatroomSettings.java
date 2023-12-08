package xyz.whisperchat.client.connection;

public class ChatroomSettings {
    private String chatPath = null;
    private int maxMsgLen = -1, maxUsrLen = -1;
    private boolean requiresPassword = false;
    
    public String getChatPath() {
        return chatPath;
    }
    public void setChatPath(String p) {
        chatPath = p;
    }

    public int getMaxMsgLen() {
        return maxMsgLen;
    }
    public void setMaxMsgLen(int l) {
        maxMsgLen = l;
    }

    public int getMaxUsrLen() {
        return maxUsrLen;
    }
    public void setMaxUsrLen(int u) {
        maxUsrLen = u;
    }

    public boolean getRequiresPassword() {
        return requiresPassword;
    }
    public void setRequiresPassword(boolean b) {
        requiresPassword = b;
    }
}
