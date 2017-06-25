package controller;

import constants.MenuConstants;
import models.RssUrl;
import parser.RSSParser;
import storage.RSSStorage;
import util.Logger;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Олег on 21.05.2017.
 */
public class MainController {
    private static Scanner sc;
    private static PrintWriter pw;

    private static void init() {
        sc = new Scanner(System.in);
        pw = new PrintWriter(System.out, true);
        help(null);
    }

    public static void main(String[] args) {
        String command = null;

        init();

        while (!MenuConstants.CMD_EXIT.equals(command)) {
            String line = sc.nextLine();
            String[] words = line.split(" ");
            String[] params = {};

            if (words.length > 0) {
                command = words[0];
                if (words.length > 1) {
                    params = Arrays.copyOfRange(words, 1, words.length);
                }
            } else {
                continue;
            }

            switch (command) {
                case MenuConstants.CMD_PARSE:
                    parseURL(params);
                    break;

                case MenuConstants.CMD_HELP:
                    help(params);
                    break;

                case MenuConstants.CMD_PRINT:
                    //print();
                    break;

                case  MenuConstants.CMD_VIEW_LOG:
                    viewLog();
                    break;

                case MenuConstants.CMD_EXPORT_LOG:
                    exportLog();
                    break;

                case MenuConstants.CMD_ADD_TO_LIST:
                    addToList(params);
                    break;

                case MenuConstants.CMD_DELETE_FROM_LIST:
                    deleteListAt(params);
                    break;

                case MenuConstants.CMD_EDIT_FROM_LIST:
                    editList(params);
                    break;

                case MenuConstants.CMD_VIEW_LIST:
                    viewList(params);
                    break;

                case MenuConstants.CMD_IMPORT_LIST:
                    importList();
                    break;

                case MenuConstants.CMD_EXPORT_LIST:
                    exportList();
                    break;

                case MenuConstants.CMD_CLEAN_UP_LIST:
                    exportList();
                    break;

                default:
                    break;
            }
        }
    }

    private static void help(String[] params) {
        pw.println("Type help to see commands list.");
        pw.println("Type exit to exit application.");
        pw.println("Type parse URL to parse that URL.");
        //pw.println("Type print to print parsed feed from URL.");
        pw.println("Type view_log to view log.");
        pw.println("Type export_log to export log in a logs folder.");
        pw.println("Type list to view rss list in progress.");
        pw.println("Type add {url} {period} to add rss into list with some period of executing.");
        pw.println("Type edit {id} {url} {period} to edit rss in list with some period of executing.");
        pw.println("Type delete {id} to delete rss from list.");
    }

    private static void parseURL(String[] params) {
        try {
            RssUrl rssUrl = new RssUrl(params[0]);
            if (rssUrl.getValid()) {
                RSSParser.get().parse(rssUrl);
                if (rssUrl.getValid()) {
                    print(rssUrl);
                    rssUrl.getStorage().saveFile();
                }
                else {
                    Logger.get().addMessage("Error: rss is invalid");
                }
            }
            else {
                Logger.get().addMessage("Error: rss is invalid");
            }
        }
       catch (ArrayIndexOutOfBoundsException e){
            Logger.get().addMessage("Error: you also need to specify name");
        }
    }

    private static void viewLog() {
        Logger.get().printLog(pw);
    }

    private static void addToList(String[] params){
        try {
            short period = Short.valueOf(params[1]);
            RssUrl rssUrl = new RssUrl(params[0], period);
            if (rssUrl.getValid()) {
                RSSListController.get().addToList(rssUrl);
            }
            else {
                Logger.get().addMessage("Error: rss is invalid");
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            Logger.get().addMessage("Error: you also need to specify name and period of time");
        }
        catch (NumberFormatException e){
            Logger.get().addMessage("Error: NaN:period");
        }
    }

    private static void viewList(String[] params){
        RSSListController.get().printList(pw);
    }

    private static void editList(String[] params){
        try {
            short period = Short.valueOf(params[2]);
            int id = Integer.valueOf(params[0]);
            RssUrl rssUrl = new RssUrl(params[1], period);
            if (rssUrl.getValid()) {
                RSSListController.get().editList(rssUrl, id);
            }
            else {
                Logger.get().addMessage("Error: rss is invalid");
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            Logger.get().addMessage("Error: you also need to specify id, name and period of time");
        }
        catch (NumberFormatException e){
            Logger.get().addMessage("Error: NaN:period");
        }
    }

    private static void deleteListAt(String[] params){
        try {
            int id = Integer.valueOf(params[0]);
            RSSListController.get().deletelistAt(id);
        }
        catch (ArrayIndexOutOfBoundsException e){
            Logger.get().addMessage("Error: you need to specify id");
        }
    }

    private static void exportList(){
        RSSListController.get().exportList();
    }

    private static void importList(){
        RSSListController.get().cleanUpList();
        RSSListController.get().importList();
    }

    private static void exportLog(){
        Logger.get().exportLog();
    }

    private static void cleanUpList(){ RSSListController.get().cleanUpList();}

    private static void print(RssUrl rssUrl){
        rssUrl.getStorage().print();
    }
}
