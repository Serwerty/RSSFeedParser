package util;

/**
 * Created by User on 026 26.05.17.
 */
public class TextFilter {
    private TextFilter() {
    }

    private static TextFilter instance;

    public static TextFilter get() {
        if (instance == null) {
            instance = new TextFilter();
        }
        return instance;
    }

    public String deleteRedundantSymbols(String input) {
        input = input.replaceAll("\\s+", " ");
        return input;
    }

    public String prepareToSave(String input){
        input = input.replaceAll("[^a-zA-Z0-9.-]", "_");
        input =  input.replaceAll("_+", "_");
        return input;
    }
}
