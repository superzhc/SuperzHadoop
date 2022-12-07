package com.github.superzhc.data.other;

import com.github.superzhc.common.html.util.HtmlUtils;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.utils.MapUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/12/7 10:04
 **/
public class ZhiYaDaily {
    private static final String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36";

    public static List<Map<String, Object>> daily() {
        return daily(LocalDate.now());
    }

    public static List<Map<String, Object>> daily(LocalDate date) {
        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return daily(dateStr);
    }

    public static List<Map<String, Object>> daily(String dateStr) {

        String url = String.format("https://tool.lu/article/report/%s/", dateStr);

        String html = HttpRequest.get(url).userAgent(UA).body();
        Document doc = Jsoup.parse(html);

        List<Map<String, Object>> dataRows = new ArrayList<>();

        Elements elements = HtmlUtils.xpath(doc, "//div[@data-type=\"info\"]/..");
        if (null != elements) {
            for (Element element : elements) {
                Element basicEle = element.selectFirst("h3.ct-heading>a.ct-link");
                String title = basicEle.text();
                String link = basicEle.attr("href");
                String content = element.selectFirst("div.ct-note>div.ct-note-content").html();

                Map<String, Object> dataRow = new LinkedHashMap<>();
                dataRow.put("title", title);
                dataRow.put("content", content);
                dataRow.put("link", link);
                dataRows.add(dataRow);
            }
        }
        return dataRows;
    }

    public static void main(String[] args) {
        List<Map<String, Object>> dataRows = new ArrayList<>();

        LocalDate start = LocalDate.of(2022, 11, 1);
        LocalDate end = LocalDate.now();
        while (end.isAfter(start)) {
            dataRows.addAll(daily(start));
            start = start.plusDays(1);
        }

        System.out.println(MapUtils.print(dataRows));
    }
}
