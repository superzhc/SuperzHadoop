package com.github.superzhc.data.shopping;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.html.HtmlRequest;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.MapUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author superz
 * @create 2022/10/25 21:58
 **/
public class SMZDM {
    private static final Logger log = LoggerFactory.getLogger(SMZDM.class);

    private static final String UA_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36";

    public enum RankType {

        好价品类榜_全部("pinlei", "11"),
        好价品类榜_食品生鲜("pinlei", "12"),//
        好价品类榜_电脑数码("pinlei", "13"),//
        好价品类榜_运动户外("pinlei", "14"),//
        好价品类榜_家用电器("pinlei", "15"),//
        好价品类榜_白菜("pinlei", "17"),//
        好价品类榜_服饰鞋包("pinlei", "74"),//
        好价品类榜_日用百货("pinlei", "75"),//
        好价电商榜_券活动("dianshang", "24"),//
        好价电商榜_京东("dianshang", "23"),//
        好价电商榜_天猫("dianshang", "25"),//
        好价电商榜_亚马逊中国("dianshang", "26"),//
        好价电商榜_国美在线("dianshang", "27"),//
        好价电商榜_苏宁易购("dianshang", "28"),//
        好价电商榜_网易("dianshang", "29"),//
        好价电商榜_考拉海购("dianshang", "29"),//
        好价电商榜_西集网("dianshang", "30"),//
        好价电商榜_美国亚马逊("dianshang", "31"),//
        好价电商榜_日本亚马逊("dianshang", "32"),//
        好价电商榜_ebay("dianshang", "33"),//
        海淘_TOP榜_全部("haitao", "39"),//
        海淘_TOP榜_海外直邮("haitao", "34"),//
        海淘_TOP榜_美国榜("haitao", "35"),//
        海淘_TOP榜_欧洲榜("haitao", "36"),//
        海淘_TOP榜_澳新榜("haitao", "37"),//
        海淘_TOP榜_亚洲榜("haitao", "38"),//
        海淘_TOP榜_晒物榜("haitao", "hsw"),//
        好文排行榜_原创("haowen", "yc"),//
        好文排行榜_资讯("haowen", "zx"),//
        好物排行榜_新晋榜("haowu", "hwall"),//
        好物排行榜_消费众测("haowu", "zc")//
//        ,好物排行榜_新锐品牌("haowu", "nb"),
//        好物排行榜_好物榜单("haowu", "hw")
        ;

        private String type;
        private String id;

        RankType(String type, String id) {
            this.type = type;
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }
    }

    public static List<Map<String, Object>> haowen() {
        return haowen2("all");
    }

    /**
     * @param period 以天为时间跨度，默认为 all，其余可以选择 1，7，30，365
     * @return
     */
    public static List<Map<String, Object>> haowen(String period) {
        String url = String.format("https://post.smzdm.com/hot_%s", period);

        String html = HttpRequest.get(url).userAgent(UA_CHROME).body();
        Document document = Jsoup.parse(html);

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

//            String contentHtml = HttpRequest.get(articleLink).userAgent(UA_CHROME).body();
//            Document contentDocument = Jsoup.parse(contentHtml);
//            Element content = contentDocument.selectFirst("#articleId");
//            content.select(".item-name").remove();
//            content.select(".recommend-tab").remove();
//            String description = content.html();
//            dataRow.put("description", description);
            dataRows.add(dataRow);
        }

