package main.server.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailHandler {

    private static final String hostEmail = "library.app.iut@gmail.com";
    private static final String password = "#libraryApp20";

    private MailHandler(){}

    public static void sendMail(String receiverEmail, String emailContent){
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        //Set TLS encryption enabled
        properties.put("mail.smtp.starttls.enable", "true");
        //Set SSL encryption method
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        //Set SMTP host
        properties.put("mail.smtp.host", "smtp.gmail.com");
        //Set smtp port
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(hostEmail, password);
            }
        });

        Message message = prepareMessage(session, receiverEmail, emailContent);

        try {
            assert message != null;
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static Message prepareMessage(Session session, String recipient, String emailContent) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MailHandler.hostEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Your document " + emailContent + " is available!");
            String htmlCode = "<h3> Hello it's you favorite library !</h3> <br/> " +
                    "<h5>Your document " + emailContent +" is now available. You can come to reserve or borrow.</h5>";
            message.setContent(htmlCode, "text/html");
            return message;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
