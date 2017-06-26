package parser;

import models.Item;
import models.RssUrl;
import util.Logger;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Олег on 26.06.2017.
 */
public class CSVParser {
    private static CSVParser instance;

    private CSVParser() {
    }

    public static CSVParser get() {
        if (instance == null)
            instance = new CSVParser();
        return instance;
    }

    public ArrayList<Item> getItems(RssUrl rssURL) {
        ArrayList<Item> items = new ArrayList<>();
        if (rssURL.getCsvFilePath() != null) {
            try {
                File file = new File(rssURL.getCsvFilePath());
                BufferedReader br = new BufferedReader(new FileReader(file));
                String readLine = "";
                while ((readLine = br.readLine()) != null) {
                    String[] words = readLine.split("\t");
                    Item item = new Item();
                    item.setTitle(words[0].substring(1, words[0].length() - 1));
                    item.setPubDate(words[1].substring(1, words[1].length() - 1));
                    item.setAuthor(words[2].substring(1, words[2].length() - 1));
                    item.setLastUpdateDate(words[3].substring(1, words[3].length() - 1));
                    item.setDescription(words[4].substring(1, words[4].length() - 1));
                    item.setCategory(words[5].substring(1, words[5].length() - 1));
                    item.setLink(words[6].substring(1, words[6].length() - 1));
                    items.add(item);
                }
                br.close();
            } catch (FileNotFoundException e) {
                Logger.get().addMessage("Error: file not found.");
            } catch (IOException e) {
                Logger.get().addMessage("Error: cant parse csv file.");
            } catch (IndexOutOfBoundsException e) {
                Logger.get().addMessage("Error: wrong csv format.");
            }
        }
        return items;
    }
}
