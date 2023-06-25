package mail;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.PasswordAuthentication;

import java.util.Properties;

/**
 * Email client.
 */
public class Client {

    private final String SMTP_AUTH_USER = System.getenv("SMTP_AUTH_USER");
    private final String SMTP_AUTH_PWD  = System.getenv("SMTP_AUTH_PWD");

    private final Session session;
    private final Transport transport;

    public Client() throws NoSuchProviderException {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");

        String SMTP_HOST_NAME = System.getenv("SMTP_HOST_NAME");
        properties.put("mail.smtp.host", SMTP_HOST_NAME);

        properties.put("mail.smtp.auth", "true");

        String SMTP_PORT = System.getenv("SMTP_PORT");
        properties.put("mail.smtp.port", SMTP_PORT);

        var auth = new SMTPAuthenticator();
        session = Session.getDefaultInstance(properties, auth);

        transport = session.getTransport();
    }

    /**
     * Method to send authentication code.
     * @param receptionist one who receive authentication code
     * @param code given code
     * @throws MessagingException if something went wrong
     */
    public void sendAuthenticationCode(String receptionist, String code) throws MessagingException {
        var msg = new MimeMessage(session);
        msg.setSubject("University Booking Bot authentication code");
        msg.setContent(String.format("Your code is %s", code), "text/plain");
        msg.setFrom(new InternetAddress(SMTP_AUTH_USER));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(receptionist));

        transport.connect();
        transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

    /**
     * Authenticator class
     */
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
        }
    }
}