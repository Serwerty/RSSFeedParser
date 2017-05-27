package controller;

import constants.MenuConstants;
import parser.RSSParser;
import storage.RSSStorage;

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
                    // parseURL(new String[]{"topstories.xml"});
                    parseURL(params);
                    break;

                case MenuConstants.CMD_PRINT:
                    print();
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
        pw.println("Type print to print parsed feed from URL.");
    }

    private static void parseURL(String[] params) {
        RSSParser.get().parse(params);
        RSSStorage.get().saveFile(RSSStorage.get().getRssTitle().split(" ")[0]);
    }

    private static void print() {
        RSSStorage.get().print();
    }
}
