package com.github.superzhc.data.stock;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;

import java.util.*;

/**
 * @author superz
 * @create 2022/9/13 11:29
 **/
public class EastMoneyStock {
    public static List<Map<String, Object>> boardConceptName() {
        String url = "http://push2.eastmoney.com/api/qt/clist/get";

        Map<String, Object> params = new HashMap<>();
        params.put("pn", 1);
        params.put("pz", 2000);
        params.put("po", "1");
        params.put("np", "1");
        params.put("fltt", "2");
        params.put("invt", "2");
        params.put("fid", "3");
        params.put("fs", "m:90 t:3 f:!50");
        params.put("fields", "f2,f3,f4,f8,f12,f14,f15,f16,f17,f18,f20,f21,f24,f25,f22,f33,f11,f62,f128,f124,f107,f104,f105,f136");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        JsonNode data = JsonUtils.json(result, "data", "diff");

        List<Map<String, Object>> dataRows = new ArrayList<>();
        for (JsonNode item : data) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("板块名称", JsonUtils.string(item.get("f14")));
            dataRow.put("板块代码", JsonUtils.string(item.get("f12")));
            dataRow.put("最新价", JsonUtils.aDouble(item.get("f2")));
            dataRow.put("涨跌额", JsonUtils.aDouble(item.get("f4")));
            dataRow.put("涨跌幅", JsonUtils.aDouble(item.get("f3")));
            dataRow.put("总市值", JsonUtils.aLong(item.get("f20")));
            dataRow.put("换手率", JsonUtils.aDouble(item.get("f8")));
            dataRow.put("上涨家数", JsonUtils.integer(item.get("f104")));
            dataRow.put("下跌家数", JsonUtils.integer(item.get("f105")));
            dataRow.put("领涨股票", JsonUtils.string(item.get("f128")));
            dataRow.put("领涨股票-涨跌幅", JsonUtils.string(item.get("f136")));

            dataRows.add(dataRow);
        }

        return dataRows;
    }

    // 概念板块-历史行情
    public static List<Map<String, Object>> boardConceptHistory(String symbol, String type) {
        String url = "http://push2his.eastmoney.com/api/qt/stock/kline/get";

        Map<String, Object> params = new HashMap<>();
        params.put("secid", String.format("90.%s", symbol));
        params.put("fields1", "f1,f2,f3,f4,f5,f6");
        params.put("fields2", "f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61");
        params.put("klt", "101");
        params.put("fqt", "0"/*0 不复权，1 前复权，2 后复权*/);
        params.put("beg", "0");
        params.put("end", "20500101");
        params.put("smplmt", "10000");
        params.put("lmt", 1000000);
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();

        // TODO

        return null;
    }

    // 概念模块分时历史行情
    public static List<Map<String, Object>> boardConceptMinuteHistory(String symbol, String period) {
        String url = "http://91.push2his.eastmoney.com/api/qt/stock/kline/get";

        Map<String, Object> params = new HashMap<>();
        params.put("secid", String.format("90.%s", symbol));
        params.put("fields1", "f1,f2,f3,f4,f5,f6");
        params.put("fields2", "f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61");
        params.put("klt", period/*周期："1", "5", "15", "30", "60"*/);
        params.put("fqt", "1");
        params.put("end", "20500101");
        params.put("lmt", "1000000");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();

        return null;
    }

    // 概念模块组成成分
    public static List<Map<String, Object>> boardConceptConstituent(String symbol) {
        String url = "http://29.push2.eastmoney.com/api/qt/clist/get";

        Map<String, Object> params = new HashMap<>();
        params.put("pn", "1");
        params.put("pz", "2000");
        params.put("po", "1");
        params.put("np", "1");
        params.put("fltt", "2");
        params.put("invt", "2");
        params.put("fid", "f3");
        params.put("fs", String.format("b:%s f:!50", symbol));
        params.put("fields", "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152,f45");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();

        return null;
    }

    public static List<Map<String, Object>> newShares() {
        String url = "https://datainterface.eastmoney.com/EM_DataCenter/JS.aspx";

        Map<String, Object> params = new HashMap<>();
        params.put("st", "16");
        params.put("sr", "-1");
        params.put("ps", "50000");
        params.put("p", "1");
        params.put("type", "NS");
        params.put("sty", "NSDXSYL");
        params.put("js", "({data:[(x)],pages:(pc)})");

        String result = HttpRequest.get(url, params).body();
        result = result.substring(1, result.length() - 1);
        JsonNode json = JsonUtils.json(result, "data");

        List<Map<String, Object>> dataRows = new ArrayList<>();
        /*"股票代码",
        "股票简称",
        "发行价",
        "最新价",
        "网上-发行中签率",
        "网上-有效申购股数",
        "网上-有效申购户数",
        "网上-超额认购倍数",
        "网下-配售中签率",
        "网下-有效申购股数",
        "网下-有效申购户数",
        "网下-配售认购倍数",
        "总发行数量",
        "开盘溢价",
        "首日涨幅",
        "打新收益",
        "上市日期",
        "-" */
        for (JsonNode item : json) {
            String str = JsonUtils.string(item);
            String[] arr = str.split(",", -1);

            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("股票代码", arr[0]);
            dataRow.put("股票简称", arr[1]);
            dataRow.put("发行价", arr[2]);
            dataRow.put("最新价", arr[3]);
            dataRow.put("网上-发行中签率", arr[4]);
            dataRow.put("网上-有效申购股数", arr[5]);
            dataRow.put("网上-有效申购户数", arr[6]);
            dataRow.put("网上-超额认购倍数", arr[7]);
            dataRow.put("网下-配售中签率", arr[8]);
            dataRow.put("网下-有效申购股数", arr[9]);
            dataRow.put("网下-有效申购户数", arr[10]);
            dataRow.put("网下-配售认购倍数", arr[11]);
            dataRow.put("总发行数量", arr[12]);
            dataRow.put("开盘溢价", arr[13]);
            dataRow.put("首日涨幅", arr[14]);
            dataRow.put("打新收益", arr[15]);
            dataRow.put("上市日期", arr[16]);
            dataRow.put("-", arr[17]);

            dataRows.add(dataRow);
        }
        return dataRows;
    }

    public static void main(String[] args) {
        boardConceptName();
    }
}
