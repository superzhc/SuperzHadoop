package com.github.superzhc.data.report;

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
 * @create 2022/8/16 23:31
 */
public class News199IT {
    private static final String UA_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36";

    /**
     * 报告
     *
     * @return
     */
    public static List<Map<String, Object>> report() {
        return category("report");
    }

    public static List<Map<String,Object>> _5g(){
        return category("emerging/5g");
    }

    public static List<Map<String,Object>> ai(){
        return category("emerging/%e4%ba%ba%e5%b7%a5%e6%99%ba%e8%83%bd");
    }

    public static List<Map<String,Object>> ml(){
        return category("emerging/%e4%ba%ba%e5%b7%a5%e6%99%ba%e8%83%bd/%e6%9c%ba%e5%99%a8%e5%ad%a6%e4%b9%a0");
    }

    public static List<Map<String,Object>> knowledgeDomains(){
        return category("emerging/%e4%ba%ba%e5%b7%a5%e6%99%ba%e8%83%bd/knowledge-domains");
    }

    /**
     * 医疗健康
     *
     * @return
     */
    public static List<Map<String, Object>> healthTech() {
        return category("emerging/health-tech");
    }

    public static List<Map<String,Object>> shangyebangong(){
        return category("emerging/shangyebangong");
    }

    public static List<Map<String, Object>> newEnergy() {
        return category("emerging/新能源");
    }

    /**
     * 新基建
     *
     * @return
     */
    public static List<Map<String, Object>> infrastructure() {
        return category("emerging/xinjijian");
    }

    public static List<Map<String,Object>> internetfinance(){
        return category("fintech/internetfinance");
    }

    public static List<Map<String, Object>> iot() {
        return category("emerging/物联网");
    }

    public static List<Map<String, Object>> industry() {
        return category("emerging/工业4-0");
    }

    public static List<Map<String, Object>> dataMing() {
        return category("dataindustry/data-mining");
    }

    public static List<Map<String, Object>> bigdata() {
        return tag("大数据");
    }

    public static List<Map<String, Object>> data() {
        return tag("数据早报");
    }

    public static List<Map<String, Object>> category(String path) {
        String url = String.format("http://www.199it.com/archives/category/%s", path);
        return execute(url);
    }

    public static List<Map<String, Object>> tag(String path) {
        String url = String.format("http://www.199it.com/archives/tag/%s", path);
        return execute(url);
    }

    private static List<Map<String, Object>> execute(String url) {
        String html = HttpRequest.get(url).userAgent(UA_CHROME).body();
        System.out.println(html);
        Document document = Jsoup.parse(html);

        List<Map<String, Object>> dataRows = new ArrayList<>();

        Elements articles = document.select("article.entry-list");
        for (Element article : articles) {
            String id=article.attr("id");
            Element ele = article.selectFirst("a[title]");
            String title = ele.attr("title");
            String link = ele.attr("href");
            String pubDate = article.selectFirst("time").attr("datetime");

            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("id",id);
            dataRow.put("title", title);
            dataRow.put("link", link);
            dataRow.put("pubDate", pubDate);
            dataRows.add(dataRow);
        }

        return dataRows;
    }

    public static void main(String[] args) {
        System.out.println(MapUtils.print(ai()));
    }
}
