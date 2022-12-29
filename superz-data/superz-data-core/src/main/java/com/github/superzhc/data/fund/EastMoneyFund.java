package com.github.superzhc.data.fund;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.MapUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author superz
 * @create 2022/11/18 9:32
 **/
public class EastMoneyFund {
    private static final Logger log = LoggerFactory.getLogger(EastMoneyFund.class);

    public static final String UA =
            "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/94.0.4606.71";
    public static final String UA_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36";

    /**
     * SQL表创建语句：create table em_funds(id bigint auto_increment primary key,code varchar(8) not null,name varchar(255) not null,type varchar(255) not null,pinyin varchar(255) null,full_pinyin varchar(255) null)
     *
     * @return
     */
    public static List<Map<String, Object>> funds() {
        String url = "http://fund.eastmoney.com/js/fundcode_search.js";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", UA_CHROME);

        try {
            String result = HttpRequest.get(url).headers(headers).body();
            String json = result.substring("var r = ".length(), result.length() - 1);
            JsonNode nodes = JsonUtils.json(json);
            Map<String, Object>[] data = JsonUtils.arrayArray2Map(nodes, new String[]{"code",
                    "pinyin",
                    "name",
                    "type",
                    "full_pinyin"});

            return Arrays.asList(data);
        } catch (Exception e) {
            log.error("解析失败", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Map<String, Object>> fundNetHistory(String symbol) {
        String url = "https://fundmobapi.eastmoney.com/FundMNewApi/FundMNHisNetList";

        Map<String, Object> params = new HashMap<>();
        params.put("FCODE", symbol);
        params.put("IsShareNet", true);
        params.put("pageIndex", 1);
        params.put("pageSize", LocalDate.of(1990, 1, 1).until(LocalDate.now(), ChronoUnit.DAYS));
        params.put("deviceid", "Wap");
        params.put("plat", "Wap");
        params.put("product", "EFund");
        params.put("version", "6.2.8");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params)
                .userAgent(UA)
                //.accept("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                //.acceptEncoding("gzip, deflate, br")
                //.header("Host", "fundmobapi.eastmoney.com")
                .body();

        JsonNode json = JsonUtils.json(result).get("Datas");

        List<String> columnNames = Arrays.asList(
                "FSRQ",
                "DWJZ",
                "LJJZ",
                "JZZZL"
        );
        Map<String, Object>[] originData = JsonUtils.newObjectArray4Keys(json, columnNames);
        List<Map<String, Object>> data = new ArrayList<>(originData.length);
        for (Map<String, Object> originItem : originData) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("code", symbol);
            item.put("date", originItem.get("FSRQ"));
            item.put("net_worth", originItem.get("DWJZ"));
            item.put("accumulated_net_worth", originItem.get("LJJZ"));
            item.put("change", originItem.get("JZZZL"));

            data.add(item);
        }
        return data;
    }

    public static List<Map<String, Object>> fundRealNet(String... symbols) {
        if (null == symbols || symbols.length < 1) {
            throw new IllegalArgumentException("at least one fund");
        }

        String url = "https://fundmobapi.eastmoney.com/FundMNewApi/FundMNFInfo";

        Map<String, Object> params = new HashMap<>();
        params.put("Fcodes", String.join(",", symbols));
        params.put("pageIndex", 1);
        params.put("pageSize", symbols.length);
        // params.put("Sort", "");
        // params.put("SortColumn", "");
        params.put("IsShowSE", false);
        // params.put("P", "F");
        params.put("deviceid", "Wap");
        params.put("plat", "Wap");
        params.put("product", "EFund");
        params.put("version", "6.2.8");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode json = JsonUtils.json(result, "Datas");

        Map<String, Object>[] originData = JsonUtils.newObjectArray(json);
        List<Map<String, Object>> data = new ArrayList<>();
        for (Map<String, Object> originItem : originData) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("code", originItem.get("FCODE"));
            item.put("name", originItem.get("SHORTNAME"));
            // 上一个交易日的值
            item.put("latest_date", originItem.get("PDATE"));
            item.put("latest_net_worth", originItem.get("NAV"));
            item.put("latest_accumulated_net_worth", originItem.get("ACCNAV"));
            item.put("latest_change", originItem.get("NAVCHGRT"));
            // 预估值
            item.put("estimate_net_worth", originItem.get("GSZ"));
            item.put("estimate_change", originItem.get("GSZZL"));
            item.put("estimate_date", originItem.get("GZTIME"));

            data.add(item);
        }

        return data;
    }

    public static Map<String, Object> tsdata(String symbol) {
        String url = String.format("http://fundf10.eastmoney.com/tsdata_%s.html", symbol);

        String html = HttpRequest.get(url).body();
        Document doc = Jsoup.parse(html);

        Element basic = doc.selectFirst("div.basic-new > div.bs_jz .title > a");
        String indexName = basic.attr("title");
        String indexCode = basic.text().substring(indexName.length());
        indexCode = indexCode.trim();
        indexCode = indexCode.substring(1, indexCode.length() - 1);

        String levelInAllFunds = doc.select("div.fxdj").get(0).selectFirst("span.chooseLow").text();
        String levelInSimilarFunds = doc.select("div.fxdj").get(1).selectFirst("span.chooseLow").text();

        Element table = doc.selectFirst("div#jjzsfj table.fxtb");
        Elements cells = table.select("tr").get(1).select("td");
        String trackingIndex = cells.get(0).text();
        String trackingError = cells.get(1).text();
        String similarTrackingError = cells.get(2).text();

        String rq = doc.selectFirst("div#jjzsfj div.limit-time").text().substring("截止至：".length());

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("index_code", indexCode);
        map.put("index_name", indexName);
        map.put("在所有基金中的风险等级", levelInAllFunds);
        map.put("在同类基金中的风险等级", levelInSimilarFunds);
        map.put("时间", rq);
        map.put("跟踪指数", trackingIndex);
        map.put("跟踪误差", trackingError);
        map.put("同类平均跟踪误差", similarTrackingError);

        return map;
    }

    public static void main(String[] args) {
//        System.out.println(tsdata("012348"));

//        System.out.println(MapUtils.print(funds(),20));
        System.out.println(MapUtils.print(fundNetHistory("012348"), 100));
    }

}
