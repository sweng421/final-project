package xyz.whisperchat.client.ui;

import javax.swing.JComponent;
import javax.swing.JFrame;

public abstract class StatefulFrame extends JFrame {
    public static final float DEFAULT_FONT_SIZE = 17.0f;
    public static final int SIGNED_OUT = 0,
        LOGGED_IN = 1;

    protected int state;
    protected float fixedFontSize = DEFAULT_FONT_SIZE;

    public abstract StatefulFrame trySwitchState();
    protected void fixFont(JComponent c) {
        c.setFont(c.getFont().deriveFont(fixedFontSize));
    }

    public StatefulFrame(String title) {
        super(title);
    }

    public StatefulFrame(String title, float fontSize) {
        super(title);
        fixedFontSize = fontSize;
    }
}
