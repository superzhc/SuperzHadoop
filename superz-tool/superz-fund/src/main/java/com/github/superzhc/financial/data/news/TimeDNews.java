package com.github.superzhc.financial.data.news;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.tablesaw.utils.TableUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tech.tablesaw.api.Table;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * 1. https://github.com/DIYgod/RSSHub/blob/master/lib/v2/timednews/news.js
 * <p>
 * 注意：要外网访问？？？
 *
 * @author superz
 * @create 2022/8/18 15:24
 **/
public class TimeDNews {
    public static Table news() {
        return visit("cat/1.html");
    }

    private static Table visit(String path) {
        String url = String.format("https://www.timednews.com/topic/%s", path);

        String html = HttpRequest.get(url).userAgent(UA_CHROME).body();
        Document document = Jsoup.parse(html);

        List<Map<String, Object>> dataRows = new ArrayList<>();

        Elements contents = document.select("#content li");
        for (Element content : contents) {
            Element ele = content.selectFirst("a");
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", ele.text());
            dataRow.put("link", ele.attr("href"));

            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table = news();

        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
