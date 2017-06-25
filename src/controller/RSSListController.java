package controller;

import models.RssUrl;
import util.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Олег on 21.06.2017.
 */
public class RSSListController {
    private static RSSListController instance;
    private ArrayList<RssUrl> rssArrayList;

    private RSSListController() {
        rssArrayList = new ArrayList<>();
    }

    public static RSSListController get() {
        if (instance == null)
            instance = new RSSListController();
        return instance;
    }

    public void addToList(RssUrl rssUrl) {
        if (!rssArrayList.contains(rssUrl) && !containsLink(rssUrl)) {
            int idInList = ScheduleController.get().addToSchedule(rssUrl, rssUrl.getUpdateRate());
            rssUrl.setIdInTaskList(idInList);
            rssArrayList.add(rssUrl);
            Logger.get().addMessage("Rss Link added to the Task Scheduler " + idInList);
        } else {
            Logger.get().addMessage("Rss Link is already in list");
        }
    }

    private boolean containsLink(RssUrl rssUrl) {
        boolean contains = false;
        for (RssUrl rssUrlInList : rssArrayList) {
            if (rssUrlInList.getStringLink().equals(rssUrl.getStringLink()))
                contains = true;
        }
        return contains;
    }

    public void printList(PrintWriter pw) {
        int id = 0;
        if (!rssArrayList.isEmpty()) {
            for (RssUrl rssUrl : rssArrayList) {
                String outLine = Integer.toString(id) + " ";
                outLine += rssUrl.getUrl() != null ? rssUrl.getUrl() : rssUrl.getStringLink();
                outLine += " " + rssUrl.getUpdateRate();
                pw.println(outLine);
                id++;
            }
        } else {
            pw.println("RSS list is empty.");
        }
    }

    public void editList(RssUrl rssUrl, int id) {
        try {
            ScheduleController.get().deleteAt(rssArrayList.get(id).getIdInTaskList());
            int idInList = ScheduleController.get().addToSchedule(rssUrl, rssUrl.getUpdateRate());
            rssUrl.setIdInTaskList(idInList);
            rssArrayList.set(id, rssUrl);
            String ternaryLine = rssUrl.getUrl() != null ? rssUrl.getUrl().toString() : rssUrl.getStringLink();
            Logger.get().addMessage("Rss Link was edited " + ternaryLine);
        } catch (IndexOutOfBoundsException e) {
            Logger.get().addMessage("Error: index out of bounds");
            StatisticController.get().incrementErrorsOccurredField();
        }
    }

    public void deletelistAt(int id) {
        try {
            ScheduleController.get().deleteAt(rssArrayList.get(id).getIdInTaskList());
            rssArrayList.remove(id);
        } catch (IndexOutOfBoundsException e) {
            Logger.get().addMessage("Error: index out of bounds");
            StatisticController.get().incrementErrorsOccurredField();
        }
    }

    public void cleanUpList() {
        for (RssUrl rssUrl : rssArrayList) {
            ScheduleController.get().deleteAt(rssUrl.getIdInTaskList());
        }
        rssArrayList = new ArrayList<>();
        Logger.get().addMessage("Rss list is cleaned up");
    }

    public int getId(RssUrl rssUrl) {
        return rssArrayList.indexOf(rssUrl);
    }

    public void exportList() {
        try {
            File file = new File("rssList/list.dat");
            Path pathToFile = Paths.get("rssList/list.dat");
            Files.createDirectories(pathToFile.getParent());

            try {

                boolean result = Files.deleteIfExists(file.toPath());
                if (result) Logger.get().addMessage("Replacing rss list file");

            } catch (IOException e) {
            }

            Files.createFile(pathToFile);
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            for (RssUrl rssUrl : rssArrayList) {
                String outLine = rssUrl.getStringLink() + " " + rssUrl.getUpdateRate();
                writer.println(outLine);
            }
            Logger.get().addMessage("Rss list is exported");
            writer.close();
        } catch (IOException e) {
            util.Logger.get().addMessage("error while saving rss list file");
            StatisticController.get().incrementErrorsOccurredField();
        }
    }

    public void importList() {
        try {
            File file = new File("rssList/list.dat");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readLine = "";
            while ((readLine = br.readLine()) != null) {
                String[] words = readLine.split(" ");
                try {
                    short period = Short.valueOf(words[1]);
                    RssUrl rssUrl = new RssUrl(words[0], period);
                    if (rssUrl.getValid()) {
                        RSSListController.get().addToList(rssUrl);
                    } else {
                        Logger.get().addMessage("Error: rss is invalid {" + rssUrl.getStringLink() + "}");
                        StatisticController.get().incrementErrorsOccurredField();
                    }
                    Logger.get().addMessage("Rss list is imported");
                } catch (ArrayIndexOutOfBoundsException e) {
                    Logger.get().addMessage("Error: bad format, link and period should be always separated by ':' ");
                    StatisticController.get().incrementErrorsOccurredField();
                } catch (NumberFormatException e) {
                    Logger.get().addMessage("Error: NaN:period");
                    StatisticController.get().incrementErrorsOccurredField();
                }
            }
        } catch (IOException e) {
            util.Logger.get().addMessage("error while reading rss list file");
            StatisticController.get().incrementErrorsOccurredField();
        }
    }
}
