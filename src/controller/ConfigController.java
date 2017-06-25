package controller;

import constants.EmailConstants;
import util.EmailSender;
import util.TextFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Олег on 21.06.2017.
 */
public class ConfigController {
    private static ConfigController instance;
    private boolean showLogByLine = true;
    private String recipientEmail = EmailConstants.EMAIL_DEFAULT_RECIPIENT;
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    private short periodOfTime = 15;
    private ConfigController() {
    }

    public static ConfigController get() {
        if (instance == null)
            instance = new ConfigController();
        return instance;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
    private int hours = EmailConstants.DAILY_TIMER_HOURS;
    private int minutes = EmailConstants.DAILY_TIMER_MINUTES;

    public short getPeriodOfTime() {
        return periodOfTime;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public boolean isShowLogByLine() {
        return showLogByLine;
    }

    public int getDailyHours() { return  hours;}
    public int getDailyMinutes() { return  minutes;}

    public void loadConfig() {
        try {
            File file = new File("config/config.dat");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readLine = "";
            while ((readLine = br.readLine()) != null) {
                String[] words = readLine.split(":");
                if ("showLogByLine".equals(words[0])) showLogByLine = Boolean.valueOf(words[1]);
                if ("recipientEmail".equals(words[0])) recipientEmail = words[1];
                if ("timeUnit".equals(words[0])) {
                    if ("SECONDS".equals(words[1]))
                        timeUnit = TimeUnit.SECONDS;
                    if ("MINUTES".equals(words[1]))
                        timeUnit = TimeUnit.MINUTES;
                    if ("HOURS".equals(words[1]))
                        timeUnit = TimeUnit.HOURS;
                    if ("DAYS".equals(words[1]))
                        timeUnit = TimeUnit.DAYS;
                }
                if ("periodOfTime".equals(words[0])) periodOfTime = Short.valueOf(words[1]);
                if("dailyHours".equals(words[0])) hours = Integer.valueOf(words[1]);
                if("dailyMinutes".equals(words[0])) minutes = Integer.valueOf(words[1]);
            }
        } catch (IOException e) {
            util.Logger.get().addMessage("error while reading config file");
            StatisticController.get().incrementErrorsOccurredField();
        }
    }


}
