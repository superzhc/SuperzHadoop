package com.github.superzhc.fund.akshare;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jdbc.schema.Column;
import com.github.superzhc.tablesaw.utils.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author superz
 * @create 2022/5/16 23:36
 */
public class LiXinger {

    public enum MetricsType {
        // mcw（市值加权）、ew（等权）、ewpvo（正数等权）、avg（平均值）、median（中位数）
        MarketCapitalisationWeight("mcw"), EqualWeight("ew"), PositiveEqualWeight("ewpvo"), Average("avg"), Median("median");

        private String value;

        MetricsType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static String[] all() {
            MetricsType[] types = values();
//            List<String> lst = new ArrayList<>(types.length);
//            for (MetricsType type : types) {
//                lst.add(type.getValue());
//            }
//            return lst;

            int size = types.length;
            String[] ss = new String[size];
            for (int i = 0; i < size; i++) {
                ss[i] = types[i].getValue();
            }
            return ss;
        }
    }

    public enum MetricsName {
        // "pe_ttm", "pb", "ps_ttm", "dyr", "cpc", "cp"
        PE_TTM("pe_ttm") // 滚动市盈率
        , PS_TTM("ps_ttm"), PB("pb"), CurrentPoint("cp"), CurrentPointChange("cpc"), Dividend_rate("dyr");
        private String value;

        MetricsName(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static String[] all() {
            MetricsName[] values = values();
            int size = values.length;
            String[] ss = new String[size];
            for (int i = 0; i < size; i++) {
                ss[i] = values[i].getValue();
            }
            return ss;
        }

        public static String[] commonStructure() {
            return new String[]{PE_TTM.getValue(), PS_TTM.getValue(), PB.getValue()};
        }
    }

    /**
     * 粒度
     */
    public enum Granularity {
        // fs（全部）、y20（20年）、y10（10年）、y5（5年）、y3（3年）
        All("fs"), Years_20("y20"), Years_10("y10"), Years_5("y5"), Years_3("y3");

        private String value;

        Granularity(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static String[] lastThree() {
            return new String[]{Years_10.getValue(), Years_5.getValue(), Years_3.getValue()};
        }
    }

    private String token = null;

    public LiXinger(String mobile, String password) {
        String url = "https://www.lixinger.com/api/account/sign-in/by-account";

        Map<String, Object> payload = new HashMap<>();
        payload.put("accountName", mobile);
        payload.put("password", password);

        String result = HttpRequest.post(url).json(payload).body();
        JsonNode json = JsonUtils.json(result);
        token = json.get("user").get("activeTokens").get(1).asText();
        System.out.println(token);
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
        String[] metricsTypes = MetricsType.all();
        payload.put("metricsTypes", metricsTypes);
        payload.put("metricsNames", MetricsName.all());
        // 接口限制只能选择<b>小于等于 3 个</b>
        String[] granularities = Granularity.lastThree();
        payload.put("granularities", granularities);

        String result = HttpRequest.post(url).cookies(String.format("jwt=%s", token)).json(payload).body();
        JsonNode json = JsonUtils.json(result);
        for (JsonNode item : json) {
            Map<String, String> index = new LinkedHashMap<>();
            String lxrStockId = item.get("stockId").asText();
            index.put("lxr_index_code", lxrStockId);

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

            for (String type : MetricsName.commonStructure()) {
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

    public Table valuation(String symbol, MetricsName metricsName, MetricsType metricsType, Granularity granularity) {
        String url = "https://www.lixinger.com/api/company-collection/price-metrics/get-price-metrics-chart-info";

        Map<String, Object> payload = new HashMap<>();
        payload.put("stockIds", Arrays.asList(symbol));
        payload.put("rightMetricsNames", Arrays.asList(MetricsName.CurrentPoint.getValue()));
        payload.put("leftMetricsNames", Arrays.asList(metricsName.getValue()));
        payload.put("metricsTypes", Arrays.asList(metricsType.getValue()));
        payload.put("granularity", granularity.getValue());

        String result = HttpRequest.post(url).cookies(String.format("jwt=%s", token)).json(payload).body();
        JsonNode json = JsonUtils.json(result);

        List<String[]> dataRows = new ArrayList<>();
        JsonNode priceMetricsList = json.get("priceMetricsList");
        for (JsonNode priceMetrics : priceMetricsList) {
            List<String> dataRow = new ArrayList<>();

            dataRow.add(priceMetrics.get("date").asText());
            dataRow.add(priceMetrics.get("cp").asText());
            dataRow.add(priceMetrics.get(metricsName.getValue()).get(metricsType.getValue()).asText());

            // 当前点位所在位置的百分比
            JsonNode cvposNode = priceMetrics.get("pos").get(metricsName.getValue()).get(metricsType.getValue()).get("cvpos");
            dataRow.add(null == cvposNode ? null : cvposNode.asText());
            // 20%位置的点位
            JsonNode q2vNode = priceMetrics.get("pos").get(metricsName.getValue()).get(metricsType.getValue()).get("q2v");
            dataRow.add(null == q2vNode ? null : q2vNode.asText());
            // 50%位置的点位
            JsonNode q5vNode = priceMetrics.get("pos").get(metricsName.getValue()).get(metricsType.getValue()).get("q5v");
            dataRow.add(null == q5vNode ? null : q5vNode.asText());
            // 80%位置的点位
            JsonNode q8vNode = priceMetrics.get("pos").get(metricsName.getValue()).get(metricsType.getValue()).get("q8v");
            dataRow.add(null == q8vNode ? null : q8vNode.asText());

            dataRows.add(dataRow.toArray(new String[7]));
        }

        List<String> columnNames = Arrays.asList("date", "cp", metricsName.getValue(), "cvpos", "q2v", "q5v", "q8v");
        Table table = TableUtils.build(columnNames, dataRows);
        DateColumn date = table.stringColumn("date").map(
                d -> LocalDateTime.parse(d, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).toLocalDate(),
                name -> DateColumn.create(name));
        table.replaceColumn("date", date);

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
