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
 * @create 2022/8/15 23:39
 */
public class GuangDiu {
    /**
     * 日常
     *
     * @return
     */
    public static Table daily() {
        return execute("k=daily");
    }

    /**
     * 家电
     *
     * @return
     */
    public static Table electrical() {
        return execute("k=electrical");
    }

    public static Table food() {
        return execute("k=food");
    }

    public static Table personCare() {
        return execute("k=pcare");
    }

    /**
     * 配饰
     *
     * @return
     */
    public static Table accessory() {
        return execute("k=accessory");
    }

    /**
     * 家具家装
     *
     * @return
     */
    public static Table furniture() {
        return execute("k=furniture");
    }

    /**
     * 医疗保健
     *
     * @return
     */
    public static Table medical() {
        return execute("k=medical");
    }

    /**
     * 男装
     *
     * @return
     */
    public static Table menswear() {
        return execute("k=menswear");
    }

    /**
     * 男鞋
     *
     * @return
     */
    public static Table mensshoes() {
        return execute("k=mensshoes");
    }

    public static Table sport() {
        return execute("k=sport");
    }

    public static Table all() {
        return execute(null);
    }

    private static Table execute(String query) {
        String host = "https://guangdiu.com";

        String url = host;
        if (null != query && query.trim().length() > 0) {
            url = String.format("%s/cate.php?%s", host, query);
        }

        String html = HttpRequest.get(url).userAgent(UA_CHROME).body();
        Document document = Jsoup.parse(html);

        List<Map<String, Object>> dataRows = new ArrayList<>();

        Elements goods = document.select("#mainleft > div.zkcontent > div.gooditem");
        for (Element good : goods) {
            String title = good.selectFirst("a.goodname").ownText();
            String price = good.selectFirst("a.goodname span.emphricepart").text();
            String image = good.selectFirst("img.imgself").attr("src");
            String link = good.selectFirst("div.iteminfoarea > h2 > a").attr("href");
            // String syncPlatform = good.selectFirst("div.infofrom").text();
            String actUrl = good.selectFirst("a.innergototobuybtn").attr("href");
            String platform = good.selectFirst("a.rightmallname").text();

            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", title);
            dataRow.put("price", price);
            // dataRow.put("sync", syncPlatform);
            dataRow.put("platform", platform);
            dataRow.put("url", actUrl);
            dataRow.put("image", image);
            dataRow.put("link", link);
            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) throws Exception {
        Table table = food();
        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
