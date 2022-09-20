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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2022/9/20 9:18
 **/
public class News50Forum {
    public static List<Map<String, String>> articles() {
        String url = "http://www.50forum.org.cn/home/article/index/category/zhuanjia.html";

        String result = HttpRequest.get(url).body();

        Pattern pattern = Pattern.compile("^(.+)\\[(.+)\\](.+)$");
        List<Map<String, String>> dataRows = new ArrayList<>();

        Document doc = Jsoup.parse(result);
        Elements elements = doc.select("div.container div.list_list.mtop10 ul li");
        for (Element e1 : elements) {
            Map<String, String> dataRow = new LinkedHashMap<>();
            Element e2 = e1.selectFirst("a");

            String text = e2.text();
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                dataRow.put("title", matcher.group(1));
                dataRow.put("author", matcher.group(2));
            } else {
                dataRow.put("title", text);
            }

            String link = e2.attr("href");
            dataRow.put("link", link);
            dataRows.add(dataRow);
        }
        return dataRows;
    }

    public static void main(String[] args) {

    }
}
