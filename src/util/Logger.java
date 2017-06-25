package util;



import controller.ConfigController;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import models.Item;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by Олег on 21.05.2017.
 */
public class Logger {
    private static Logger instance;
    private static PrintWriter writer;

    private Logger() {
        logList = new ArrayList<>();
        logListProperty = new SimpleListProperty<>();
    }

    public static Logger get() {
        if (instance == null)
            instance = new Logger();
        return instance;
    }

    public static void init(PrintWriter pw){
        writer = pw;
    }

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private ListProperty<String> logListProperty;
    private List<String> logList;

    public ListProperty<String> getList() {
        return logListProperty;
    }

    public void addMessage(String messageToLog) {
        String lineToAdd = LocalDateTime.now() + ": " + messageToLog;
        logList.add(lineToAdd);
        logListProperty.set(FXCollections.observableArrayList(logList));
        if (ConfigController.get().isShowLogByLine()){
            writer.println(lineToAdd);
        }

    }

    public void printLog(PrintWriter pw) {
        for (String line: logList) {
            pw.println(line);
        }
    }

    public void exportLog(){
        Date dateNow = new Date();

        SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormatter = new SimpleDateFormat("dd");


        String yearDate = yearFormatter.format(dateNow);
        String monthDate = monthFormatter.format(dateNow);
        String dayDate = dayFormatter.format(dateNow);

        try {
            File file = new File(String.format("logs/%s/%s/%s/log.log", yearDate, monthDate, dayDate));
            Path pathToFile = Paths.get(String.format("logs/%s/%s/%s/log.log", yearDate, monthDate, dayDate));
            Files.createDirectories(pathToFile.getParent());
            try {
                boolean result = Files.deleteIfExists(file.toPath());
                Logger.get().addMessage("Replacing log file " + "log");
            }
            catch (IOException e) {
            }
            Files.createFile(pathToFile);
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            for (String line : logList) {
                writer.println(line);
            }
            Logger.get().addMessage("Log saved");
            writer.close();
        } catch (IOException e) {
            Logger.get().addMessage("Error while saving log");
        }
    }
}
