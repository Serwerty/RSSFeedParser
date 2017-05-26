package storage;

import models.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Олег on 21.05.2017.
 */
public class RSSStorage {
    private static RSSStorage instance;

    private RSSStorage() {
        initStorage();
    }

    public static RSSStorage get() {
        if (instance == null)
            instance = new RSSStorage();
        return instance;
    }

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

    public void saveFile(String fileName) {

    }

    public void print() {
        for (Item item : itemsList) {
            System.out.println(item.toString());
        }
    }
}
