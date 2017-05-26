package parser;

/**
 * Created by Олег on 21.05.2017.
 */
public class HTMLParser {
    private static HTMLParser instance;

    private HTMLParser() {

    }

    public static HTMLParser get() {
        if (instance == null)
            instance = new HTMLParser();
        return instance;
    }
}
