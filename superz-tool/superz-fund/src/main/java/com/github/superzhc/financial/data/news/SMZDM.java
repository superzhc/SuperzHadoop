package com.github.superzhc.financial.data.news;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.tablesaw.utils.TableUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tech.tablesaw.api.Table;

import java.util.*;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * @author superz
 * @create 2022/8/15 19:58
 */
public class SMZDM {
    public static Table userArticles(String uid) {
        String url = String.format("https://zhiyou.smzdm.com/member/%s/article/", uid);

        String html = HttpRequest.get(url).userAgent(UA_CHROME).body();
        Document document = Jsoup.parse(html);
        // String title = document.selectFirst(".info-stuff-nickname").text();

        List<Map<String, Object>> dataRows = new ArrayList<>();

        Elements articles = document.select(".pandect-content-stuff");
        for (Element article : articles) {
            Element ele = article.selectFirst(".pandect-content-title a");
            String articleTitle = ele.text();
            String articleLink = ele.attr("href");
            String articlePubDate = article.selectFirst(".pandect-content-time").text();

//            String articleDetailHtml = HttpRequest.get(articleLink).userAgent(UA_CHROME).body();
//            Document articleDetailDocument = Jsoup.parse(articleDetailHtml);
//            String articleContent = articleDetailDocument.selectFirst(".m-contant article").html();

            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", articleTitle);
//            dataRow.put("content", articleContent);
            dataRow.put("pubdate", articlePubDate);
            dataRow.put("link", articleLink);
            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table userBaoLiao(String uid) {
        String url = String.format("https://zhiyou.smzdm.com/member/%s/baoliao/", uid);

        String html = HttpRequest.get(url).userAgent(UA_CHROME).body();
        Document document = Jsoup.parse(html);
        // String title = document.selectFirst(".info-stuff-nickname").text();

        Elements eles = document.select(".pandect-content-stuff");
        for (Element ele : eles) {
            Element e = ele.selectFirst(".pandect-content-title a");
            String articleTitle = e.text();
            String articleLink = e.attr("href");
            String articlePubDate = ele.selectFirst(".pandect-content-time").text();

            String contentHtml = HttpRequest.get(articleLink).userAgent(UA_CHROME).body();
            Document contentDocument = Jsoup.parse(contentHtml);
            String articleContent = contentDocument.selectFirst("article.txt-detail").html();
        }

        Table table = null;
        return table;
    }

    public static Table haowen() {
        return haowen("all");
    }

    /**
     * @param period all,1,7,30,365
     * @return
     */
    public static Table haowen(String period) {
        String url = String.format("https://post.smzdm.com/hot_%s", period);

        String html = HttpRequest.get(url).userAgent(UA_CHROME).body();
        Document document = Jsoup.parse(html);
        // String title = document.selectFirst("li.filter-tab.active").text();
        // System.out.println(title);

        List<Map<String, Object>> dataRows = new ArrayList<>();

        Elements articles = document.select("li.feed-row-wide");
        for (Element article : articles) {
            String articleTitle = article.selectFirst("h5.z-feed-title a").text();
            String articleLink = article.selectFirst("h5.z-feed-title a").attr("href");
            String articlePublishDate = article.selectFirst("span.z-publish-time").text();

            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", articleTitle);
            dataRow.put("link", articleLink);
            dataRow.put("pubdate", articlePublishDate);
            // System.out.println(map);

            String contentHtml = HttpRequest.get(articleLink).userAgent(UA_CHROME).body();
            Document contentDocument = Jsoup.parse(contentHtml);
            Element content = contentDocument.selectFirst("#articleId");
            content.select(".item-name").remove();
            content.select(".recommend-tab").remove();
            String description = content.html();
            dataRow.put("description", description);
            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table search(String keyword) {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.shopping.SMZDM.search(keyword);

        Table table = TableUtils.buildByMap(dataRows);
        return table;

    }

    public static void main(String[] args) {
        Table table = search("自行车");

        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
