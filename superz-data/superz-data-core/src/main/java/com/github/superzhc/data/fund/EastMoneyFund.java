package com.github.superzhc.data.fund;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

import static com.github.superzhc.data.utils.XueQiuUtils.UA;

/**
 * @author superz
 * @create 2022/11/18 9:32
 **/
public class EastMoneyFund {
    public static List<Map<String, String>> fundRealNet(String... symbols) {
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

        String result = HttpRequest.get(url, params).userAgent(UA).body();
        JsonNode json = JsonUtils.json(result, "Datas");

        List<Map<String, String>> data = Arrays.asList(JsonUtils.objectArray2Map(json));

//        List<String[]> dataRows = JsonUtils.objectArrayWithKeys(json, columnNames);
//
//        Table table = TableUtils.build(dataRows);
//        table.column("FCODE").setName("code");
//        table.column("SHORTNAME").setName("name");
//        // 上一个交易日的值
//        table.column("PDATE").setName("latest_date");
//        table.column("NAV").setName("latest_net_worth");
//        table.column("ACCNAV").setName("latest_accumulated_net_worth");
//        table.column("NAVCHGRT").setName("latest_change");
//        // 预估值
//        table.column("GSZ").setName("estimate_net_worth");
//        table.column("GSZZL").setName("estimate_change");
//        table.column("GZTIME").setName("estimate_date");
        //table.column("").setName("");

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
        System.out.println(tsdata("012348"));
    }

}
