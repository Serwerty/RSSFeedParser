package parser;


import constants.RSSTags;
import models.Item;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import storage.RSSStorage;
import util.Logger;

import javax.xml.parsers.*;
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

    public void parse(String[] url)
    {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            File xmlFile = new File(url[0]);
            InputStream inputStream= new FileInputStream(xmlFile);
            InputStreamReader inputReader = new InputStreamReader(inputStream,"UTF-8");
            InputSource inputSource = new InputSource(inputReader);
            inputSource.setEncoding("UTF-8");

            Document doc = builder.parse(inputStream);

            NodeList title = doc.getElementsByTagName(RSSTags.TITLE_TAG);
            RSSStorage.get().setRssTitle(getCharacterDataFromElement((Element)title.item(0)));
            NodeList link = doc.getElementsByTagName(RSSTags.LINK_TAG);
            RSSStorage.get().setRssLink(getCharacterDataFromElement((Element)title.item(0)));


            NodeList nodes = doc.getElementsByTagName(RSSTags.ITEM_TAG);
            RSSStorage.get().setItemsList(getItems(nodes));


        }
        catch(ParserConfigurationException exp)
        {
            Logger.get().addMessage("Error while configuring of the parser.");
        }
        catch(IOException exp )
        {
            Logger.get().addMessage("Error while parsing url:" + url[0]);
        }
        catch(SAXException exp )
        {
            Logger.get().addMessage("SAX Error while parsing url:" + url[0]);
        }
    }


    private String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof org.w3c.dom.CharacterData) {
            org.w3c.dom.CharacterData cd = (org.w3c.dom.CharacterData) child;
            if (cd != null) {
                return cd.getData();
            }
        }
        return "";
    }

    private List<Item> getItems(NodeList itemNodes) {

        List<Item> items = new ArrayList<>();
        for (int i = 0; i < itemNodes.getLength(); i++) {
            Item item = new Item();
            Element element = (Element)itemNodes.item(i);

            item.setTitle(getCharacterDataFromTag(element, RSSTags.TITLE_TAG));
            item.setAuthor(getCharacterDataFromTag(element, RSSTags.AUTHOR_TAG));
            item.setCategory(getCharacterDataFromTag(element, RSSTags.CATEGORY_TAG));
            item.setDescription(getCharacterDataFromTag(element, RSSTags.DESCRIPTION_TAG));
            item.setPubDate(getCharacterDataFromTag(element, RSSTags.PUBDATE_TAG));
            item.setLastUpdateDate(getCharacterDataFromTag(element, RSSTags.LASTBUILDDATE_TAG));
            item.setLink(getCharacterDataFromTag(element, RSSTags.LINK_TAG));

            items.add(item);
        }
        return items;
    }

    private String getCharacterDataFromTag(Element element, String tag) {
        NodeList currentTag = element.getElementsByTagName(tag);
        if (currentTag != null) {
            for (int i = 0; i < currentTag.getLength(); i++) {
                Element line = (Element)currentTag.item(i);
                if (line != null) {
                    return getCharacterDataFromElement(line);
                }
            }
        }
        return null;
    }
}
