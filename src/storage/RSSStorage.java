package storage;

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

    public void saveFile() {
        Date dateNow = new Date();

        SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormatter = new SimpleDateFormat("dd");


        String yearDate = yearFormatter.format(dateNow);
        String monthDate = monthFormatter.format(dateNow);
        String dayDate = dayFormatter.format(dateNow);
        if (rssTitle == null)
            rssTitle = "Untitled";
        try {
            File file = new File(String.format("csvStorage/%s/%s/%s/%s.csv", yearDate, monthDate, dayDate,
                    TextFilter.get().prepareToSave(rssTitle)));
            Path pathToFile = Paths.get(String.format("csvStorage/%s/%s/%s/%s.csv", yearDate, monthDate, dayDate,
                    TextFilter.get().prepareToSave(rssTitle)));
            Files.createDirectories(pathToFile.getParent());
            try {
                boolean result = Files.deleteIfExists(file.toPath());
                Logger.get().addMessage("Replacing file " + TextFilter.get().prepareToSave(rssTitle));
            }
            catch (IOException e) {
            }
            Files.createFile(pathToFile);
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            for (Item item : itemsList) {
                writer.println(item.toString());
            }
            Logger.get().addMessage("File saved " + TextFilter.get().prepareToSave(rssTitle));
            writer.close();
        } catch (IOException e) {
            Logger.get().addMessage("Error while saving the file " + TextFilter.get().prepareToSave(rssTitle));
        }
    }

    public void print() {
        for (Item item : itemsList) {
            System.out.println(item.toString());
        }
    }
}
