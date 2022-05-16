package com.github.superzhc.fund.akshare;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.tablesaw.utils.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import jdk.nashorn.internal.parser.Token;
import tech.tablesaw.api.Table;

import java.util.*;

/**
 * @author superz
 * @create 2022/5/16 23:36
 */
public class LiXinger {
    private String token = null;

    public LiXinger(String mobile, String password) {
        String url = "https://www.lixinger.com/api/account/sign-in/by-account";

        Map<String, Object> payload = new HashMap<>();
        payload.put("accountName", mobile);
        payload.put("password", password);

        String result = HttpRequest.post(url).json(payload).body();
        JsonNode json = JsonUtils.json(result);
        token = json.get("user").get("activeTokens").get(1).asText();
    }

    public LiXinger(String token) {
        this.token = token;
    }

    public Table indices() {
        String url = "https://www.lixinger.com/api/company-collection/fs-metrics/indices/latest";

        Map<String, Object> payload = new HashMap<>();
        payload.put("series", "all");
        payload.put("source", "all");
        payload.put("metricsNames", new String[]{"q.m.roe.t"});
        payload.put("dateStrs", new String[]{"2022-03-31", "2021-12-31", "2021-03-31"});

        String result = HttpRequest.post(url).cookies(String.format("jwt=%s", token)).json(payload).body();
        System.out.println(result);
        return Table.create();
    }

    public Table indices2() {
        Map<String, Map<String, String>> map = new HashMap<>();

        List<String> columnNames = null;

        String url = "https://www.lixinger.com/api/company-collection/price-metrics/indices/latest";

        Map<String, Object> payload = new HashMap<>();
        payload.put("series", "all");
        payload.put("source", "all");
        // 计算方式：mcw（市值加权）、ew（等权）、ewpvo（正数等权）、avg（平均值）、median（中位数）
        String[] metricsTypes = new String[]{"mcw", "ew", "ewpvo", "avg", "median"};
        payload.put("metricsTypes", metricsTypes);
        payload.put("metricsNames", new String[]{"pe_ttm", "pb", "ps_ttm", "dyr", "cpc", "cp"});
        // 可选取的值有 fs（全部）、y20（20年）、y10（10年）、y5（5年）、y3（3年），但接口限制只能选择<b>小于等于 3 个</b>
        String[] granularities = new String[]{"y10", "y5", "y3"};
        payload.put("granularities", granularities);

        String result = HttpRequest.post(url).cookies(String.format("jwt=%s", token)).json(payload).body();
        JsonNode json = JsonUtils.json(result);
        for (JsonNode item : json) {
            Map<String, String> index = new LinkedHashMap<>();
            String lxrStockId = item.get("stockId").asText();

            JsonNode stock = item.get("stock");
            index.put("index_name", stock.get("name").asText());
            index.put("index_code", stock.get("stockCode").asText());
            index.put("index_type", null == stock.get("series") ? null : stock.get("series").asText());
            index.put("market", stock.get("source").asText());
            index.put("exchange", stock.get("exchange").asText());
            index.put("publish_date", stock.get("launchDate").asText());
            index.put("stocks_num", stock.get("stocksNum").asText());

            JsonNode pm = item.get("pm");
            index.put("date", pm.get("date").asText());
            index.put("current", null == pm.get("cp") ? null : pm.get("cp").asText());
            index.put("change", null == pm.get("cpc") ? null : pm.get("cpc").asText());
            index.put("dividend_rate", pm.get("dyr").asText());//股息率

            for (String type : new String[]{"pb", "pe_ttm", "ps_ttm"}) {
                for (String year : granularities) {
                    for (String method : metricsTypes) {
                        JsonNode node = pm.get(type).get(year).get("mcw");
                        index.put(String.format("%s_%s_%s", type, method, year), node.get("cv").asText());
                        index.put(String.format("%s_%s_%s_pos", type, method, year), node.get("cvpos").asText());
                        index.put(String.format("%s_%s_%s_max", type, method, year), node.get("maxv").asText());
                        index.put(String.format("%s_%s_%s_min", type, method, year), node.get("minv").asText());
                        index.put(String.format("%s_%s_%s_20", type, method, year), node.get("q2v").asText());
                        index.put(String.format("%s_%s_%s_50", type, method, year), node.get("q5v").asText());
                        index.put(String.format("%s_%s_%s_80", type, method, year), node.get("q8v").asText());
                    }
                }
            }

            if (null == columnNames) {
                columnNames = new ArrayList<>(index.keySet());
            }

            map.put(lxrStockId, index);
        }

        List<String[]> dataRows = new ArrayList<>(map.size());
        for (Map<String, String> value : map.values()) {
            String[] row = new String[value.size()];
            for (int i = 0, len = columnNames.size(); i < len; i++) {
                row[i] = value.get(columnNames.get(i));
            }
            dataRows.add(row);
        }

        Table table = TableUtils.build(columnNames, dataRows);

        return table;
    }

    public static void main(String[] args) {
        LiXinger liXinger = new LiXinger("188xxx", "xxxx");
        Table table = liXinger.indices2();
        System.out.println(table.print());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
    }
}
