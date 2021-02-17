package org.emanueleseminara.clientEmail;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import javax.mail.*;

public class formLogin extends JFrame {
    private JTextField textFieldEmail;
    private JButton ButtonLogin;
    private JComboBox comboBoxService;
    private JPanel panelLogin;
    private JLabel LabelEmail;
    private JLabel LabelPassword;
    private JLabel LabelService;
    private JPasswordField passwordField;

    private String username;
    private String password;
    private final Email email = new Email();
    private static Message[] messages = null;

    public formLogin() {
        this.add(panelLogin);
        this.setSize(350, 200);
        this.setTitle("Login");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        comboBoxService.addItem("Gmail");
        ButtonLogin.setEnabled(false);
        this.setVisible(true);



        ButtonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                username = textFieldEmail.getText();
                password = new String(passwordField.getPassword());
                try {
                    messages = email.loadEmail("Gmail", "pop3", username, password);
                    dispose();
                    JOptionPane.showMessageDialog(null, "LOGIN SUCCESSFUL!", "Login successful", 1);
                    formClient client = new formClient();
                } catch (MessagingException ex) {
                    JOptionPane.showMessageDialog(null, "WRONG EMAIL OR PASSWORD", "Error", 0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        textFieldEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                //System.out.println(e.getExtendedKeyCode()==10);
                if(e.getExtendedKeyCode() == 10 && ButtonLogin.isEnabled()) {
                    ButtonLogin.doClick();
                }
                if(textFieldEmail.getText().trim().contains("@") && !String.valueOf(passwordField.getPassword()).trim().isEmpty()) {
                    ButtonLogin.setEnabled(true);
                }
                else {
                    ButtonLogin.setEnabled(false);
                }
            }
        });
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if(e.getExtendedKeyCode() == 10 && ButtonLogin.isEnabled()) {
                    ButtonLogin.doClick();
                }
                if(textFieldEmail.getText().trim().contains("@") && !String.valueOf(passwordField.getPassword()).trim().isEmpty()) {
                    ButtonLogin.setEnabled(true);
                }
                else {
                    ButtonLogin.setEnabled(false);
                }
            }
        });

    }

    public static Message[] getMessages() {
        return messages;
    }
}
