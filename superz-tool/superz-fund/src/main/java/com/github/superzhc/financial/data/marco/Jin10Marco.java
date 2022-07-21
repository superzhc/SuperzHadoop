package com.github.superzhc.financial.data.marco;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.format.LogFormat;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.superzhc.common.HttpConstant.UA;

/**
 * @author superz
 * @create 2022/7/5 15:43
 **/
public class Jin10Marco {
    private static Map<String, String> headers = initHeaders();

    private static Map<String, String> initHeaders() {
        Map<String, String> map = new HashMap<>();
        map.put("user-agent", UA);
        map.put("x-app-id", "rU6QIu7JHe2gOUeR");
        map.put("x-version", "1.0.0");
        return map;
    }


    public static void main(String[] args) {
//        String url = "https://datacenter-api.jin10.com/reports/list_v2";
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("max_date", "");
//        params.put("category", "ec");
//        params.put("attr_id", "57");
//        params.put("_", System.currentTimeMillis());
//
//
//        String resp = HttpRequest.get(url, params).headers(headers).body();
//        JsonNode json = JsonUtils.json(resp, "data");
//
//
//        String[] columnNames = JsonUtils.mapOneArray(json, "name", "keys");
//
//
//        List<String[]> dataRows = Arrays.asList(JsonUtils.arrayArray(json, "values"));
//
//        Table table = TableUtils.build(columnNames, dataRows);
//
//        System.out.println(table.print());
//        System.out.println(table.shape());

        String url = LogFormat.format("https://cdn.jin10.com/dc/reports/dc_chinese_gdp_yoy_all.js?v={}&_={}", System.currentTimeMillis(), System.currentTimeMillis() + 90);
        String resp = HttpRequest.get(url).body();
        System.out.println(resp);
    }
}
