package com.github.superzhc.data.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.html.HtmlRequest;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.MapUtils;

import java.util.*;

import static com.github.superzhc.data.utils.XueQiuUtils.UA;

/**
 * @author superz
 * @create 2022/11/21 23:56
 */
public class EastMoneyIndex {

    public static List<Map<String, Object>> indices() {
        return indices("m:1+s:3,m:0+t:5,m:2");
    }

    public static List<Map<String, Object>> csIndices() {
        return indices("m:2");
    }

    public static List<Map<String, Object>> szseIndices() {
        return indices("m:0+t:5");
    }

    public static List<Map<String, Object>> sseIndices() {
        return indices("m:1+s:3");
    }

    public static List<Map<String, Object>> indices(String type) {
        String url = "http://push2.eastmoney.com/api/qt/clist/get";

        String fields = "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f33,f11,f62,f128,f136,f115,f152";
        Map<String, Object> params = new HashMap<>();
        params.put("pn", 1);// pageNumber
        params.put("pz", 10000);//pageSize
        params.put("po", 1);
        params.put("np", 1);
        // params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
        params.put("fltt", 2);
        params.put("invt", 2);
        params.put("fid", "f3");
        params.put("fs", type);
        params.put("fields", fields);
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(UA).referer("http://quote.eastmoney.com/").body();
        JsonNode json = JsonUtils.json(result, "data", "diff");
        Map<String, Object>[] arr = JsonUtils.newObjectArray(json);

        List<Map<String, Object>> data = new ArrayList<>();
        for (Map<String, Object> item : arr) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("index_code", String.format("%s.%s", item.get("f12"), transform2M(String.valueOf(item.get("13")))));
            map.put("index_name", item.get("f14"));
            map.put("market", item.get("f13"));
            map.put("last", item.get("f18"));
            map.put("open", item.get("f17"));
            map.put("high", item.get("f15"));
            map.put("low", item.get("f16"));
            map.put("close", item.get("f2"));
            map.put("change", item.get("f3"));
            map.put("change_price", item.get("f4"));
            map.put("volume", item.get("f5"));
            map.put("turnover", item.get("f6"));
            map.put("amplitude", item.get("f7"));
            map.put("equivalence_ratio", item.get("f10"));
            map.put("f1", item.get("f1"));
            map.put("f22", item.get("f22"));
            map.put("f24", item.get("f24"));
            map.put("f25", item.get("f25"));
            map.put("f33", item.get("f33"));
            map.put("f152", item.get("f152"));

            data.add(map);
        }
        return data;
    }

    public static List<Map<String, Object>> history(String symbol) {
        return history(symbol, "101");
    }

    public static List<Map<String, Object>> dailyHistory(String symbol) {
        return history(symbol, "101");
    }

    public static List<Map<String, Object>> weeklyHistory(String symbol) {
        return history(symbol, "102");
    }

    public static List<Map<String, Object>> monthlyHistory(String symbol) {
        return history(symbol, "103");
    }

    /**
     *
     * @param symbol
     * @param period 周期；101（每日）、102（每周）、103（每月）
     * @return
     */
    public static List<Map<String, Object>> history(String symbol, String period) {
        String url = "http://push2his.eastmoney.com/api/qt/stock/kline/get";

        Map<String, Object> params = new HashMap<>();
        params.put("secid", transform(symbol));
        // params.put("ut", "7eea3edcaed734bea9cbfc24409ed989");
        params.put("fields1", "f1,f2,f3,f4,f5,f6");
        params.put("fields2", "f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61");
        params.put("klt", period);
        params.put("fqt", "0");
        params.put("beg", "0");
        params.put("end", "20500000");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data");
        String indexName = JsonUtils.string(json, "name");

        List<Map<String, Object>> data = new ArrayList<>();

        String[] ss = JsonUtils.stringArray(json, "klines");
        for (String str : ss) {
            String[] arr = str.split(",", -1);

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("index_code", symbol);
            map.put("index_name", indexName);
            map.put("date", arr[0]);
            map.put("open", arr[1]);
            map.put("close", arr[2]);
            map.put("high", arr[3]);
            map.put("low", arr[4]);
            map.put("volume", arr[5]);
            map.put("amount", arr[6]);
            map.put("amplitude", arr[7]);
            map.put("quote_change", arr[8]);
            map.put("quote_change_amount", arr[9]);
            map.put("turnover", arr[10]);
            data.add(map);
        }
        return data;
    }

    private static String transform(String symbol) {
        String[] ss = symbol.split("\\.");
        return String.format("%s.%s", transform4M(ss[1]), ss[0]);
    }

    public static String transform4M(String market) {
        String s = market.toLowerCase();
        if ("sh".equals(s)) {
            return "1";
        } else if ("sz".equals(s)) {
            return "0";
        } else if ("csi".equals(s)) {
            return "2";
        } else {
            return "1";
        }
    }

    public static String transform2M(String marketCode) {
        if ("1".equals(marketCode)) {
            return "SH";
        } else if ("0".equals(marketCode)) {
            return "SZ";
        } else if ("2".equals(marketCode)) {
            return "CSI";
        } else {
            return "SH";
        }
    }

    public static void main(String[] args) {
        System.out.println(MapUtils.print(history("000300.SH")));
    }
}
