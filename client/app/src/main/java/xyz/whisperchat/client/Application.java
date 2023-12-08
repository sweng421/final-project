package xyz.whisperchat.client;

import javax.swing.UIManager;
import xyz.whisperchat.client.ui.LoginFrame;

public class Application {
    public static void main(String[] args) throws Exception {
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoginFrame f = new LoginFrame();
        f.setVisible(true);
    }
}
