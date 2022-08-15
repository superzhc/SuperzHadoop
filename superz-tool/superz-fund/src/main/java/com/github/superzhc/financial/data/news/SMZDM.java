package com.github.superzhc.financial.data.news;

import com.github.superzhc.common.http.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * @author superz
 * @create 2022/8/15 19:58
 */
public class SMZDM {
    public static void main(String[] args) {
        // 可选值：all,1,7,30,365
        String type="all";
        String url = String.format("https://post.smzdm.com/hot_%s",type);

        String html = HttpRequest.get(url).userAgent(UA_CHROME).body();
        Document document = Jsoup.parse(html);
        String title = document.selectFirst("li.filter-tab.active").text();
        System.out.println(title);

        Elements articles = document.select("li.feed-row-wide");
        for (Element article : articles) {
            String articleTitle = article.selectFirst("h5.z-feed-title a").text();
            String articleLink = article.selectFirst("h5.z-feed-title a").attr("href");
            String articlePublishDate = article.selectFirst("span.z-publish-time").text();

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("title", articleTitle);
            map.put("link", articleLink);
            map.put("pubdate", articlePublishDate);
            // System.out.println(map);

            String contentHtml=HttpRequest.get(articleLink).userAgent(UA_CHROME).body();
            Document contentDocument=Jsoup.parse(contentHtml);
            Element content=contentDocument.selectFirst("#articleId");
            String description =content.removeClass("item-name").removeClass("recommend-tab").html();
            System.out.println(description);
        }
    }
}
