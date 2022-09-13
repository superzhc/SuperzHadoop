package com.github.superzhc.data.stock;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;

import java.util.*;

/**
 * @author superz
 * @create 2022/9/13 11:29
 **/
public class EastMoneyStock {
    public static List<Map<String, Object>> newShares() {
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

        List<Map<String, Object>> dataRows = new ArrayList<>();
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
        for (JsonNode item : json) {
            String str = JsonUtils.string(item);
            String[] arr = str.split(",");

            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("stock_code",arr[0]);
            dataRow.put("stock_name",arr[1]);
            dataRow.put("",arr[2]);
            dataRow.put("",arr[3]);
            dataRow.put("",arr[4]);
            dataRow.put("",arr[5]);
            dataRow.put("",arr[6]);
            dataRow.put("",arr[7]);
            dataRow.put("",arr[8]);
            dataRow.put("",arr[9]);
            dataRow.put("",arr[10]);
            dataRow.put("",arr[11]);
            dataRow.put("",arr[12]);
            dataRow.put("",arr[13]);
            dataRow.put("",arr[14]);
            dataRow.put("",arr[15]);

            dataRows.add(dataRow);
        }
        return dataRows;
    }

    public static void main(String[] args) {
        newShares();
    }
}
