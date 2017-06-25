package controller;

import util.TextFilter;

/**
 * Created by Олег on 25.06.2017.
 */
public class StatisticController {
    private static StatisticController instance;

    private StatisticController() {
    }

    public static StatisticController get() {
        if (instance == null)
            instance = new StatisticController();
        return instance;
    }

    private int itemsCollected;
    private int linksParsed;
    private int errorsOccurred;
    private long startTime;
    private long currentTime;

    public int getItemsCollected() {
        return itemsCollected;
    }

    public int getLinksParsed() {
        return linksParsed;
    }

    public int getErrorsOccurred() {
        return errorsOccurred;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getCurrentTime() {
        currentTime = System.nanoTime();
        return currentTime;
    }

    public String getWorkingTime(){
        long curTime = getCurrentTime();
        long timeInSec = (curTime - startTime)/1000000000;
        return TextFilter.get().prepareTime(timeInSec);
    }

    public void Init(){
        startTime = System.nanoTime();
        itemsCollected = 0;
        linksParsed = 0;
        errorsOccurred = 0;
    }

    public void incrementItemsCollectedField(){
        itemsCollected ++;
    }

    public void incrementLinkParsedField(){
        linksParsed ++;
    }

    public void incrementErrorsOccurredField(){
        errorsOccurred ++;
    }
}
