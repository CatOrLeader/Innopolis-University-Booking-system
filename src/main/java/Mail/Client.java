package Mail;

import Utilities.Services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Email client.
 */
public class Client {

    private final String SMTP_AUTH_USER = Services.getEnv("SMTP_AUTH_USER");
    private final String SMTP_AUTH_PWD = Services.getEnv("SMTP_AUTH_PWD");

    private final Session session;
    private final Transport transport;

    public Client() throws NoSuchProviderException {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");

        String SMTP_HOST_NAME = Services.getEnv("SMTP_HOST_NAME");
        properties.put("mail.smtp.host", SMTP_HOST_NAME);

        properties.put("mail.smtp.auth", "true");

        String SMTP_PORT = Services.getEnv("SMTP_PORT");
        properties.put("mail.smtp.port", SMTP_PORT);

        var auth = new SMTPAuthenticator();
        session = Session.getDefaultInstance(properties, auth);

        transport = session.getTransport();
    }

    /**
     * Method to send authentication code.
     *
     * @param receptionist one's email who receive authentication code
     * @param code         given code
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