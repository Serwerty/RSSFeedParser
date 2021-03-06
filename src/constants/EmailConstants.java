package constants;

/**
 * Created by liman on 25.06.2017.
 */
public class EmailConstants {
    public static final int VAL_PORT = 587;
    public static final String VAL_HOST = "smtp.gmail.com";
    public static final String VAL_USERNAME = "rssfeedparser.idsm@gmail.com";
    public static final String VAL_PASSWORD = "rssfeedparser2016";

    public static final String KEY_PORT = "mail.smtp.port";
    public static final String KEY_HOST = "mail.smtp.host";
    public static final String KEY_AUTH = "mail.smtp.auth";
    public static final String KEY_SSL = "mail.smtp.starttls.enable";

    public static final String ERROR_SUBJECT = "RSSFeedParser failed";
    public static final String STATISTICS_SUBJECT = "RSSFeedParser Statistics";
    public static final String DEFAULT_SUBJECT = "RSSFeedParser Info";

    public static final String EMAIL_DEFAULT_RECIPIENT = "oleg.dovzhenko2012@gmail.com";

    public static final int DAILY_TIMER_HOURS = 12;
    public static final int DAILY_TIMER_MINUTES = 0;
}
