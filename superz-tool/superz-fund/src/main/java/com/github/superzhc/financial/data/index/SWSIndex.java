package com.github.superzhc.financial.data.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/5/6 19:31
 **/
public class SWSIndex {
    public static Table indexRepresentationSpot() {
        String url = "http://www.swsindex.com/handler.aspx";

        Map<String, Object> params = new HashMap<>();
        params.put("tablename", "swzs");
        params.put("key", "L1");
        params.put("p", "1");
        params.put("where", "L1 in('801001','801002','801003','801005','801300','801901','801903','801905','801250','801260','801270','801280','802613')");
        params.put("orderby", "");
        params.put("fieldlist", "L1,L2,L3,L4,L5,L6,L7,L8,L11");
        params.put("pagecount", "9");
        params.put("timed", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "root");

        List<String> columnNames = Arrays.asList("L1,L2,L3,L4,L5,L6,L7,L8,L11".split(","));

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);
        Table table = TableUtils.build(columnNames, dataRows);
        table.replaceColumn("L2", table.stringColumn("L2").trim().setName("L2"));

        List<String> finalColumnNames = Arrays.asList("指数代码", "指数名称", "昨收盘", "今开盘", "成交额", "最高价", "最低价", "最新价", "成交量");
        table = TableUtils.rename(table, finalColumnNames);

        return table;
    }

    public static void main(String[] args) {
        Table table = indexRepresentationSpot();

        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
