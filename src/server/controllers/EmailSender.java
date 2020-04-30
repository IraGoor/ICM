package server.controllers;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// USE IT LIKE: EmailSender.sendEmail("EMAIL","TITLE","CONTENT");

/**
 * Email Sender.
 * send email as ICM mail account.

 */
public class EmailSender {

    public void sendPlainTextEmail(String host, String port,
                                   final String userName, final String password, String toAddress,
                                   String subject, String message) throws AddressException,
            MessagingException {

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // creates a new session with an authenticator


        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };

        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setContent(message, "text/html; charset=utf-8");

        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.saveChanges();
        // set plain text message

        // sends the e-mail
        Transport.send(msg);

    }

    /**
     * Email Sender: sends email from ICM mail account. HTML Friendly.
     * Email sends from: icm.systemg.10@gmail.com
     * Using: SMTP - google
     * TLS: Enabled.
     * @param mailTo  : String - One contact only
     * @param subject : String
     * @param message : String (HTML)
     */
    public static void sendEmail(String mailTo, String subject, String message) {
        // SMTP server information
        String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = "icm.systemg.10@gmail.com";
        String password = "ICM@12345";

        // outgoing message information

        EmailSender mailer = new EmailSender();
        String msgHTML = "<center> <table style=\"height: 323px;\" width=\"426\"> <tbody> <tr style=\"height: 28px;\"> <td style=\"width: 416px; height: 28px;\" bgcolor=\"#264653\"><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"https://i.imgur.com/89JJ9Yj.png\" alt=\"ICM_Logo\" width=\"193\" height=\"70\" /></td> </tr> <tr style=\"height: 77px;\"> <td style=\"width: 416px; height: 77px; text-align: center;\">"
                + message +
                "</td> </tr> </tbody> </table> </center>";
        try {
            mailer.sendPlainTextEmail(host, port, mailFrom, password, mailTo,
                    subject, msgHTML);
            System.out.println("Email sent to: "+ mailTo + "{"+subject+" , " + msgHTML+" }" );
        } catch (Exception ex) {
            System.out.println("Failed to sent email.");
            ex.printStackTrace();
        }
    }
}