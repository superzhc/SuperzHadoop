package com.github.superzhc.fund.data.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.common.JsonUtils;
import com.github.superzhc.tablesaw.utils.ReadOptionsUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.TableBuildingUtils;
import tech.tablesaw.io.html.HtmlReadOptions;

import java.util.*;

import static com.github.superzhc.common.HttpConstant.UA;
import static com.github.superzhc.fund.utils.FundConstant.*;
import static com.github.superzhc.fund.utils.IndexConstant.*;

/**
 * 统一 indexCode.market，示例 000300.SH
 *
 * @author superz
 * @create 2022/5/6 19:57
 **/
public class EastMoneyIndex {

    public static Table indices() {
        // return sseIndices().append(szseIndices()).append(csIndices());
        return indices("m:1+s:3,m:0+t:5,m:2");
    }

    public static Table csIndices() {
        return indices("m:2");
    }

    public static Table szseIndices() {
        return indices("m:0+t:5");
    }

    public static Table sseIndices() {
        return indices("m:1+s:3");
    }

    private static Table indices(String type) {
        String url = "http://push2.eastmoney.com/api/qt/clist/get";

        String fields = "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f22,f33,f11,f62,f128,f136,f115,f152";
        Map<String, Object> params = new HashMap<>();
        params.put("pn", 1);// pageNumber
        params.put("pz", 10000);//pageSize
        params.put("po", 1);
        params.put("np", 1);
        params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
        params.put("fltt", 2);
        params.put("invt", 2);
        params.put("fid", "f3");
        params.put("fs", type);
        params.put("fields", fields);
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(UA).referer("http://quote.eastmoney.com/").body();
        JsonNode json = JsonUtils.json(result, "data", "diff");

        List<String> columnNames = Arrays.asList(fields.split(","));
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        List<String> finalColumnNames = Arrays.asList(
                "f1",
                "close",
                "change",// 涨跌幅（%）
                "change_price",
                "volume",
                "turnover",
                "amplitude",//振幅（%）
                "f8",
                "f9",
                "equivalence_ratio",//量比
                "index_code",
                "market",//"f13",
                "index_name",
                "high",
                "low",
                "open",
                "last",
                "f20",
                "f21",
                "f23",
                "f24",
                "f25",
                "f26",
                "f22",
                "f33",
                "f11",
                "f62",
                "f128",
                "f136",
                "f115",
                "f152"
        );

        Map<String, ColumnType> columnTypeMap = new HashMap<>();
        columnTypeMap.put("index_code", ColumnType.STRING);
        columnTypeMap.put("market", ColumnType.STRING);
        columnTypeMap.put("volume", ColumnType.LONG);
        columnTypeMap.put("turnover", ColumnType.DOUBLE);
        columnTypeMap.put("f8", ColumnType.STRING);
        columnTypeMap.put("f9", ColumnType.STRING);
        columnTypeMap.put("f20", ColumnType.STRING);
        columnTypeMap.put("f21", ColumnType.STRING);
        columnTypeMap.put("f23", ColumnType.STRING);
        columnTypeMap.put("f26", ColumnType.STRING);
        columnTypeMap.put("f62", ColumnType.STRING);
        columnTypeMap.put("f128", ColumnType.STRING);
        columnTypeMap.put("f136", ColumnType.STRING);
        columnTypeMap.put("f115", ColumnType.STRING);
        columnTypeMap.put("f152", ColumnType.STRING);

        Table table = TableBuildingUtils.build(finalColumnNames, dataRows, ReadOptionsUtils.columnTypeByName(columnTypeMap));

        // StringColumn indexCode = StringColumn.create("index_code", table.stream().map(row ->
        //         String.format("%s.%s", row.getString("index_code"), transform2M(row.getString("market")))
        // ));
        // table.replaceColumn("index_code", indexCode);

        return table;
    }

