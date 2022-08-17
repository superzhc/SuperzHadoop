package com.github.superzhc.financial.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tech.tablesaw.api.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * @author superz
 * @create 2022/8/17 11:43
 **/
public class CCTV {
    // 每周质量保障
    public static Table mzzlbg() {
        String url = "https://api.cntv.cn/video/videolistById";

        Map<String, Object> params = new HashMap<>();
        params.put("serviceId", "cbox");
        params.put("vsid", "C10354");
        params.put("em", "01");
        params.put("p", 1);
        params.put("n", 50);

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode data = JsonUtils.json(result, "video");

        List<Map<String, Object>> dataRows = new ArrayList<>();

        for (JsonNode item : data) {
            String title = JsonUtils.string(item, "t");
            String desc = JsonUtils.string(item, "desc");
            String link = String.format("https://vdn.apps.cntv.cn/api/getHttpVideoInfo.do?pid=%s", JsonUtils.string(item, "vid"));
            Long pubDate = JsonUtils.aLong(item, "ptime");

            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", title);
            dataRow.put("description", desc);
            dataRow.put("link", link);
            dataRow.put("pubDate", pubDate);
            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table xwlb() {
        return xwlb(null);
    }

    public static Table xwlb(LocalDate date) {
        if (null == date || !date.isBefore(LocalDate.now())) {
            LocalDateTime now = LocalDateTime.now();
            if (now.getHour() < 20) {
                date = now.minusDays(1).toLocalDate();
            }
        }

        String url = String.format("https://tv.cctv.com/lm/xwlb/day/%s.shtml", date.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        String html = HttpRequest.get(url).userAgent(UA_CHROME).body();
        Document document = Jsoup.parse(html);

        Elements eles = document.select("body li");

        List<Map<String, Object>> dataRows = new ArrayList<>();

        for (Element ele : eles) {
            Element e = ele.selectFirst("a");
            String title = e.attr("title");
            String link = e.attr("href");
            String content = ele.html();

            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", title);
            dataRow.put("link", link);
            dataRow.put("content", content);
            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table = xwlb();
        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
