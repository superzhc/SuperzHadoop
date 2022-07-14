package com.github.superzhc.spider;

import com.github.superzhc.common.http.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.seimicrawler.xpath.JXDocument;

import static com.github.superzhc.common.HttpConstant.UA;

/**
 * @author superz
 * @create 2022/7/11 11:11
 **/
public class SMain {
    public static void main(String[] args) {
        String url = "http://www.caijing.com.cn/original/";

        String result = HttpRequest.get(url).userAgent(UA).body();
        Document doc= Jsoup.parse(result);
        JXDocument xDoc = JXDocument.create(doc);

        Elements elements=doc.select(".wzbt a ");
        for(Element element:elements){
            System.out.println(element.attr("href"));
        }
    }
}
