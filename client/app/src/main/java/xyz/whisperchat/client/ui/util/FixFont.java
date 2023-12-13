package xyz.whisperchat.client.ui.util;

import javax.swing.JComponent;

public class FixFont {
    public static void fix(JComponent c, float fixedFontSize) {
        c.setFont(c.getFont().deriveFont(fixedFontSize));
    } 
}