        return dataRows;
    }

    /**
     * @param period 以天为时间跨度，默认为 all，其余可以选择 1(今日热门)，7(周热门)，30(月热门)，365(年热门)，all(总热门)
     * @return
     */
    public static List<Map<String, Object>> haowen2(String period) {
        String url = "https://post.smzdm.com/rank/json_more/";

        Map<String, Object> params = new HashMap<>();
        params.put("unit", period);
        params.put("p", 1);

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();

        List<Map<String, Object>> dataRows = new ArrayList<>();

        JsonNode data = JsonUtils.json(result, "data");
        for (JsonNode item : data) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("id", JsonUtils.string(item, "article_id"));
            dataRow.put("title", JsonUtils.string(item, "title"));
            dataRow.put("author", JsonUtils.string(item, "nickname"));
            dataRow.put("up_count", JsonUtils.string(item, "up_count"));
            dataRow.put("collection_count", JsonUtils.string(item, "collection_count"));
            dataRow.put("comment_count", JsonUtils.string(item, "comment_count"));
            dataRow.put("pubdate", JsonUtils.string(item, "publish_time"));
            dataRow.put("link", JsonUtils.string(item, "article_url"));
            dataRow.put("brief", JsonUtils.string(item, "content"));
            dataRows.add(dataRow);
        }

        return dataRows;
    }

    public static List<Map<String, Object>> search(String keyword) {
        String url = "https://search.smzdm.com/";

        Map<String, Object> param = new HashMap<>();
        param.put("c", "faxian");
        param.put("s", keyword);
        param.put("order", "time");
        param.put("v", "b");
        param.put("p", "1");

        String html = HtmlRequest.firefox().getPage(HttpRequest.encode(HttpRequest.append(url, param))).asXml();

        Document document = Jsoup.parse(html);

        List<Map<String, Object>> dataRows = new ArrayList<>();
        Elements eles = document.select(".feed-row-wide");
        for (Element ele : eles) {
            Elements eleItems = ele.select(".feed-block-title a");
            String title = null;
            String price = null;
            String link = null;
//            if (null != eleItems && eleItems.size() > 0) {
            title = eleItems.get(0).text().trim();
            link = eleItems.get(0).attr("href");
//                if (eleItems.size() == 2) {
            price = eleItems.get(1).text().trim();
//                }
//            }else{
//                continue;
//            }

            String pubdate = ele.selectFirst(".feed-block-extras").ownText();

            Element descriptEle = ele.selectFirst("div.feed-block-descripe-top");
            String description = null == descriptEle ? "" : descriptEle.html();

            Element platformEle = ele.selectFirst(".feed-block-extras span");
            String platform = null == platformEle ? null : platformEle.text();
            String img = ele.selectFirst(".z-feed-img img").attr("src");

            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", title);
            dataRow.put("price", price);
            dataRow.put("platform", platform);
            dataRow.put("pubdate", pubdate);
            dataRow.put("link", link);
            dataRow.put("img", img);
            dataRow.put("desc", description);

            dataRows.add(dataRow);
        }

        return dataRows;

    }

    public static List<Map<String, Object>> searchArticle(String keyword) {
        String url = "https://search.smzdm.com/";

        Map<String, Object> param = new HashMap<>();
        param.put("c", "post");
        param.put("s", keyword);
        param.put("order", /*"score"*/"time");
        param.put("v", "b");

        String html = HtmlRequest.firefox().getPage(HttpRequest.encode(HttpRequest.append(url, param))).asXml();

        Document document = Jsoup.parse(html);

        List<Map<String, Object>> dataRows = new ArrayList<>();
        Elements eles = document.select(".feed-row-wide");
        for (Element ele : eles) {
            String title = ele.selectFirst(".feed-shaiwu-title a").text();
            String link = ele.selectFirst(".feed-shaiwu-title a").attr("href");

            String pubdate = ele.selectFirst(".feed-block-extras").ownText();

            Element descriptEle = ele.selectFirst("div.feed-shaiwu-descripe");
            String description = null == descriptEle ? "" : descriptEle.text();

            String author = ele.selectFirst(".z-avatar-group a.z-avatar-name").text();

            String img = ele.selectFirst(".z-feed-img img").attr("src");

            String tags = "";
            Elements tagEles = ele.select(".feed-block-info .feed-block-tags a");
            if (null != tagEles && tagEles.size() > 0) {
                for (Element tagEle : tagEles) {
                    tags += (tagEle.text() + ";");
                }
            }

            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", title);
            dataRow.put("author", author);
            dataRow.put("tags", tags);
            dataRow.put("pubdate", pubdate);
            dataRow.put("link", link);
            dataRow.put("img", img);
            dataRow.put("desc", description);

            dataRows.add(dataRow);
        }

        return dataRows;

    }

    public static List<Map<String, Object>> ranking(RankType type) {
        log.debug("");
        String url = "https://www.smzdm.com/top/json_more";

        Map<String, Object> params = new HashMap<>();
        params.put("rank_type", type.getType());
        params.put("rank_id", type.getId());
        params.put("hour", "");

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        List<Map<String, Object>> dataRows = new ArrayList<>();

        JsonNode data = JsonUtils.json(result, "data", "list");
        for (JsonNode arr : data) {
            for (JsonNode item : arr) {
                if (null == item || (item.isArray())) {
                    continue;
                }
                Map<String, Object> dataRow = JsonUtils.map(item);
                dataRows.add(dataRow);
            }
        }

        return dataRows;
    }

    public static List<Map<String, Object>> userArticles(String uid) {
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

        return dataRows;
    }

    // public static List<Map<String, Object>> userBaoLiao(String uid) {
    //     String url = String.format("https://zhiyou.smzdm.com/member/%s/baoliao/", uid);
    //
    //     String html = HttpRequest.get(url).userAgent(UA_CHROME).body();
    //     Document document = Jsoup.parse(html);
    //     // String title = document.selectFirst(".info-stuff-nickname").text();
    //
    //     Elements eles = document.select(".pandect-content-stuff");
    //     for (Element ele : eles) {
    //         Element e = ele.selectFirst(".pandect-content-title a");
    //         String articleTitle = e.text();
    //         String articleLink = e.attr("href");
    //         String articlePubDate = ele.selectFirst(".pandect-content-time").text();
    //
    //         String contentHtml = HttpRequest.get(articleLink).userAgent(UA_CHROME).body();
    //         Document contentDocument = Jsoup.parse(contentHtml);
    //         String articleContent = contentDocument.selectFirst("article.txt-detail").html();
    //     }
    // }

    public static void main(String[] args) {
        String out = "";
        // System.out.println(MapUtils.print(haowen("1")));
//        System.out.println(MapUtils.print(haowen2("7")));

       out = MapUtils.print(search("xbox"));

//        List<Map<String, Object>> data = new ArrayList<>();
//        data.addAll(ranking(RankType.好价电商榜_天猫));
//        data.addAll(ranking(RankType.好价电商榜_京东));
//        out = MapUtils.print(data);
//        out = MapUtils.print(ranking(RankType.好价电商榜_天猫));
//        out = MapUtils.print(ranking(RankType.好价电商榜_京东));
//        out = MapUtils.print(ranking(RankType.好价电商榜_苏宁易购));
//        out = MapUtils.print(ranking(RankType.好价电商榜_网易));
//        out = MapUtils.print(ranking(RankType.好价电商榜_券活动));
//        out = MapUtils.print(ranking(RankType.好价电商榜_考拉海购));

//        out = MapUtils.print(ranking(RankType.好价品类榜_全部));
//        out = MapUtils.print(ranking(RankType.好价品类榜_电脑数码));
//        out = MapUtils.print(ranking(RankType.好价品类榜_家用电器));
//        out = MapUtils.print(ranking(RankType.好价品类榜_日用百货));
//        out = MapUtils.print(ranking(RankType.好价品类榜_服饰鞋包));
//        out = MapUtils.print(ranking(RankType.好价品类榜_白菜));
//        out = MapUtils.print(ranking(RankType.好价品类榜_运动户外));
//         out = MapUtils.print(ranking(RankType.好价品类榜_食品生鲜));

        System.out.println(out);
    }
}
