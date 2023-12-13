package xyz.whisperchat.client.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import xyz.whisperchat.client.ui.filter.Filter;
import xyz.whisperchat.client.ui.filter.MessageFilter;
import xyz.whisperchat.client.ui.filter.UsernameFilter;

class FilterUI extends JDialog implements ActionListener {
    public static final String MESSAGE_FILTER = "Message",
        USERNAME_FILTER = "Username";
    private static final String[] FILTER_OPTIONS = { 
        MESSAGE_FILTER, USERNAME_FILTER };

    private JButton addBtn = new JButton("Add filter"),
        cancelBtn = new JButton("Cancel");
    private JComboBox<String> comboBox = new JComboBox<>(FILTER_OPTIONS);
    private JTextField contentInput = new JTextField(20);

    private String savedType = "", savedContent = "";
    
    private static final int INSET_SIZE = 5;
    private Insets insets = new Insets(INSET_SIZE, INSET_SIZE, INSET_SIZE, INSET_SIZE);

    public FilterUI(Window parent) {
        super(parent, "Add filter", Dialog.ModalityType.DOCUMENT_MODAL);
        setLocationRelativeTo(parent);
        initializeComponents();
    }

    private void initializeComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = insets;

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Filter by: "), c);

        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        panel.add(comboBox, c);

        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Filter value: "), c);

        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        panel.add(contentInput, c);

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(addBtn);
        btnPanel.add(cancelBtn);

        addBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(btnPanel, c);

        panel.setBorder(new EmptyBorder(insets));
        add(panel);
        pack();
        setResizable(false);
        setVisible(true);
    }

    public Filter newFilter(Filter prev) {
        if (savedContent.length() > 0) {
            switch (savedType) {
                case MESSAGE_FILTER:
                    return new MessageFilter(prev, savedContent);
                case USERNAME_FILTER:
                    return new UsernameFilter(prev, savedContent);
            }
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(addBtn)) {
            savedType = comboBox.getSelectedItem().toString();
            savedContent = contentInput.getText().trim();
            dispose();
        } else if (e.getSource().equals(cancelBtn)) {
            dispose();
        }
    }

}
