package controller;

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

    public int getItemsCollected() {
        return itemsCollected;
    }

    public int getLinksParsed() {
        return linksParsed;
    }

    public int getErrorsOccurred() {
        return errorsOccurred;
    }

    public void Init(){
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
