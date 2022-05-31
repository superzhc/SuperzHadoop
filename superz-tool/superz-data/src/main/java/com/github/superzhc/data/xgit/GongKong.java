package com.github.superzhc.data.xgit;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jdbc.JdbcHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/4/8 16:05
 **/
public class GongKong {
    public static void main(String[] args) throws Exception {
        String jdbcUrl = "jdbc:mysql://localhost:3306/news_dw?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";
        String sql = "INSERT INTO xgit_spider(title, author,brief,content) VALUES(?, ?,?, ?)";
        try (JdbcHelper jdbc = new JdbcHelper(jdbcUrl, username, password)) {
            String url = "http://www.gongkong.com/design/DemandLink/";

            Map<String, String> headers = new HashMap<>();
            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36");
            headers.put("Host", "www.gongkong.com");
            //headers.put("Cookie","ASP.NET_SessionId=hin0jawqj05csn55rezekawe; CheckCode=48nt");

            for (int i = 6094; i < 6885; i++) {
                Map<String, String> params = new HashMap<>();
                // 总页码 6885
                params.put("pageIndex", String.valueOf(i)/*"1"*/);
                params.put("pageSize", "10");
                params.put("status", "-1");
                params.put("id", "0");
                params.put("brandid", "0");
                params.put("industryid", "0");
                params.put("productsid", "0");
                params.put("parmterid", "0");
                params.put("iitype", "0");
                params.put("sort", "0");
                params.put("sortType", "1");
                params.put("sTypeValue", "0");

                String html = HttpRequest.get(url, params).headers(headers).body();
                Document doc = Jsoup.parse(html);
                Elements elements = doc.select("div.main-bottom > div.cont_l > div > div.cont_tj_text > div.cont_tj_list");

                List<List<Object>> dataRows = new ArrayList<>();

                for (Element element : elements) {
                    Element cname = element.selectFirst("a.cname");
                    //String title = cname.attr("title");
                    String articleUrl = cname.attr("href");
                    if (articleUrl.startsWith("/download")) {
                        continue;
                    }
                    articleUrl = String.format("http://www.gongkong.com%s", articleUrl);

                    String brief = element.select("div.cont_tj_list_r_text > div").text();

                    String childHtml = HttpRequest.get(articleUrl).headers(headers).body();

                    Document childDoc = Jsoup.parse(childHtml);//Jsoup.connect(artUrl).headers(headers).get();

                    Element titleElement = childDoc.selectFirst("body > div.main-content > div > div.main-left > div.msg > h1");
                    if (null == titleElement) {
                        continue;
                    }
                    String title = titleElement.text();

                    Element authorElement = childDoc.selectFirst("body > div.main-content > div > div.main-left > div.msg > div.regsiter a");
                    String author = null == authorElement ? null : authorElement.text();

                    // 资源不需要爬取
                    Element contentElement = childDoc.selectFirst("body > div.main-content > div > div.main-left > div.msg > div.summary");
//                    if (null == contentElement) {
//                        continue;
//                    }
                    String content = contentElement.html().replaceAll("\n", "").replace("\"", "'");

                    List<Object> dataRow = new ArrayList<>();
                    dataRow.add(title);
                    dataRow.add(author);
                    dataRow.add(brief);
                    dataRow.add(content);
                    dataRows.add(dataRow);
                    //System.out.printf("title:%s\nauthor:%s\n%s\n------------------------------------------------------\n", title, author, content);
                }
                jdbc.batchUpdate(sql, dataRows);
            }
        }
    }
}
