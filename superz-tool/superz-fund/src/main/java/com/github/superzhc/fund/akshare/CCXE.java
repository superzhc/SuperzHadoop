package com.github.superzhc.fund.akshare;

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
 * 参考：https://github.com/akfamily/akshare/blob/master/akshare/index/index_cx.py
 *
 * @author superz
 * @create 2022/4/7 16:31
 **/
public class CCXE {

    public static Table com() {
        Table table = execute("com");
        table = TableUtils.rename(table, "data", "综合PMI");
        return table;
    }

    public static Table man() {
        Table table = execute("man");
        table = TableUtils.rename(table, "data", "制造业PMI");
        return table;
    }

    public static Table ser() {
        Table table = execute("ser");
        table = TableUtils.rename(table, "data", "服务业PMI");
        return table;
    }

    public static Table dei() {
        Table table = execute("dei");
        table = TableUtils.rename(table, "data", "数字经济指数");
        return table;
    }

    public static Table ii() {
        Table table = execute("ii");
        table = TableUtils.rename(table, "data", "产业指数");
        return table;
    }

    public static Table si() {
        Table table = execute("si");
        table = TableUtils.rename(table, "data", "溢出指数");
        return table;
    }

    public static Table fi() {
        Table table = execute("fi");
        table = TableUtils.rename(table, "data", "融合指数");
        return table;
    }

    public static Table bi() {
        Table table = execute("bi");
        table = TableUtils.rename(table, "data", "基础指数");
        return table;
    }

    public static Table nei() {
        Table table = execute("nei");
        table = TableUtils.rename(table, "data", "中国新经济指数");
        return table;
    }

    public static Table li() {
        Table table = execute("li");
        table = TableUtils.rename(table, "data", "劳动力投入指数");
        return table;
    }

    public static Table ci() {
        Table table = execute("ci");
        table = TableUtils.rename(table, "data", "资本投入指数");
        return table;
    }

    public static Table ti() {
        Table table = execute("ti");
        table = TableUtils.rename(table, "data", "科研投入指数");
        return table;
    }

    public static Table neaw() {
        Table table = execute("neaw");
        table = TableUtils.rename(table, "data", "新经济行业入职平均工资水平");
        return table;
    }

    public static Table awpr() {
        Table table = execute("awpr");
        table = TableUtils.rename(table, "data", "新经济入职工资溢价水平");
        return table;
    }

    private static Table execute(String type) {
        String url = "https://s.ccxe.com.cn/api/index/pro/cxIndexTrendInfo";

        Map<String, String> params = new HashMap<>();
        params.put("type", type);

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data");

        List<String> columnNames = Arrays.asList("time", "data", "changeRate");
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        table = TableUtils.timestamp2Date(table, "time");
        return table;
    }

    public static Table cci() {
        String url = "https://s.ccxe.com.cn/api/index/pro/cxIndexTrendInfo";

        Map<String, String> params = new HashMap<>();
        params.put("type", "cci");
        params.put("code", "1000050");
        params.put("month", "-1");

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data");

        List<String> columnNames = Arrays.asList("time", "data", "changeRate");
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);
        table = TableUtils.timestamp2Date(table, "time");
        table = TableUtils.rename(table, "data", "大宗商品指数");
        return table;
    }

    public static void main(String[] args) {
        Table table = cci();
        System.out.println(table.print());
    }
}
