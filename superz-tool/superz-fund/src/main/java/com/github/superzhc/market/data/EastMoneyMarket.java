package com.github.superzhc.market.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;

import java.time.LocalDate;
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
        List<String[]> data = JsonUtils.objectArrayWithKeys(json, columnNames);
        Table table = TableUtils.build(data);
        table.column("f6").setName("成交额");
        table.column("f104").setName("上涨数");
        table.column("f105").setName("下跌数");
        table.column("f106").setName("平盘数");

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

    public static Table hsgtNorth() {
        String url = "http://push2his.eastmoney.com/api/qt/kamt.kline/get";

        Map<String, Object> params = new HashMap<>();
        params.put("fields1", "f1,f3,f5");
        params.put("fields2", "f51,f52,f53,f54");
        params.put("klt", "101");
        params.put("lmt", "5000");
        // params.put("ut", "b2884a393a59ad64002292a3e90d46a5");
        // params.put("cb", "jQuery18305732402561585701_1584961751919");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data");

        Table hk2shT = Table.create(
                DateColumn.create("date")
                , DoubleColumn.create("hk2sh-净流入")
                , DoubleColumn.create("hk2sh-资金余额")
                , DoubleColumn.create("hk2sh-累计净流入")
        );
        JsonNode hk2sh = json.get("hk2sh");
        for (JsonNode item : hk2sh) {
            String str = JsonUtils.string(item);
            String[] arr = str.split(",");
            LocalDate date = LocalDate.parse(arr[0]);
            Double netFlow = Double.parseDouble(arr[1]);
            Double cash = Double.parseDouble(arr[2]);
            Double accFlow = Double.parseDouble(arr[3]);
            hk2shT.dateColumn(0).append(date);
            hk2shT.doubleColumn(1).append(netFlow);
            hk2shT.doubleColumn(2).append(cash);
            hk2shT.doubleColumn(3).append(accFlow);
        }

        Table hk2szT = Table.create(
                DateColumn.create("date")
                , DoubleColumn.create("hk2sz-净流入")
                , DoubleColumn.create("hk2sz-资金余额")
                , DoubleColumn.create("hk2sz-累计净流入")
        );
        JsonNode hk2sz = json.get("hk2sz");
        for (JsonNode item : hk2sz) {
            String str = JsonUtils.string(item);
            String[] arr = str.split(",");
            LocalDate date = LocalDate.parse(arr[0]);
            Double netFlow = Double.parseDouble(arr[1]);
            Double cash = Double.parseDouble(arr[2]);
            Double accFlow = Double.parseDouble(arr[3]);
            hk2szT.dateColumn(0).append(date);
            hk2szT.doubleColumn(1).append(netFlow);
            hk2szT.doubleColumn(2).append(cash);
            hk2szT.doubleColumn(3).append(accFlow);
        }

        Table s2nT = Table.create(
                DateColumn.create("date")
                , DoubleColumn.create("s2n-净流入")
                , DoubleColumn.create("s2n-资金余额")
                , DoubleColumn.create("s2n-累计净流入")
        );
        JsonNode s2n = json.get("s2n");
        for (JsonNode item : s2n) {
            String str = JsonUtils.string(item);
            String[] arr = str.split(",");
            LocalDate date = LocalDate.parse(arr[0]);
            Double netFlow = Double.parseDouble(arr[1]);
            Double cash = Double.parseDouble(arr[2]);
            Double accFlow = Double.parseDouble(arr[3]);
            s2nT.dateColumn(0).append(date);
            s2nT.doubleColumn(1).append(netFlow);
            s2nT.doubleColumn(2).append(cash);
            s2nT.doubleColumn(3).append(accFlow);
        }

        Table table = s2nT.joinOn("date").leftOuter(hk2shT, hk2szT);
        return table;
    }

    public static Table hsgtSouth() {
        String url = "http://push2his.eastmoney.com/api/qt/kamt.kline/get";

        Map<String, Object> params = new HashMap<>();
        params.put("fields1", "f2,f4,f6");
        params.put("fields2", "f51,f52,f53,f54");
        params.put("klt", "101");
        params.put("lmt", "5000");
        // params.put("ut", "b2884a393a59ad64002292a3e90d46a5");
        // params.put("cb", "jQuery18305732402561585701_1584961751919");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data");

        Table sh2hkT = Table.create(
                DateColumn.create("date")
                , DoubleColumn.create("sh2hk-净流入")
                , DoubleColumn.create("sh2hk-资金余额")
                , DoubleColumn.create("sh2hk-累计净流入")
        );
        JsonNode sh2hk = json.get("sh2hk");
        for (JsonNode item : sh2hk) {
            String str = JsonUtils.string(item);
            String[] arr = str.split(",");
            LocalDate date = LocalDate.parse(arr[0]);
            Double netFlow = Double.parseDouble(arr[1]);
            Double cash = Double.parseDouble(arr[2]);
            Double accFlow = Double.parseDouble(arr[3]);
            sh2hkT.dateColumn(0).append(date);
            sh2hkT.doubleColumn(1).append(netFlow);
            sh2hkT.doubleColumn(2).append(cash);
            sh2hkT.doubleColumn(3).append(accFlow);
        }

        Table sz2hkT = Table.create(
                DateColumn.create("date")
                , DoubleColumn.create("sz2hk-净流入")
                , DoubleColumn.create("sz2hk-资金余额")
                , DoubleColumn.create("sz2hk-累计净流入")
        );
        JsonNode sz2hk = json.get("sz2hk");
        for (JsonNode item : sz2hk) {
            String str = JsonUtils.string(item);
            String[] arr = str.split(",");
            LocalDate date = LocalDate.parse(arr[0]);
            Double netFlow = Double.parseDouble(arr[1]);
            Double cash = Double.parseDouble(arr[2]);
            Double accFlow = Double.parseDouble(arr[3]);
            sz2hkT.dateColumn(0).append(date);
            sz2hkT.doubleColumn(1).append(netFlow);
            sz2hkT.doubleColumn(2).append(cash);
            sz2hkT.doubleColumn(3).append(accFlow);
        }

        Table n2sT = Table.create(
                DateColumn.create("date")
                , DoubleColumn.create("n2s-净流入")
                , DoubleColumn.create("n2s-资金余额")
                , DoubleColumn.create("n2s-累计净流入")
        );
        JsonNode n2s = json.get("n2s");
        for (JsonNode item : n2s) {
            String str = JsonUtils.string(item);
            String[] arr = str.split(",");
            LocalDate date = LocalDate.parse(arr[0]);
            Double netFlow = Double.parseDouble(arr[1]);
            Double cash = Double.parseDouble(arr[2]);
            Double accFlow = Double.parseDouble(arr[3]);
            n2sT.dateColumn(0).append(date);
            n2sT.doubleColumn(1).append(netFlow);
            n2sT.doubleColumn(2).append(cash);
            n2sT.doubleColumn(3).append(accFlow);
        }

        Table table = n2sT.joinOn("date").leftOuter(sh2hkT, sz2hkT);
        return table;
    }

    public static Table industry() {
        String indicator = "1";
        String indicatorField1 = "f62";
        String indicatorField2 = "f12,f14,f2,f3,f62,f184,f66,f69,f72,f75,f78,f81,f84,f87,f204,f205,f124";

        Table table = industry0(indicator, indicatorField1, indicatorField2);
        return table;
    }

    public static Table industry5day(){
        String indicator = "5";
        String indicatorField1 = "f164";
        String indicatorField2 = "f12,f14,f2,f109,f164,f165,f166,f167,f168,f169,f170,f171,f172,f173,f257,f258,f124";

        Table table = industry0(indicator, indicatorField1, indicatorField2);
        return table;
    }

    public static Table industry10day(){
        String indicator = "10";
        String indicatorField1 = "f174";
        String indicatorField2 = "f12,f14,f2,f160,f174,f175,f176,f177,f178,f179,f180,f181,f182,f183,f260,f261,f124";

        Table table = industry0(indicator, indicatorField1, indicatorField2);
        return table;
    }

    private static Table industry0(String indicator, String indicatorField1, String indicatorField2) {
        // {"行业资金流": "2", "概念资金流": "3", "地域资金流": "1"}
        String sectorType = "2";

        String url = "http://push2.eastmoney.com/api/qt/clist/get";

        Map<String, Object> params = new HashMap<>();
        params.put("pn", "1");
        params.put("pz", "5000");
        params.put("po", "1");
        params.put("np", "1");
        // params.put("ut", "b2884a393a59ad64002292a3e90d46a5");
        // params.put("fltt", "2");
        // params.put("invt", "2");
        // params.put("fid0", indicatorField1);
        params.put("fid", indicatorField1);
        params.put("fs", "m:90+t:" + sectorType);
        params.put("stat", indicator);
        params.put("fields", indicatorField2);
        params.put("rt", "52975239");
        // params.put("cb", "jQuery18308357908311220152_1589256588824");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).userAgent(UA).body();
        JsonNode json = JsonUtils.json(result, "data", "diff");

        List<String[]> data = JsonUtils.objectArrayWithKeys(json, indicatorField2.split(","));

        Table table = TableUtils.build(data);
        return table;
    }

    public static void main(String[] args) {
        Table table = Table.create();

//        Table table = concept();

        table = industry();

        System.out.println(table.print());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
    }
}
