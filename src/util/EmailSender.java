package util;

import java.util.Date;
import java.util.Properties;

import constants.EmailConstants;
import constants.EmailType;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * Created by liman on 24.06.2017.
 */
public class EmailSender {

    private static EmailSender instance;
    private Properties props;
    private Authenticator authenticator;

    private EmailSender(){
        props = new Properties();
        authenticator = new Authenticator() {
            private PasswordAuthentication pa = new PasswordAuthentication(EmailConstants.VAL_USERNAME, EmailConstants.VAL_PASSWORD);
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return pa;
            }
        };
    }

    public static EmailSender get() {
        if (instance == null)
            instance = new EmailSender();
        return instance;
    }

        public void SendEmail(EmailType emailType, String text){
            props.put(EmailConstants.KEY_HOST, EmailConstants.VAL_HOST);
            props.put(EmailConstants.KEY_PORT, EmailConstants.VAL_PORT);
            props.put(EmailConstants.KEY_SSL, true);
            props.put(EmailConstants.KEY_AUTH, true);

            Session session = Session.getInstance(props, authenticator);
            //session.setDebug(debug); // to log??

            Message message = new MimeMessage(session);
            try {
                InternetAddress[] address = {new InternetAddress(EmailConstants.EMAIL_DEFAULT_RECIPIENT)};
                message.setRecipients(Message.RecipientType.TO, address);
                switch (emailType){
                    case Error:
                        message.setSubject(EmailConstants.ERROR_SUBJECT);
                        break;
                    case Statistics:
                        message.setSubject(EmailConstants.STATISTICS_SUBJECT);
                        break;
                    default:
                        message.setSubject(EmailConstants.DEFAULT_SUBJECT);
                        break;
                }
                message.setSentDate(new Date());
                message.setText(text);
                Transport.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }


}
