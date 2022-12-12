package com.github.superzhc.data.life;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.utils.MapUtils;
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
 * @create 2022/9/20 16:45
 **/
public class BenDiBao {
    public static List<Map<String, String>> nanjing() {
        return news("nj");
    }

    public static List<Map<String, String>> news(String city) {
        String url = String.format("http://%s.bendibao.com", city);

        String html = HttpRequest.get(url).body();

        Document doc = Jsoup.parse(html);

        List<Map<String, String>> dataRows = new ArrayList<>();

        Elements elements = doc.select("ul.focus-news li");
        for (Element element : elements) {
            Element item = element.selectFirst("a");

            Map<String, String> dataRow = new LinkedHashMap<>();
            dataRow.put("title", item.text());
            dataRow.put("link", item.attr("href"));

            dataRows.add(dataRow);
        }

        return dataRows;
    }

    public static void main(String[] args) {
        String city = "nj";
        System.out.println(MapUtils.print(news(city)));
    }
}
