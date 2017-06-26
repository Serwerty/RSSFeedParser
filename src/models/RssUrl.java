package models;

import controller.StatisticController;
import parser.RSSParser;
import storage.RSSStorage;
import util.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Олег on 24.05.2017.
 */
public class RssUrl implements Runnable {
    private static final String XML_EXTENSION = "xml";
    private URL url;
    private String stringLink;
    private Boolean isValid;
    private short updateRate;
    private RSSStorage storage;
    private int idInTaskList;
    private String csvFilePath;

    public RssUrl(String stringLink, short updateRate) {
        storage = new RSSStorage();
        this.stringLink = stringLink;
        this.updateRate = updateRate;
        validateLink();
    }

    public RssUrl(String stringLink) {
        storage = new RSSStorage();
        this.stringLink = stringLink;
        validateLink();
    }

    public int getIdInTaskList() {
        return idInTaskList;
    }

    public void setIdInTaskList(int idInTaskList) {
        this.idInTaskList = idInTaskList;
    }

    private void validateLink() {
        try {
            url = new URL(stringLink);
            isValid = true;
        } catch (MalformedURLException exp) {
            isValid = false;
        }
        File testFile = new File(stringLink);
        if (testFile.exists() && !testFile.isDirectory()) {
            int indexOfDot = testFile.getName().lastIndexOf('.');
            String fileExtension = (indexOfDot == -1) ? "" : stringLink.substring(indexOfDot + 1);
            if (fileExtension.equals(XML_EXTENSION))
                isValid = true;
        }
    }

    public RSSStorage getStorage() {
        return storage;
    }

    public void setStorage(RSSStorage storage) {
        this.storage = storage;
    }

    public URL getUrl() {
        return url;
    }

    public String getStringLink() {
        return stringLink;
    }

    public void setStringLink(String stringLink) {
        this.stringLink = stringLink;
        validateLink();
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public short getUpdateRate() {
        return updateRate;
    }

    public void setUpdateRate(short updateRate) {
        this.updateRate = updateRate;
    }

    public String getCsvFilePath() {
        return csvFilePath;
    }

    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    @Override
    public void run() {
        if (isValid) {
            RSSParser.get().parse(this);
        }
        if (isValid) {
            storage.saveFile();
            csvFilePath = storage.getCsvFilePath();
        } else {
            Logger.get().addMessage("Error: rss is invalid");
            StatisticController.get().incrementErrorsOccurredField();
        }
    }
}
