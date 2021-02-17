package org.emanueleseminara.clientEmail;

import javax.swing.*;

public class formNewEmail extends JFrame {
    private JTextField textField1;
    private JTextField textField2;
    private JTextArea textArea1;
    private JButton sendButton;
    private JButton deleteButton;

    public formNewEmail() {
        //this.add(panelLogin);
        this.setSize(350, 200);
        this.setTitle("New Email");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
}
