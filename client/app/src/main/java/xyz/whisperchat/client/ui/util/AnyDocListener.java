package xyz.whisperchat.client.ui.util;

import java.util.function.Consumer;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AnyDocListener implements DocumentListener {
    private Consumer<DocumentEvent> action;
    public AnyDocListener(Consumer<DocumentEvent> r) {
        action = r;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        action.accept(e);
    }
    @Override
    public void removeUpdate(DocumentEvent e) {
        action.accept(e);
    }
    @Override
    public void changedUpdate(DocumentEvent e) {
        action.accept(e);
    }
}
