package org.emanueleseminara.clientEmail;

import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;
import javax.mail.*;
import java.awt.event.*;
import java.io.IOException;

public class formClient extends JFrame{
    private JList listEmail;
    private JPanel PanelClient;
    private JTextPane textPaneEmail;
    private JButton buttonNewEmail;
    private JTextField textFieldSearch;
    private JButton buttonSearch;

    private DefaultListModel<String> mailList;
    private Message[] messages = formLogin.getMessages();

    public formClient() {
        this.add(PanelClient);
        this.setSize(900, 500);
        this.setTitle("Client Email");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mailList = new DefaultListModel<>();
        for (int i = 0; i < 10; i++) {
            Message message = messages[i];
            try {
                mailList.add(i, "<html>From: " + message.getFrom()[0] + "<br/>" +
                        "Subject: " + message.getSubject());
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        }

        listEmail.setModel(mailList);

        this.setVisible(true);

        listEmail.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println(listEmail.getSelectedIndex());
                try {
                    textPaneEmail.setText("From: " + messages[listEmail.getSelectedIndex()].getFrom()[0]);
                    textPaneEmail.setText(textPaneEmail.getText() + "\nSubject: " + messages[listEmail.getSelectedIndex()].getSubject());
                    textPaneEmail.setText(textPaneEmail.getText() + "\n\n" + getTextFromMessage(messages[listEmail.getSelectedIndex()]));
                } catch (MessagingException | IOException messagingException) {
                    messagingException.printStackTrace();
                }
            }
        });
        buttonNewEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formNewEmail newEmail = new formNewEmail();
            }
        });
    }

    private String getTextFromMimeMultipart (
            MimeMultipart mimeMultipart) throws IOException, MessagingException {

        int count = mimeMultipart.getCount();
        if (count == 0)
            throw new MessagingException("Multipart with no body parts not supported.");
        boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
        if (multipartAlt)
            return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            result.append(getTextFromBodyPart(bodyPart));
        }
        return result.toString();
    }

    private String getTextFromBodyPart (BodyPart bodyPart) throws IOException, MessagingException {

        String result = "";
        if (bodyPart.isMimeType("text/plain")) {
            result = (String) bodyPart.getContent();
        } else if (bodyPart.isMimeType("text/html")) {
            String html = (String) bodyPart.getContent();
            result = org.jsoup.Jsoup.parse(html).text();
        } else if (bodyPart.getContent() instanceof MimeMultipart){
            result = getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
        }
        return result;
    }

    private String getTextFromMessage(Message message) throws IOException, MessagingException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }
}
