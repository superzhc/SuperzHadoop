package com.github.superzhc.market.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.tablesaw.utils.ColumnUtils;
import com.github.superzhc.common.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.*;

import static com.github.superzhc.common.HttpConstant.UA;

/**
 * @author superz
 * @create 2022/5/8 10:44
 **/
public class EastMoneyMarket {
    public static Table market() {
        String url = "https://push2.eastmoney.com/api/qt/ulist.np/get";

        String fields = "f1,f2,f3,f4,f6,f12,f13,f104,f105,f106";
        Map<String, Object> params = new HashMap<>();
        params.put("fltt", 2);
        params.put("secids", "1.000001,0.399001");
        params.put("fields", fields);
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data", "diff");

        List<String> columnNames = Arrays.asList(fields.split(","));
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);
        Table table = TableUtils.build(columnNames, dataRows);
        table.column("f6").setName("成交额");
        table.column("f104").setName("上涨数");
        table.column("f105").setName("下跌数");
        table.column("f106").setName("平盘数");

        return table;
    }

    // 行业板块
    public static Table industry() {
        String url = "http://push2.eastmoney.com/api/qt/clist/get";

        String fields = "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18,f20,f21,f22,f23,f24,f25,f26,f33,f62,f104,f105,f106,f107,f115,f124,f128,f136,f140,f141,f152,f207,f208,f209,f222";

        Map<String, Object> params = new HashMap<>();
        params.put("pn", 1);
        params.put("pz", 2000);
        params.put("po", 1);
        params.put("np", 1);
        params.put("fltt", 2);
        params.put("invt", 2);
        params.put("fields", fields);
        params.put("fid", "f3");
        params.put("fs", "m:90+t:2");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data", "diff");

        List<String> columnNames = Arrays.asList(fields.split(","));
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        List<String> columnNames2 = Arrays.asList(
                "f1,最新价,涨跌幅,涨跌额,f5,f6,f7,换手率,f9,f10,f11,板块代码,f13,板块名称,f15,f16,f17,f18,总市值,f21,f22,f23,f24,f25,f26,f33,f62,上涨家数,下跌家数,平盘家数,f107,f115,f124,f128,f136,f140,f141,f152,f207,f208,f209,f222".split(",")
        );

        Table table = TableUtils.build(columnNames2, dataRows);
        return table;
    }

    // 概念板块
    public static Table concept() {
        String url = "http://push2.eastmoney.com/api/qt/clist/get";

        String fields = "f2,f3,f4,f8,f11,f12,f14,f15,f16,f17,f18,f20,f21,f22,f24,f25,f33,f62,f104,f105,f107,f124,f128,f136,f140,f141";

        Map<String, Object> params = new HashMap<>();
        params.put("pn", "1");
        params.put("pz", "2000");
        params.put("po", "1");
        params.put("np", "1");
        //params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
        params.put("fltt", "2");
        params.put("invt", "2");
        params.put("fid", "f3");
        params.put("fs", "m:90 t:3 f:!50");
        params.put("fields", fields);
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data", "diff");

        List<String> columnNames = Arrays.asList(fields.split(","));
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        List<String> columnNames2 = ColumnUtils.transform(
                "最新价,涨跌幅,涨跌额,换手率,f11,板块代码,板块名称,f15,f16,f17,f18,总市值,f21,f22,f24,f25,f33,f62,上涨家数,下跌家数,f107,f124,领涨股票,f136,f140,领涨股票-涨跌幅".split(",")
        );

        Table table = TableUtils.build(columnNames2, dataRows);
        return table;
    }

    public static Table test() {
        List<String> columnNames = Arrays.asList(
                "日期",
                "主力净流入-净额",
                "小单净流入-净额",
                "中单净流入-净额",
                "大单净流入-净额",
                "超大单净流入-净额",
                "主力净流入-净占比",
                "小单净流入-净占比",
                "中单净流入-净占比",
                "大单净流入-净占比",
                "超大单净流入-净占比",
                "上证-收盘价",
                "上证-涨跌幅",
                "深证-收盘价",
                "深证-涨跌幅"
        );

        String url = "http://push2his.eastmoney.com/api/qt/stock/fflow/daykline/get";

        Map<String, Object> params = new HashMap<>();
        params.put("lmt", "0");
        params.put("klt", "101");
        params.put("secid", "1.000001");
        params.put("secid2", "0.399001");
        params.put("fields1", "f1,f2,f3,f7");
        params.put("fields2", "f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61,f62,f63,f64,f65");
        //params.put( "ut", "b2884a393a59ad64002292a3e90d46a5");
//        params.put("cb", "jQuery183003743205523325188_1589197499471");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(UA).body();
        JsonNode json = JsonUtils.json(result, "data", "klines");

        List<String[]> dataRows = new ArrayList<>();
        for (JsonNode line : json) {
            String[] row = JsonUtils.string(line).split(",");
            dataRows.add(row);
        }

        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table = concept();
        System.out.println(table.print());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
    }
}
