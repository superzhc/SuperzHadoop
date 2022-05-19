package com.github.superzhc.fund.data.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.JsonUtils;
import com.github.superzhc.tablesaw.utils.ReadOptionsUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.TableBuildingUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 深证证券交易所-指数
 *
 * @author superz
 * @create 2022/5/10 19:47
 */
public class SZSEIndex {
    public static Table indices() {
        String url = "https://www.szse.cn/api/report/ShowReport/data";

        Map<String, Object> params = new HashMap<>();
        params.put("SHOWTYPE", "JSON");
        params.put("CATALOGID", "1812_zs");
        params.put("PAGENO", 1);
        params.put("random", String.format("0.%d", System.currentTimeMillis()));

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result).get(0);

        int pageCount = json.get("metadata").get("pagecount").asInt();

        List<String> columnNames = Arrays.asList("zsdm", "zsmc", "jrnew", "jrzs", "qsrnew");
        List<String[]> dataRows = JsonUtils.extractObjectData(json.get("data"), columnNames);
        Table table = TableUtils.build(columnNames, dataRows);

        for (int i = 2; i <= pageCount; i++) {
            params.put("PAGENO", i);
            params.put("random", String.format("0.%d", System.currentTimeMillis()));

            result = HttpRequest.get(url, params).body();
            JsonNode data = JsonUtils.json(result).get(0).get("data");

            List<String[]> dataRows2 = JsonUtils.extractObjectData(data, columnNames);
            Table t2 = TableUtils.build(columnNames, dataRows2);
            table.append(t2);
        }

        Function<String, String> extraFunc = new Function<String, String>() {
            Pattern pattern = Pattern.compile("<u>(.*?)</u>");

            @Override
            public String apply(String str) {
                Matcher matcher = pattern.matcher(str);
                if (matcher.find()) {
                    return matcher.group(1);
                } else {
                    return str;
                }
            }
        };

        Pattern pattern = Pattern.compile("<u>(.*?)</u>");

        StringColumn codeColumn = StringColumn.create("code");
        StringColumn nameColumn = StringColumn.create("name");
        for (int i = 0, len = table.rowCount(); i < len; i++) {
            String zsdm = table.getString(i, "zsdm");
            Matcher m1 = pattern.matcher(zsdm);
            codeColumn.append(m1.find() ? m1.group(1) : zsdm);

            String zsmc = table.getString(i, "zsmc");
            Matcher m2 = pattern.matcher(zsmc);
            nameColumn.append(m2.find() ? m2.group(1) : zsmc);
        }

        table.replaceColumn("zsdm", codeColumn);
        table.replaceColumn("zsmc", nameColumn);

        return table;
    }

    public static Table test2(String symbol) {
        String url = "https://www.szse.cn/api/report/ShowReport/data";

        Map<String, Object> params = new HashMap<>();
        params.put("SHOWTYPE", "JSON");
        params.put("CATALOGID", "1747_zs");
        params.put("ZSDM", transform(symbol));
        params.put("PAGENO", 1);

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result).get(0);

        int pageCount = json.get("metadata").get("pagecount").asInt();

        List<String> columnNames = Arrays.asList("zqdm", "zqjc", "zgb", "ltgb", "hylb", "nrzsjs");
        Map<String, ColumnType> columnTypeMap = new HashMap<>();
        columnTypeMap.put("zqdm", ColumnType.STRING);
        columnTypeMap.put("zgb", ColumnType.STRING);
        columnTypeMap.put("ltgb", ColumnType.STRING);
        columnTypeMap.put("hylb", ColumnType.STRING);
        columnTypeMap.put("nrzsjs", ColumnType.STRING);

        List<String[]> dataRows = JsonUtils.extractObjectData(json.get("data"), columnNames);
        Table table = TableBuildingUtils.build(columnNames, dataRows, ReadOptionsUtils.columnTypeByName(columnTypeMap));

        for (int i = 2; i <= pageCount; i++) {
            params.put("PAGENO", i);

            result = HttpRequest.get(url, params).body();
            JsonNode data = JsonUtils.json(result).get(0).get("data");

            List<String[]> dataRows2 = JsonUtils.extractObjectData(data, columnNames);
            Table t2 = TableBuildingUtils.build(columnNames, dataRows2, ReadOptionsUtils.columnTypeByName(columnTypeMap));
            table.append(t2);
        }

        return table;
    }

    private static String transform(String symbol) {
        String[] ss = symbol.split("\\.");
        return ss[0];
    }

    public static void main(String[] args) {
        Table table = test2("399001.SZ");
        System.out.println(table.print());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
    }
}
