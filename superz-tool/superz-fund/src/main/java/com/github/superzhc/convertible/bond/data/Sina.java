package com.github.superzhc.convertible.bond.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.fund.common.HttpConstant;
import com.github.superzhc.fund.tablesaw.utils.JsonUtils;
import com.github.superzhc.fund.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2022/4/28 17:52
 **/
public class Sina {
    public static Table convertibleBond() {
        String countUrl = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeStockCountSimple";
        Map<String, String> countParams = new HashMap<>();
        countParams.put("node", "hskzz_z");
        String countResult = HttpRequest.get(countUrl, countParams).userAgent(HttpConstant.UA).body();
        System.out.println(countResult);
        int count = Integer.valueOf(countResult.substring(1, countResult.length() - 1));

        String url = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeDataSimple";

        Map<String, Object> params = new HashMap<>();
        params.put("page", "1");
        params.put("num", count);
        params.put("sort", "symbol");
        params.put("asc", "1");
        params.put("node", "hskzz_z");
        params.put("_s_r_a", "page");

        String result = HttpRequest.get(url, params).userAgent(HttpConstant.UA).body();
        JsonNode json = JsonUtils.json(result);

        List<String> columnNames = JsonUtils.extractObjectColumnName(json);

        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static void main(String[] args) {
        Table table = convertibleBond();

        System.out.println(table.print());
        System.out.println(table.structure().printAll());
        System.out.println(table.shape());
    }
}
