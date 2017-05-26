package models;

import util.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by Олег on 24.05.2017.
 */
public class RssUrl {
    private URL url;
    private String stringLink;
    private Boolean isValid;
    private Boolean isParsed;
    private String csvLink;
    private Date lastUpdate;
    private short updateRate;
    private static final String XML_EXTENSION = "xml";

    public RssUrl(String stringLink, Date lastUpdate, short updateRate) {
        this.stringLink = stringLink;
        validateLink();
    }

    private void validateLink() {
        try {
            url = new URL(stringLink);
            isValid = true;
        } catch (MalformedURLException exp) {

        }
        this.lastUpdate = lastUpdate;
        this.updateRate = updateRate;
        File testFile = new File(stringLink);
        if (testFile.exists() && !testFile.isDirectory()) {
            int indexOfDot = testFile.getName().lastIndexOf('.');
            String fileExtension = (indexOfDot == -1) ? "" : stringLink.substring(indexOfDot + 1);
            if (fileExtension.equals(XML_EXTENSION))
                isValid = true;
        }
    }

    public URL getUrl() {
        return url;
    }

    public String getStringLink() {
        return stringLink;
    }

    public void setStringLink(String stringLink) {
        this.stringLink = stringLink;
        validateLink();
    }

    public Boolean getValid() {
        return isValid;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public short getUpdateRate() {
        return updateRate;
    }

    public void setUpdateRate(short updateRate) {
        this.updateRate = updateRate;
    }

    public Boolean getParsed() {
        return isParsed;
    }

    public void setParsed(Boolean parsed) {
        isParsed = parsed;
    }

    public String getCsvLink() {
        return csvLink;
    }

    public void setCsvLink(String csvLink) {
        this.csvLink = csvLink;
    }
}
