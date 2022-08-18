package com.github.superzhc.financial.data.other;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.tablesaw.utils.TableUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tech.tablesaw.api.Table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * @author superz
 * @create 2022/8/18 15:42
 **/
public class SoBooks {
    private static final String ROOT_URL = "https://www.sobooks.net";

    public static Table date() {
        return date(LocalDate.now().minusDays(1));
    }

    public static Table date(LocalDate date) {
        String path = String.format("books/date/%s", date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        return visit(path);
    }

    public static Table category(String c) {
        return visit(c);
    }

    public static Table tag(String t) {
        return visit(String.format("books/tag/%s", t));
    }

    private static Table visit(String path) {
        String url = String.format("%s/%s", ROOT_URL, path);

        String html = HttpRequest.get(url).userAgent(UA_CHROME).body();
        Document document = Jsoup.parse(html);

        List<Map<String, Object>> dataRows = new ArrayList<>();

        Elements eles = document.select(".card-item h3 a");
        for (Element ele : eles) {
            Element e = ele;
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", e.text());
            dataRow.put("link", e.attr("href"));

            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table = tag("编程");

        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
