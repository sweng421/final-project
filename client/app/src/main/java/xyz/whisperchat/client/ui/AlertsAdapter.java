package xyz.whisperchat.client.ui;

import javax.swing.*;
import java.awt.*;

public class AlertsAdapter extends Thread
{
    private String userName;
    private MessageView msgView;
    private JCheckBox listener;
    private int flag = 1;

    public AlertsAdapter(String user, MessageView msgView) {
        userName = user;
        this.msgView = msgView;
    }
    public boolean checkMentions(String userName)
    {
        return (this.userName.equals(userName));
    }
    public void setAlertListener(JCheckBox listener) {this.listener = listener;}

    public void setMsgView(MessageView msgView) {this.msgView = msgView;}

    public void setUserName(String userName) {this.userName = userName;}

    public void run()
    {
        //If the thread was not just started.
        //If it did not find a mention prior to subscription
        //Set the flag to enable alerts
        if((msgView.checkMessageAlert(this))) {flag = -1;}
        else{flag = -1;}

        while(listener.isSelected()){
            synchronized (this) {
                if (msgView.checkMessageAlert(this)) {
                    if(flag != 1){sendNotificationAlert();}
                }
            }
        }
    }
    private void sendNotificationAlert() {
        Toolkit.getDefaultToolkit().beep();
    }
}
