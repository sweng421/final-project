package xyz.whisperchat.client.ui;

import javax.swing.JFrame;

public abstract class StatefulFrame extends JFrame {
    protected int state;
    public static final int SIGNED_OUT = 0,
        LOGGED_IN = 1;
    public abstract StatefulFrame trySwitchState();

    public StatefulFrame() {
        super();
    }
}
