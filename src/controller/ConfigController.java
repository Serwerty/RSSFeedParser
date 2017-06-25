package controller;

import util.TextFilter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Олег on 21.06.2017.
 */
public class ConfigController {
    private static ConfigController instance;

    private ConfigController() {
    }

    public static ConfigController get() {
        if (instance == null)
            instance = new ConfigController();
        return instance;
    }

    private boolean showLogByLine = true;

    public boolean isShowLogByLine() {
        return showLogByLine;
    }

    public void loadConfig() {
        try {
            File file = new File("config/config.dat");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readLine = "";
            while ((readLine = br.readLine()) != null) {
                String[] words = readLine.split(":");
                if ("showLogByLine".equals(words[0])) showLogByLine = Boolean.valueOf(words[1]);
            }
        } catch (IOException e) {
            util.Logger.get().addMessage("error while reading config file");
        }


    }

    public void saveConfig(){
        try {
            File file = new File("config/config.dat");
            Path pathToFile = Paths.get("config/config.dat");
            Files.createDirectories(pathToFile.getParent());
            Files.createFile(pathToFile);
            PrintWriter writer = new PrintWriter(file);
            writer.println("showLogByLine:" + showLogByLine);
            writer.close();
        } catch (IOException e) {
            util.Logger.get().addMessage("error while saving config file");
        }
    }

}
