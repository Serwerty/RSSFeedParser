package util;


import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Олег on 21.05.2017.
 */
public class Logger {
    private static Logger instance;
    private Logger() {
        logList = new ArrayList<>();
        logListProperty = new SimpleListProperty<>();
    }

    public static Logger get() {
        if (instance == null)
            instance = new Logger();
        return instance;
    }

    private static DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private ListProperty<String> logListProperty;
    private List<String> logList;

    public ListProperty<String> getList() {
        return logListProperty;
    }

    public void addMessage(String messageToLog) {
        logList.add(LocalDateTime.now() + ": " + messageToLog);
        logListProperty.set(FXCollections.observableArrayList(logList));
    }
}
