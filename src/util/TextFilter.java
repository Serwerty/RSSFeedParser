package util;

/**
 * Created by User on 026 26.05.17.
 */
public class TextFilter {
    private static TextFilter instance;

    private TextFilter() {
    }

    public static TextFilter get() {
        if (instance == null) {
            instance = new TextFilter();
        }
        return instance;
    }

    public String deleteRedundantSymbols(String input) {
        input = deleteHTMLTags(input);
        input = input.replaceAll("\\s+", " ");
        return input;
    }

    public String deleteHTMLTags(String input) {
        input = input.replaceAll("(?i)<td[^>]*>", "");
        input = input.replaceAll("(?i)<a[^>]*>", "");
        input = input.replaceAll("(?i)<script[^>]*>", "");
        input = input.replaceAll("(?i)<div[^>]*>", "");
        input = input.replaceAll("(?i)<img[^>]*>", "");
        input = input.replaceAll("</td>", "");
        input = input.replaceAll("</a>", "");
        input = input.replaceAll("</script>", "");
        input = input.replaceAll("</div>", "");
        input = input.replaceAll("</img>", "");
        input = input.replaceAll("\"", "");
        input = input.replaceAll("href", "");
        return input;
    }

    public String prepareToSave(String input) {
        input = input.replaceAll("[^a-zA-Z0-9.-]", "_");
        input = input.replaceAll("_+", "_");
        return input;
    }

    public String prepareTime(Long input) {
        long timeInSec = input;
        long hours = timeInSec / 60 / 60;
        long minutes = (timeInSec - hours * 60 * 60) / 60;
        return Long.toString(hours) + "h " +
                Long.toString(minutes) + "m " +
                Long.toString(timeInSec - hours * 60 * 60 - minutes * 60) + "s";
    }
}
