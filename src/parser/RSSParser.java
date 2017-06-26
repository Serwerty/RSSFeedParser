package parser;


import constants.EmailType;
import constants.RSSTags;
import controller.RSSListController;
import controller.StatisticController;
import models.Item;
import models.RssUrl;
import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import util.EmailSender;
import util.Logger;
import util.TextFilter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    public void parse(RssUrl rssUrl) {
        if (rssUrl.getValid()) {
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                InputStream inputStream;
                if (rssUrl.getUrl() == null) {
                    File xmlFile = new File(rssUrl.getStringLink());
                    inputStream = new FileInputStream(xmlFile);
                    InputStreamReader inputReader = new InputStreamReader(inputStream, "UTF-8");
                    InputSource inputSource = new InputSource(inputReader);
                    inputSource.setEncoding("UTF-8");
                } else {
                    inputStream = rssUrl.getUrl().openStream();
                }
                Document doc = builder.parse(inputStream);

                NodeList title = doc.getElementsByTagName(RSSTags.TITLE_TAG);
                rssUrl.getStorage().setRssTitle(getCharacterDataFromElement((Element) title.item(0)));

                NodeList rssLink = doc.getElementsByTagName(RSSTags.LINK_TAG);
                rssUrl.getStorage().setRssLink(getCharacterDataFromElement((Element) rssLink.item(0)));

                NodeList nodes = doc.getElementsByTagName(RSSTags.ITEM_TAG);
                rssUrl.getStorage().setItemsList(getItems(nodes, rssUrl));
                StatisticController.get().incrementLinkParsedField();

            } catch (ParserConfigurationException exp) {
                String text = "Error while configuring of the parser.";
                Logger.get().addMessage(text);
                EmailSender.get().sendEmail(EmailType.Error, text);
                StatisticController.get().incrementErrorsOccurredField();
            } catch (IOException exp) {
                String text = "Error while parsing url:" + rssUrl.getStringLink();
                Logger.get().addMessage(text);
                EmailSender.get().sendEmail(EmailType.Error, text);
                StatisticController.get().incrementErrorsOccurredField();
            } catch (SAXException exp) {
                rssUrl.setValid(false);
                String text = "SAX Error while parsing url:" + rssUrl.getStringLink();
                Logger.get().addMessage(text);
                EmailSender.get().sendEmail(EmailType.Error, text);
                StatisticController.get().incrementErrorsOccurredField();
                RSSListController.get().deletelistAt(RSSListController.get().getId(rssUrl));
            }
        } else {
            Logger.get().addMessage("Error: rss is invalid");
            String text = "Error: rss is invalid:" + rssUrl.getStringLink();
            StatisticController.get().incrementErrorsOccurredField();
            EmailSender.get().sendEmail(EmailType.Error, text);
        }
    }


    private List<Item> getItems(NodeList itemNodes, RssUrl rssUrl) {

        List<Item> items = CSVParser.get().getItems(rssUrl);
        for (int i = 0; i < itemNodes.getLength(); i++) {
            Item item = new Item();
            Element element = (Element) itemNodes.item(i);

            item.setTitle(getCharacterDataFromTag(element, RSSTags.TITLE_TAG));
            item.setAuthor(getCharacterDataFromTag(element, RSSTags.AUTHOR_TAG));
            item.setCategory(getCharacterDataFromTag(element, RSSTags.CATEGORY_TAG));
            item.setDescription(getCharacterDataFromTag(element, RSSTags.DESCRIPTION_TAG));
            item.setPubDate(getCharacterDataFromTag(element, RSSTags.PUBDATE_TAG));
            item.setLastUpdateDate(getCharacterDataFromTag(element, RSSTags.LASTBUILDDATE_TAG));
            item.setLink(getCharacterDataFromTag(element, RSSTags.LINK_TAG));

            boolean contains = false;
            for (Item itemToCheck : items) {
                if (itemToCheck.getTitle().equals(item.getTitle())){
                    contains = true;
                    if (!items.contains(item)){
                        items.set(items.indexOf(itemToCheck),item);
                    }
                }
            }
            if (!contains) {
                items.add(item);
                StatisticController.get().incrementItemsCollectedField();
            }
        }
        return items;
    }

    private String getCharacterDataFromTag(Element element, String tag) {
        //TODO: rename currentTag
        NodeList currentTag = element.getElementsByTagName(tag);
        if (currentTag != null) {
            for (int i = 0; i < currentTag.getLength(); i++) {
                Element line = (Element) currentTag.item(i);
                if (line != null) {
                    return getCharacterDataFromElement(line);
                }
            }
        }
        return null;
    }

    private String getCharacterDataFromElement(Element element) {
        Node child = element.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData characterData = (CharacterData) child;
            if (characterData != null) {
                return TextFilter.get().deleteRedundantSymbols(characterData.getData());
            }
        }
        return null;
    }
}
