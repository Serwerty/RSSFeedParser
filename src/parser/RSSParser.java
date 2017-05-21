package parser;

/**
 * Created by Олег on 21.05.2017.
 */
public class RSSParser {
    private static RSSParser instance;
    private RSSParser() {

    }

    public static RSSParser get() {
        if (instance == null)
            instance = new RSSParser();
        return instance;
    }
}
