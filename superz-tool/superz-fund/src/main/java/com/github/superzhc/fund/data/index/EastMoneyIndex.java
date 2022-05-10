package com.github.superzhc.fund.data.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.tablesaw.utils.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.*;

/**
 * 统一 indexCode.market，示例 000300.SH
 *
 * @author superz
 * @create 2022/5/6 19:57
 **/
public class EastMoneyIndex {

    public static Table index(String symbol) {
        String url = "https://push2.eastmoney.com/api/qt/ulist.np/get";

        String fields = "f1,f2,f3,f4,f6,f12,f13,f104,f105,f106";
        Map<String, Object> params = new HashMap<>();
        params.put("fltt", 2);
        params.put("secids", transformC(symbol));
        params.put("fields", fields);
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data", "diff");

        List<String> columnNames = Arrays.asList(fields.split(","));
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        List<String> columnNames2 = Arrays.asList("f1", "f2", "f3", "f4", "成交额", "code", "f13", "上涨数", "下跌数", "平盘");
        Table table = TableUtils.build(columnNames2, dataRows);
        return table;
    }

    public static Table dailyHistory(String symbol) {
        return history(symbol, "daily");
    }

    public static Table weeklyHistory(String symbol) {
        return history(symbol, "weekly");
    }

    public static Table monthlyHistory(String symbol) {
        return history(symbol, "monthly");
    }

    /**
     * @param symbol
     * @param period 周期，choice of {'daily', 'weekly', 'monthly'}
     *
     * @return
     */
    public static Table history(String symbol, String period) {
        Map<String, String> periodMap = new HashMap<>();
        periodMap.put("daily", "101");
        periodMap.put("weekly", "102");
        periodMap.put("monthly", "103");

        String url = "http://push2his.eastmoney.com/api/qt/stock/kline/get";

        Map<String, Object> params = new HashMap<>();
        params.put("secid", transformC(symbol));
        params.put("ut", "7eea3edcaed734bea9cbfc24409ed989");
        params.put("fields1", "f1,f2,f3,f4,f5,f6");
        params.put("fields2", "f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61");
        params.put("klt", periodMap.get(period));
        params.put("fqt", "0");
        params.put("beg", "0");
        params.put("end", "20500000");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data", "klines");

        List<String> columnNames = Arrays.asList(
                "date",//"日期"
                "open",//"开盘"
                "close",//"收盘"
                "high",//"最高"
                "low",//"最低"
                "volume",//"成交量"
                "turnover",//"成交额"
                "amplitude",//"振幅"
                "quote_change",//"涨跌幅"
                "quote_change_amount",//"涨跌额"
                "turnover_rate"//"换手率"
        );

        List<String[]> dataRows = new ArrayList<>();
        for (JsonNode item : json) {
            String[] row = item.asText().split(",");
            dataRows.add(row);
        }

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    private static String transformC(String symbol) {
        String[] ss = symbol.split("\\.");
        return String.format("%s.%s", transformM(ss[1]), ss[0]);
    }

    private static String transformM(String market) {
        String s = market.toLowerCase();
        if ("sh".equals(market)) {
            return "1";
        } else if ("sz".equals(market)) {
            return "0";
        } else {
            return "1";
        }
    }

    public static void main(String[] args) {
        String symbol = "000300.SH";

        Table table = weeklyHistory(symbol);
        System.out.println(table.print());
        System.out.println(table.structure().print());
        System.out.println(table.shape());
    }
}