    public static Table index(String symbol) {
        String url = "https://push2.eastmoney.com/api/qt/stock/get";

        String fields = "f58,f107,f57,f43,f59,f169,f170,f152,f46,f60,f44,f45,f47,f48,f19,f532,f39,f161,f49,f171,f50,f86,f600,f601,f154,f84,f85,f168,f108,f116,f167,f164,f92,f71,f117,f292,f113,f114,f115,f119,f120,f121,f122,f296";
        fields = "f58,f107,f57,f43,f59,f169,f170,f152,f46,f60,f44,f45,f47,f48,f19,f532,f39,f161,f49,f171,f50,f86,f600,f601,f154,f84,f85,f168,f108,f116,f167,f164,f92,f71,f117,f292,f113,f114,f115,f119,f120,f121,f122,f296";
        Map<String, Object> params = new HashMap<>();
        params.put("invt", 2);
        params.put("fltt", 1);
        // params.put("ut","fa5fd1943c7b386f172d6893dbfba10b");
        params.put("secid", transform(symbol));
        params.put("fields", fields);
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        Map<String, Object> map = JsonUtils.map(result, "data");
        MapUtils.replaceKey(map, "f57", "index_code");
        MapUtils.replaceKey(map, "f58", "index_name");
        map.put("last", Double.parseDouble(map.get("f60").toString()) / 100);
        map.remove("f60");
        map.put("open", Double.parseDouble(map.get("f46").toString()) / 100);
        map.remove("f46");
        map.put("high", Double.parseDouble(map.get("f44").toString()) / 100);
        map.remove("f44");
        map.put("low", Double.parseDouble(map.get("f45").toString()) / 100);
        map.remove("f45");
        map.put("close", Double.parseDouble(map.get("f43").toString()) / 100);
        map.remove("f43");
        MapUtils.replaceKey(map, "f47", "volume");
        MapUtils.replaceKey(map, "f48", "turnover");
        // 外盘
        MapUtils.replaceKey(map, "f49", "outer");
        // 涨家数
        MapUtils.replaceKey(map, "f113", "rise");
        // 跌家数
        MapUtils.replaceKey(map, "f114", "fall");
        // 平家数
        MapUtils.replaceKey(map, "f115", "equal");
        // 5日涨跌幅 f119
        // 20日涨跌幅 f120
        // 60日涨跌幅 f121
        // 今年涨跌幅 f122
        // 换手率
        map.put("turnover_rate", Double.parseDouble(map.get("f168").toString()) / 100);
        map.remove("f168");
        map.put("change", Double.parseDouble(map.get("f170").toString()) / 100);
        map.remove("f170");
        map.put("change_amount", Double.parseDouble(map.get("f169").toString()) / 100);
        map.remove("f169");
        map.put("amplitude", Double.parseDouble(map.get("f171").toString()) / 100);
        map.remove("f171");

        Table table = TableUtils.map2Table(map);
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
     * @return
     */
    public static Table history(String symbol, String period) {
        Map<String, String> periodMap = new HashMap<>();
        periodMap.put("daily", "101");
        periodMap.put("weekly", "102");
        periodMap.put("monthly", "103");

        String url = "http://push2his.eastmoney.com/api/qt/stock/kline/get";

        Map<String, Object> params = new HashMap<>();
        params.put("secid", transform(symbol));
        // params.put("ut", "7eea3edcaed734bea9cbfc24409ed989");
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
                INDEX_TRADE_DATE,//"日期"
                INDEX_TRADE_OPEN,//"开盘"
                INDEX_TRADE_CLOSE,//"收盘"
                INDEX_TRADE_HIGH,//"最高"
                INDEX_TRADE_LOW,//"最低"
                INDEX_TRADE_VOLUME,//"成交量"
                INDEX_TRADE_TURNOVER,//"成交额"
                INDEX_TRADE_AMPLITUDE,//"振幅"
                INDEX_TRADE_QUOTE_CHANGE,//"涨跌幅"
                INDEX_TRADE_QUOTE_CHANGE_AMOUNT,//"涨跌额"
                INDEX_TRADE_TURNOVER_RATE//"换手率"
        );

        List<String[]> dataRows = new ArrayList<>();
        for (JsonNode item : json) {
            String[] row = item.asText().split(",");
            dataRows.add(row);
        }

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static Table indices2() {
        String url = "https://zhishubao.1234567.com.cn/home/AllIndex";

        Map<String, Object> form = new HashMap<>();
        form.put("marketType", "");
        form.put("dateType", "D");

        String result = HttpRequest.post(url).userAgent(UA).form(form).body();
        Document doc = Jsoup.parse(result);
        Elements eles = doc.select("li.fl");

        List<String> indexNames = new ArrayList<>();
        List<String> indexCodes = new ArrayList<>();
        List<String> fundNums = new ArrayList<>();
        for (Element element : eles) {
            String indexName = element.attr("title");
            indexNames.add(indexName);

            String indexCode = element.selectFirst("div").attr("data-code");
            indexCodes.add(indexCode);

            String fundNum = element.selectFirst("p.view span.red").text();
            fundNums.add(fundNum);
        }

        Table table = Table.create(
                StringColumn.create("index_code", indexCodes),
                StringColumn.create("index_name", indexNames),
                StringColumn.create("fund_num", fundNums).parseDouble()
        );
        return table;
    }

    public static Table tranceIndex(String symbol) {
        String[] ss = symbol.split("\\.");

        String url = "https://zhishubao.1234567.com.cn/home/detail";

        Map<String, Object> params = new HashMap<>();
        params.put("code", ss[0]);

        try {
            String result = HttpRequest.get(url, params).userAgent(UA).body();

            Map<String, ColumnType> columnTypeMap = new HashMap<>();
            columnTypeMap.put("近1周", ColumnType.STRING);
            columnTypeMap.put("近1月", ColumnType.STRING);
            columnTypeMap.put("近3月", ColumnType.STRING);
            columnTypeMap.put("近6月", ColumnType.STRING);
            columnTypeMap.put("今年来", ColumnType.STRING);
            columnTypeMap.put("近1年", ColumnType.STRING);
            columnTypeMap.put("近3年", ColumnType.STRING);
            columnTypeMap.put("成立来", ColumnType.STRING);
            columnTypeMap.put("跟踪误差", ColumnType.STRING);
            columnTypeMap.put("手续费", ColumnType.STRING);

            HtmlReadOptions options = HtmlReadOptions.builderFromString(result).tableIndex(1).columnTypesPartial(columnTypeMap).build();
            Table table = Table.read().usingOptions(options);

            final List<String> fundCodes = new ArrayList<>();
            final List<String> fundNames = new ArrayList<>();
            table.stringColumn("产品名称").forEach(d -> {
                String[] arr = d.split(" ");
                fundCodes.add(arr[1]);
                fundNames.add(arr[0]);
            });
            table.insertColumn(0, StringColumn.create("基金代码", fundCodes));
            table.insertColumn(1, StringColumn.create("基金名称", fundNames));

            table.removeColumns("产品名称", "净值 日增长率", "购买起点", "操作");

            Map<String, String> map = new HashMap<>();
            map.put("基金代码", FUND_CODE);
            map.put("基金名称", FUND_NAME);
            map.put("近1周", FUND_YIELD_LAST_WEEK);
            map.put("近1月", FUND_YIELD_LAST_MONTH);
            map.put("近3月", FUND_YIELD_LAST_THREE_MONTH);
            map.put("近6月", FUND_YIELD_LAST_SIX_MONTH);
            map.put("近1年", FUND_YIELD_LAST_YEAR);
            map.put("近3年", FUND_YIELD_LAST_THREE_YEAR);
            map.put("今年来", FUND_YIELD_THIS_YEAR);
            map.put("成立来", FUND_YIELD_ALL);
            map.put("跟踪误差", FUND_TRACKING_ERROR);
            map.put("手续费", FUND_FEE);

            table = TableUtils.rename(table, map);

            return table;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String transform(String symbol) {
        String[] ss = symbol.split("\\.");
        return String.format("%s.%s", transformM(ss[1]), ss[0]);
    }

    public static String transformM(String market) {
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
        String symbol = "000300.SH";

        Table table = weeklyHistory(symbol);
        System.out.println(table.print());
        System.out.println(table.structure().print());
        System.out.println(table.shape());
    }
}
