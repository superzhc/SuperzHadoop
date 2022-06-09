package com.github.superzhc.fund.akshare;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.script.ScriptUtils;
import com.github.superzhc.tablesaw.functions.DateFunctions;
import com.github.superzhc.tablesaw.functions.LongFunctions;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import javax.script.ScriptEngine;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/6/7 19:04
 **/
public class LeGuLeGu {
    public static Table indicator(String symbol) {
        String url = String.format("https://www.legulegu.com/s/base-info/%s", transform(symbol));
        String result = HttpRequest.get(url).body();
        JsonNode json = JsonUtils.json(result, "data");

        List<String> columnNames = JsonUtils.list(json, "fields");
        List<String[]> dataRows = JsonUtils.arrayArray(json, "items");

        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    public static Table valuation(String symbol) {
        ScriptEngine engine = ScriptUtils.JSEngine();
        ScriptUtils.load(engine, LeGuLeGu.class.getClassLoader().getResource("js/legulegu.js").getPath());
        String token = ScriptUtils.call(engine, "hex", LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        token = token.toLowerCase();

        String url = "https://legulegu.com/api/stockdata/index-basic";

        Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        if ("sh".equalsIgnoreCase(symbol)) {
            params.put("indexCode", 1);
        } else if ("sz".equalsIgnoreCase(symbol)) {
            params.put("indexCode", 2);
        } else if ("cy".equalsIgnoreCase(symbol)) {
            params.put("indexCode", 4);
        } else if ("kc".equalsIgnoreCase(symbol)) {
            params.put("indexCode", 7);
        } else {
            params.put("indexCode", symbol);
        }

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data");

        List<String[]> data = JsonUtils.objectArray(json);
        List<String> columnNames = Arrays.asList(data.remove(0));
        Table table = TableUtils.build(columnNames, data);

        table.replaceColumn("date", DateFunctions.long2Date(table.longColumn("date")).setName("date"));
        table.removeColumns("id","industryCode");

        return table;
    }

    private static String transform(String symbol) {
        String[] ss = symbol.split("\\.");
        return ss[0];
    }

    public static void main(String[] args) throws Exception {
        String str = "000300.SH";
        str="000905.SH";
        Table table = valuation(str);
        System.out.println(table.print());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
    }
}
