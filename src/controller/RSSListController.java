package controller;

import models.RssUrl;
import util.Logger;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Олег on 21.06.2017.
 */
public class RSSListController {
    private static RSSListController instance;

    private RSSListController() {
        rssArrayList = new ArrayList<>();
    }

    public static RSSListController get() {
        if (instance == null)
            instance = new RSSListController();
        return instance;
    }

    private ArrayList<RssUrl> rssArrayList;

    public void addToList(RssUrl rssUrl) {
            int idInList = ScheduleController.get().addToSchedule(rssUrl, rssUrl.getUpdateRate());
            rssUrl.setIdInTaskList(idInList);
            rssArrayList.add(rssUrl);
            Logger.get().addMessage("Rss Link added to the Task Scheduler" + idInList);
    }

    public void printList(PrintWriter pw) {
        int id =0;
        if(!rssArrayList.isEmpty()) {
            for (RssUrl rssUrl : rssArrayList) {
                pw.println(id + ": " + rssUrl.getUrl() != null ? rssUrl.getUrl() : rssUrl.getStringLink() + " " + rssUrl.getUpdateRate());
                id++;
            }
        }
        else
        {
            pw.println("RSS list is empty.");
        }
    }

    public void editList(RssUrl rssUrl, int id){
        try {
            ScheduleController.get().deleteAt(rssArrayList.get(id).getIdInTaskList());
            int idInList = ScheduleController.get().addToSchedule(rssUrl, rssUrl.getUpdateRate());
            rssUrl.setIdInTaskList(idInList);
            rssArrayList.set(id, rssUrl);
            Logger.get().addMessage("Rss Link added to the Task Scheduler" + rssUrl.getUrl()!=null?rssUrl.getUrl().toString():rssUrl.getStringLink());
        }
        catch (IndexOutOfBoundsException e) {
            Logger.get().addMessage("Error: index out of bounds");
        }
    }

    public void deletelistAt(int id) {
        try {
            ScheduleController.get().deleteAt(rssArrayList.get(id).getIdInTaskList());
            rssArrayList.remove(id);
        }
        catch (IndexOutOfBoundsException e) {
            Logger.get().addMessage("Error: index out of bounds");
        }
    }
}
