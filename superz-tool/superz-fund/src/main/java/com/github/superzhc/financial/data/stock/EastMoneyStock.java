package com.github.superzhc.financial.data.stock;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.ColumnUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/6/10 13:45
 **/
public class EastMoneyStock {
    public static Table newShares() {
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
        List<String> columnNames = ColumnUtils.transform("_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_");
        List<String[]> dataRows = new ArrayList<>();
        for (JsonNode item : json) {
            String str = JsonUtils.string(item);
            dataRows.add(str.split(",", -1));
        }

        Table table = TableUtils.build(columnNames, dataRows);
        return table;
    }

    public static void main(String[] args) {

        Table table = newShares();

        System.out.println(table.print());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
    }
}
