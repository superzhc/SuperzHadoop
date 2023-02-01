package com.github.superzhc.data.shopping;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.utils.MapUtils;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 逛丢
 *
 * @author superz
 * @create 2022/10/24 19:31
 **/
public class GuangDiu {
    private static final String UA_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36";

    /**
     * 日常
     *
     * @return
     */
    public static List<Map<String, Object>> daily() {
        return execute("k=daily");
    }

    /**
     * 家电
     *
     * @return
     */
    public static List<Map<String, Object>> electrical() {
        return execute("k=electrical");
    }

    public static List<Map<String, Object>> food() {
        return execute("k=food");
    }

    public static List<Map<String, Object>> personCare() {
        return execute("k=pcare");
    }

    /**
     * 配饰
     *
     * @return
     */
    public static List<Map<String, Object>> accessory() {
        return execute("k=accessory");
    }

    /**
     * 家具家装
     *
     * @return
     */
    public static List<Map<String, Object>> furniture() {
        return execute("k=furniture");
    }

    /**
     * 医疗保健
     *
     * @return
     */
    public static List<Map<String, Object>> medical() {
        return execute("k=medical");
    }

    /**
     * 男装
     *
     * @return
     */
    public static List<Map<String, Object>> menswear() {
        return execute("k=menswear");
    }

    /**
     * 男鞋
     *
     * @return
     */
    public static List<Map<String, Object>> mensshoes() {
        return execute("k=mensshoes");
    }

    public static List<Map<String, Object>> sport() {
        return execute("k=sport");
    }

    public static List<Map<String, Object>> all() {
        return execute(null);
    }

    private static List<Map<String, Object>> execute(String query) {
        String host = "https://guangdiu.com";

        String url = host;
        if (null != query && query.trim().length() > 0) {
            url = String.format("%s/cate.php?%s", host, query);
        } else {
            url = String.format("%s/index.php", host);
        }

        Map<String, Object> params = new HashMap<>();
        // 分页
        params.put("p", 1);

        String html = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        Document document = Jsoup.parse(html);

        List<Map<String, Object>> dataRows = new ArrayList<>();

        Pattern pattern = Pattern.compile("id=(\\d+)");

        Elements goods = document.select("#mainleft > div.zkcontent > div.gooditem");
        for (Element good : goods) {
            String id = good.selectFirst("a.goodname").attr("href");
            Matcher matcher = pattern.matcher(id);
            if (matcher.find()) {
                id = matcher.group(1);
            } else {
                id = null;
            }

            String title = good.selectFirst("a.goodname").ownText();

//            String titleFenCi = HanLP.segment(title).stream()
//                    .filter(t -> {
//                        if (t.nature.startsWith('w')
//                                || t.nature.startsWith('u')
//                                || t.nature.startsWith('m')// 去掉数词
//                                // || t.nature.startsWith('q')// 去掉量词
//                                || t.nature.startsWith('t')) {
//                            return false;
//                        }
//
//                        return !CoreStopWordDictionary.contains(t.word);
//                    })
//                    .map(d -> d.word)
//                    .collect(Collectors.joining(";"));

            Element priceEle = good.selectFirst("a.goodname span.emphricepart");
            String price = null == priceEle ? null : priceEle.text();
            String image = good.selectFirst("img.imgself").attr("src");
            String link = good.selectFirst("div.iteminfoarea > h2 > a").attr("href");
            link = host + "/" + link;
            Element syncPlatformEle = good.selectFirst("div.infofrom");
            String syncPlatform = null == syncPlatformEle ? null : syncPlatformEle.text();
            String brief = good.selectFirst("div.shortabstract > a.abstractcontent").text();
            String actUrl = good.selectFirst("a.innergototobuybtn").attr("href");
            if (!(actUrl.startsWith("http://") && actUrl.startsWith("https://"))) {
                actUrl = host + "/" + actUrl;
            }
            Element platformEle = good.selectFirst("a.rightmallname");
            String platform = null == platformEle ? null : platformEle.text();
            Element platformMore = good.selectFirst("div.rightmall > span.malladd");
            if (null != platformMore) {
                platform += platformMore.text().replaceAll("\\s", "");
            }

            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("id", Integer.valueOf(id));
            dataRow.put("title", title);
//            dataRow.put("title_fenci", titleFenCi);
            dataRow.put("price", price);
            dataRow.put("sync", syncPlatform);
            dataRow.put("platform", platform);
            dataRow.put("url", actUrl);
            dataRow.put("image", image);
            dataRow.put("brief", brief);
            dataRow.put("link", link);
            dataRows.add(dataRow);
        }

        return dataRows;
    }

    public static void main(String[] args) throws Exception {
//        // 加载自定义词典
//        HanLP.Config.CustomDictionaryPath = new String[]{"data/dictionary/custom/CustomDictionary.txt", "商品名称关键字.txt"};
//        HanLP.Config.enableDebug();
        System.out.println(MapUtils.print(all()));
    }
}
