package models;

/**
 * Created by Олег on 21.05.2017.
 */
public class Item {
    public Item() {

    }

    private String title;
    private String pubDate;
    private String author;
    private String lastUpdateDate;
    private String description;
    private String category;
    private String link;

    public String toString() {

        String convertedValue = "\"";
        convertedValue += title != null ? title : "";
        convertedValue += "\"\t\"";
        convertedValue += pubDate != null ? pubDate : "";
        convertedValue += "\"\t\"";
        convertedValue += author != null ? author : "";
        convertedValue += "\"\t\"";
        convertedValue += lastUpdateDate != null ? lastUpdateDate : "";
        convertedValue += "\"\t\"";
        convertedValue += description != null ? description : "";
        convertedValue += "\"\t\"";
        convertedValue += category != null ? category : "";
        convertedValue += "\"\t\"";
        convertedValue += link != null ? link : "";
        convertedValue += "\"";

          /*    convertedValue =   "\"" + title!=null?title:"" + "\",\"" + pubDate!=null?pubDate:"" + "\",\"" + author!=null?author:"" + "\",\"" +
                lastUpdateDate!=null?author:"" + "\",\"" + description!=null?description:"" + "\",\"" + category!=null?category:"" + "\",\"" +
                link!=null?link:"" + "\"";*/

        return convertedValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
