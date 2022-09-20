package com.github.superzhc.data.news;

import com.github.superzhc.common.http.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/9/20 11:07
 **/
public class News8World {

    public static List<Map<String, String>> realtime() {
        return news("realtime");
    }

    public static List<Map<String, String>> news(String category) {
        String url = String.format("https://www.8world.com/%s", category);

        String result = HttpRequest.get(url).body();

        List<Map<String, String>> dataRows = new ArrayList<>();

        Document doc = Jsoup.parse(result);
        Elements elements = doc.select("div[data-column=\"Two-Third\"] .article-title .article-link");
        for (Element element : elements) {
            Map<String, String> dataRow = new LinkedHashMap<>();
            dataRow.put("title", element.text());
            dataRow.put("link", String.format("https://www.8world.com%s", element.attr("href")));

            dataRows.add(dataRow);
        }

        return dataRows;
    }


    public static void main(String[] args) {
        String category = "realtime";

        System.out.println(news(category));
    }
}
