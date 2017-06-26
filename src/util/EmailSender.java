package util;

import java.util.*;

import constants.EmailConstants;
import constants.EmailType;
import controller.ConfigController;
import controller.StatisticController;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by liman on 24.06.2017.
 */
public class EmailSender {
    //got partially from http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/javamail/javamail.html

    private static EmailSender instance;
    private Properties props;
    private Authenticator authenticator;

    private EmailSender() {
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

    public void sendEmail (EmailType emailType, String text, String[] attachments)
    {
        props.put(EmailConstants.KEY_HOST, EmailConstants.VAL_HOST);
        props.put(EmailConstants.KEY_PORT, EmailConstants.VAL_PORT);
        props.put(EmailConstants.KEY_SSL, true);
        props.put(EmailConstants.KEY_AUTH, true);

        Session session = Session.getInstance(props, authenticator);
        //session.setDebug(debug);

        Message message = new MimeMessage(session);
        try {
            InternetAddress[] address = {new InternetAddress(ConfigController.get().getRecipientEmail())};
            message.setRecipients(Message.RecipientType.TO, address);
            switch (emailType) {
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
            if (attachments.length > 0) {
                // https://www.tutorialspoint.com/javamail_api/javamail_api_send_email_with_attachment.htm
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(text);
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);

                for (String filePath : attachments) {
                    messageBodyPart = new MimeBodyPart();
                    String filename = filePath;
                    DataSource source = new FileDataSource(filename);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(filename);
                    multipart.addBodyPart(messageBodyPart);
                }
                message.setContent(multipart);
            } else {
                message.setText(text);
            }

            Transport.send(message);
            Logger.get().addMessage(String.format("EMAIL INFO: Email of type \"%s\"" +
                    " was sent to %s.", emailType.toString(), address[0].toString()));
        } catch (MessagingException e) {
            Logger.get().addMessage("EMAIL ERROR: Error while sending email.");
            throw new RuntimeException(e);
        }
    }

    public void sendEmail(EmailType emailType, String text)
    {
        String[] attachments = {};
        sendEmail(emailType, text, attachments);
    }

    public void sendEmail(EmailType emailType, String text, String pathToFile){
        String[] attachments = {pathToFile};
        sendEmail(emailType, text, attachments);
    }

    public String createStatisticsBodyText() {
        String statText = String.format("Result statistics:" +
                        "\nTotal news processed (updated and added): %s" +
                        "\nTotal links parsed: %s" +
                        "\nTotal errors occurred: %s" +
                        "\nApplication working time: %s",
                StatisticController.get().getItemsCollected(),
                StatisticController.get().getLinksParsed(),
                StatisticController.get().getErrorsOccurred(),
                StatisticController.get().getWorkingTime());
        String bodyText = String.format("RSS Feed Parser finished.\n\n%s.\n\nLog file is attached." +
                "\n\nThanks for using our application!", statText);
        return bodyText;
    }

    public void sendStatistics()
    {
        Logger.get().exportLog();
        String logfileName = Logger.get().getLogFileName();
        String bodyText = EmailSender.get().createStatisticsBodyText();
        sendEmail(EmailType.Statistics, bodyText, logfileName);
    }

    public void initDailyTimer()
    {
        //https://stackoverflow.com/questions/9375882/how-i-can-run-my-timertask-everyday-2-pm
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, ConfigController.get().getDailyHours());
        date.set(Calendar.MINUTE, ConfigController.get().getDailyMinutes());

        if(date.before(Calendar.getInstance()))
        {
            date.add(Calendar.DAY_OF_WEEK, 1);
        }

        Timer timer = new Timer();
        TimerTask dailyEmailTask = new TimerTask() {
            @Override
            public void run() {
                sendStatistics();
            }
        };

        timer.schedule(dailyEmailTask, date.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
    }
}
