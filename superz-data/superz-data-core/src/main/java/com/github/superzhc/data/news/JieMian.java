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
 * @create 2022/12/2 18:06
 **/
public class JieMian {
    public static List<Map<String, Object>> news() {
        List<Map<String, Object>> dataRows = new ArrayList<>();

        for (int i = 1; i < 5; i++) {
            String url = String.format("https://www.jiemian.com/lists/%d.html", i);

            String html = HttpRequest.get(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36").body();
            Document document = Jsoup.parse(html);

            Elements data = document.select(".card-list__content, .columns-right-center__newsflash-content");

            for (Element item : data) {
                Element ele = item.selectFirst("a");
                Map<String, Object> dataRow = new LinkedHashMap<>();
                dataRow.put("title", ele.text());
                dataRow.put("link", ele.attr("href"));

                dataRows.add(dataRow);
            }
        }

        return dataRows;
    }
}
