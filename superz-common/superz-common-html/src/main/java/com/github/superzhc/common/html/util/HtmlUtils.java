package com.github.superzhc.common.html.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author superz
 * @create 2022/12/2 17:33
 **/
public class HtmlUtils {
    private static final Logger log = LoggerFactory.getLogger(HtmlUtils.class);

    private HtmlUtils() {
    }

    public static Elements css(Document doc, String cssSelector) {
        return doc.select(cssSelector);
    }

    public static Elements css(Elements elements, String cssSelector) {
        return elements.select(cssSelector);
    }

    public static Elements css(Element element, String cssSelector) {
        return element.select(cssSelector);
    }

    public static Elements xpath(Document doc, String path) {
        return xpath(JXDocument.create(doc), path);
    }

    public static Elements xpath(Element element, String path) {
        return xpath(new Elements(element), path);
    }

    public static Elements xpath(Elements elements, String path) {
        return xpath(JXDocument.create(elements), path);
    }

    public static Elements xpath(JXDocument doc, String path) {
        List<JXNode> lst = doc.selN(path);
        if (null == lst || lst.size() == 0) {
            return null;
        }

        List<Element> eles = new ArrayList<>();
        for (JXNode node : lst) {
            if (!node.isElement()) {
                continue;
            }
            eles.add(node.asElement());
        }

        if (eles.size() == 0) {
            return null;
        }

        return new Elements(eles);
    }

    public static String get(Document doc, String path) {
        return get(JXDocument.create(doc), path);
    }

    public static String get(Element element, String path) {
        return get(new Elements(element), path);
    }

    public static String get(Elements elements, String path) {
        return get(JXDocument.create(elements), path);
    }

    public static String get(JXDocument doc, String path) {
        List<JXNode> lst = doc.selN(path);
        if (null == lst || lst.size() == 0) {
            return null;
        }

        return lst.get(0).asString();
    }

    public static List<String> list(Document doc, String path) {
        return list(JXDocument.create(doc), path);
    }

    public static List<String> list(Element element, String path) {
        return list(new Elements(element), path);
    }

    public static List<String> list(Elements elements, String path) {
        return list(JXDocument.create(elements), path);
    }

    public static List<String> list(JXDocument doc, String path) {
        List<JXNode> lst = doc.selN(path);
        if (null == lst || lst.size() == 0) {
            return null;
        }

        List<String> data = new ArrayList<>(lst.size());
        for (JXNode node : lst) {
            data.add(null == node ? null : node.asString());
        }
        return data;
    }
}
