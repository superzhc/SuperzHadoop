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
 * @author superz
 * @create 2022/8/18 15:00
 **/
public class JieMian {
    public static Table news() {
        List<Map<String, Object>> dataRows = new ArrayList<>();

        for (int i = 1; i < 5; i++) {
            String url = String.format("https://www.jiemian.com/lists/%d.html", i);

            String html = HttpRequest.get(url).userAgent(UA_CHROME).body();
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

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table = news();
        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
