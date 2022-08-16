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
 * @create 2022/8/16 23:31
 */
public class News199IT {
    /**
     * 报告
     *
     * @return
     */
    public static Table report() {
        return category("report");
    }

    /**
     * 医疗健康
     *
     * @return
     */
    public static Table healthTech() {
        return category("emerging/health-tech");
    }

    public static Table newEnergy() {
        return category("emerging/新能源");
    }

    /**
     * 新基建
     *
     * @return
     */
    public static Table infrastructure() {
        return category("emerging/xinjijian");
    }

    public static Table iot() {
        return category("emerging/物联网");
    }

    public static Table industry() {
        return category("emerging/工业4-0");
    }

    public static Table dataMing() {
        return category("dataindustry/data-mining");
    }

    public static Table bigdata() {
        return tag("大数据");
    }

    public static Table data() {
        return tag("数据早报");
    }

    public static Table category(String path) {
        String url = String.format("http://www.199it.com/archives/category/%s", path);
        return execute(url);
    }

    public static Table tag(String path) {
        String url = String.format("http://www.199it.com/archives/tag/%s", path);
        return execute(url);
    }

    private static Table execute(String url) {
        String html = HttpRequest.get(url).userAgent(UA_CHROME).body();
        Document document = Jsoup.parse(html);

        List<Map<String, Object>> dataRows = new ArrayList<>();

        Elements articles = document.select("article.entry-list");
        for (Element article : articles) {
            Element ele = article.selectFirst("a[title]");
            String title = ele.attr("title");
            String link = ele.attr("href");
            String pubDate = article.selectFirst("time").attr("datetime");

            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", title);
            dataRow.put("link", link);
            dataRow.put("pubDate", pubDate);
            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) {
        String tag = "数据早报";
        Table table = industry();//tag(tag);
        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
