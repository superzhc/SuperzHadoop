package com.github.superzhc.fund.data.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.tablesaw.utils.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深证证券交易所-指数
 *
 * @author superz
 * @create 2022/5/10 19:47
 */
public class SZSE {
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

        return table;
    }

    public static void main(String[] args) {
        Table table = indices();
        System.out.println(table.print());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
    }
}
