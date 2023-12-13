package xyz.whisperchat.client.ui;

import javax.swing.JComponent;
import javax.swing.JFrame;

import xyz.whisperchat.client.ui.util.FixFont;

public abstract class ApplicationFrame extends JFrame {
    public static final float DEFAULT_FONT_SIZE = 17.0f;

    protected int state;
    protected float fixedFontSize = DEFAULT_FONT_SIZE;

    public abstract void trySwitchState();
    protected void fixFont(JComponent c) {
        FixFont.fix(c, fixedFontSize);
    }

    public ApplicationFrame(String title) {
        super(title);
    }

    public ApplicationFrame(String title, float fontSize) {
        super(title);
        fixedFontSize = fontSize;
    }
}
