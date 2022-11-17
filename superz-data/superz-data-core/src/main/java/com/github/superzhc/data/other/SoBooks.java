package com.github.superzhc.data.other;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.common.utils.PathUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2022/11/2 11:10
 **/
public class SoBooks {
    private static final String UA_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36";
    private static final String ROOT_URL = "https://www.sobooks.net";

    public static int getTotalPage(String tag) {
        String url = String.format("%s%s", ROOT_URL, null == tag ? "" : (tag.startsWith("/") ? tag : "/" + tag));

        String html = HttpRequest.get(url).userAgent(UA_CHROME).body();
        Document document = Jsoup.parse(html);
        String str = document.select("div.pagination li").last().text();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return Integer.valueOf(matcher.group());
        }

        return 0;
    }

    public static List<Map<String, Object>> all(int page) {
        return execute(null, page);
    }

    private static List<Map<String, Object>> execute(String tag, Integer page) {
        String urlTemplate = String.format("%s%s/page/%%d", ROOT_URL, null == tag ? "" : (tag.startsWith("/") ? tag : "/" + tag));

        if (null == page) {
            page = 1;
        }

        List<Map<String, Object>> dataRows = new ArrayList<>();

        String html = HttpRequest.get(String.format(urlTemplate, page)).userAgent(UA_CHROME).body();
        Document document = Jsoup.parse(html);
        Elements eles = document.select("div.content div#cardslist div.card-item");
        for (Element ele : eles) {
            Map<String, Object> dataRow = new LinkedHashMap<>();

            String type = ele.selectFirst("a.metacat").text();
            String imgUrl = ele.selectFirst("img.thumb").attr("data-original");
            String title = ele.selectFirst("h3 a").text();
            String link = ele.selectFirst("h3 a").attr("href");
            String author = ele.selectFirst("p").text();

            dataRow.put("title", title);
            dataRow.put("author", author);
            dataRow.put("type", type);
            dataRow.put("link", link);
            dataRow.put("image", imgUrl);

            dataRows.add(dataRow);
        }

        return dataRows;
    }

    public static void main(String[] args) {
        try (JdbcHelper jdbc = new JdbcHelper(String.format("jdbc:sqlite:%s/%s", PathUtils.project(), "superz-data/superz-data-core/db/books.db"))) {
            String sql1 = "CREATE TABLE IF NOT EXISTS SOBOOKS_20221102(id INTEGER PRIMARY KEY,title VARCHAR(255),author VARCHAR(255),type VARCHAR(255),link VARCHAR(1024),image TEXT)";
            jdbc.ddlExecute(sql1);


            for (int j = 334; j <= 396; j++) {
                List<Map<String, Object>> data = all(j);

                Object[][] data2 = new Object[data.size()][];
                for (int i = 0, len = data.size(); i < len; i++) {
                    Map<String, Object> item = data.get(i);

                    Object[] item2 = new Object[5];
                    item2[0] = item.get("title");
                    item2[1] = item.get("author");
                    item2[2] = item.get("type");
                    item2[3] = item.get("link");
                    item2[4] = item.get("image");

                    data2[i] = item2;
                }

                jdbc.batchUpdate("INSERT INTO SOBOOKS_20221102(title,author,type,link,image) VALUES(?,?,?,?,?)", data2);
            }
        }
    }
}
