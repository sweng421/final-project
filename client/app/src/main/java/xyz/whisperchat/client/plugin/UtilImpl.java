package xyz.whisperchat.client.plugin;

import java.io.File;
import java.awt.Component;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class UtilImpl implements ExtensionUtil {
    private int charLimit;
    private Component parent = null;
    public UtilImpl(int chlimit) {
        charLimit = chlimit;
    }

    public Component getParent() {
        return parent;
    }
    public void setParent(Component c) {
        parent = c;
    }

    @Override
    public File fileDialog(String description, String extension) {
        JFileChooser filePicker = new JFileChooser();
        filePicker.setFileSelectionMode(JFileChooser.FILES_ONLY);
        filePicker.setFileFilter(new FileNameExtensionFilter(description, extension));

        if (filePicker.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            return filePicker.getSelectedFile();
        }
        return null;
    }

    @Override
    public int getCharLimit() {
        return charLimit;
    }
}
