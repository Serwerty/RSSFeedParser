package storage;

import controller.StatisticController;
import models.Item;
import sun.rmi.runtime.Log;
import util.Logger;
import util.TextFilter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Олег on 21.05.2017.
 */
public class RSSStorage {
   // private static RSSStorage instance;
    private static final int ONE_MB = 1048576; //1024*1024
    private static final int FILE_MAX_SIZE_MB = 10;
    private static final String DEFAULT_TITLE = "Untitled";

    public RSSStorage() {
        initStorage();
    }

   /* public static RSSStorage get() {
        if (instance == null)
            instance = new RSSStorage();
        return instance;
    }
*/
    private List<Item> itemsList;
    private String rssTitle;
    private String rssSource;
    private String rssLink;

    public void cleanUp() {

    }

    private void initStorage() {
        rssTitle = null;
        rssSource = null;
        rssLink = null;
        itemsList = new ArrayList<>();
    }

    public List<Item> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<Item> itemsList) {
        this.itemsList = itemsList;
    }

    public String getRssTitle() {
        return rssTitle;
    }

    public void setRssTitle(String rssTitle) {
        this.rssTitle = rssTitle;
    }

    public String getRssSource() {
        return rssSource;
    }

    public void setRssSource(String rssSource) {
        this.rssSource = rssSource;
    }

    public String getRssLink() {
        return rssLink;
    }

    public void setRssLink(String rssLink) {
        this.rssLink = rssLink;
    }

    private File createNewFile()
    {
        File file = null;

        if (rssTitle == null)
        {
            rssTitle = DEFAULT_TITLE;
        }

        String fileName = createFileName(rssTitle);

        try {
            file = new File(fileName);
            Path pathToFile = Paths.get(fileName);
            Files.createDirectories(pathToFile.getParent());
            boolean result = Files.deleteIfExists(file.toPath());
            if (result) Logger.get().addMessage("Replacing rss list file");
            Files.createFile(pathToFile);
        } catch (IOException e) {
            Logger.get().addMessage("Error while saving the file " + TextFilter.get().prepareToSave(rssTitle));
            StatisticController.get().incrementErrorsOccurredField();
        }
        return file;
    }

    private String createFileName(String title)
    {
        Date dateNow = new Date();

        SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormatter = new SimpleDateFormat("dd");

        String yearDate = yearFormatter.format(dateNow);
        String monthDate = monthFormatter.format(dateNow);
        String dayDate = dayFormatter.format(dateNow);

        String fileName = String.format("csvStorage/%s/%s/%s/%s.csv", yearDate, monthDate, dayDate,
                TextFilter.get().prepareToSave(title));

        return fileName;
    }

    private Boolean isFileSizeValid(File file)
    {
        Boolean result = (file.length() / ONE_MB) < FILE_MAX_SIZE_MB;
        return result;
    }

    private void renameFile(File file, int fileNumber)
    {
        String fileName = file.getPath();
        String fileExtension = fileName.substring(fileName.length()-4);
        String newFileName = fileName.replace(fileExtension, "_" + Integer.toString(fileNumber) + fileExtension);

        File newFile = new File(newFileName);
        file.renameTo(newFile);
    }

    public void saveFile() {

        File file = createNewFile();
        int fileNumber = 1;
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            for (Item item : itemsList) {
                if(!isFileSizeValid(file))
                {
                    writer.close();
                    renameFile(file, fileNumber);
                    fileNumber++;
                    file = createNewFile();
                    writer = new PrintWriter(file, "UTF-8");
                }
                writer.println(item.toString());
                writer.flush();
            }
            Logger.get().addMessage("File saved " + TextFilter.get().prepareToSave(rssTitle));
            writer.close();
        } catch (IOException e) {
            Logger.get().addMessage("Error while saving the file " + TextFilter.get().prepareToSave(rssTitle));
            StatisticController.get().incrementErrorsOccurredField();
        }
    }

    public void print() {
        for (Item item : itemsList) {
            System.out.println(item.toString());
        }
    }
}
