package controller;

import util.TextFilter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * Created by Олег on 21.06.2017.
 */
public class ConfigController {
    private static ConfigController instance;

    private ConfigController() {
    }

    public static ConfigController get() {
        if (instance == null)
            instance = new ConfigController();
        return instance;
    }

    private boolean showLogByLine = true;
    private String recipientEmail = "kateryna.liman@gmail.com";
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public boolean isShowLogByLine() {
        return showLogByLine;
    }

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
                    if("SECONDS".equals(words[1]))
                        timeUnit = TimeUnit.SECONDS;
                    if("MINUTES".equals(words[1]))
                        timeUnit = TimeUnit.MINUTES;
                    if("HOURS".equals(words[1]))
                        timeUnit = TimeUnit.HOURS;
                    if("DAYS".equals(words[1]))
                        timeUnit = TimeUnit.DAYS;
                }
            }
        } catch (IOException e) {
            util.Logger.get().addMessage("error while reading config file");
            StatisticController.get().incrementErrorsOccurredField();
        }
    }


}
