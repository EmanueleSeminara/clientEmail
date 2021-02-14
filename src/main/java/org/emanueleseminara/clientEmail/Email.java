package org.emanueleseminara.clientEmail;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Email {

    private final Map<String, Properties> service = new HashMap<>();


    public Email() {
        this.setService();
    }

    private Properties propertiesGmail() {
        Properties properties = new Properties();

        properties.put("mail.pop3.host", "pop.gmail.com");
        properties.put("mail.pop3.port", 995);
        properties.put("mail.pop3.starttls.enable", "true");
        properties.put("mail.store.protocol", "pop3s");
        properties.put("mail.pop3.socketFactory", 995);
        properties.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        return properties;

    }

    private void setService() {
        this.service.put("Gmail", this.propertiesGmail());
    }

    private Properties searchProperties(String name) {
        return service.get(name);
    }

    public void addService(String name, Properties properties) {
        service.put(name, properties);
    }

    public Message[] loadEmail(String properties, String storeType, String username, String password) throws MessagingException, IOException {
        Properties props = searchProperties(properties);
        Session emailSession = Session.getDefaultInstance(props);
        Store emailStore =  emailSession.getStore(storeType);
        emailStore.connect(username, password);
        Folder emailFolder = emailStore.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);
        return emailFolder.getMessages();

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

    public void sendEmail(String properties, String from, String password, String to, String sub, String msg) throws MessagingException {
        Properties props = searchProperties(properties);
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(from, password);

            }

        });

        MimeMessage message = new MimeMessage(session);
        message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
        message.setSubject(sub);
        message.setText(msg);
        Transport.send(message);
    }

    public void readEmail(Message[] messages) throws MessagingException, IOException {
        for (int i = 0; i < messages.length; i++) {
            Message message = messages[i];
            System.out.println("---------------------------------");
            System.out.println("Email Number " + (i + 1));
            System.out.println("Subject: " + message.getSubject());
            System.out.println("From: " + message.getFrom()[0]);
            //System.out.println("Text: " + message.getContent().getClass());
            System.out.println("Text: " + getTextFromMessage(message));
        }
    }
}
