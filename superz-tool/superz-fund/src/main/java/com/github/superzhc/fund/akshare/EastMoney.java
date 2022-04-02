package com.github.superzhc.fund.akshare;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.tablesaw.read.EmptyReadOptions;
import com.github.superzhc.fund.tablesaw.utils.ColumnUtils;
import com.github.superzhc.fund.tablesaw.utils.JsonUtils;
import com.github.superzhc.fund.tablesaw.utils.ReadOptionsUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.TableBuildingUtils;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/3/29 10:04
 **/
public class EastMoney {
    private static final Logger log = LoggerFactory.getLogger(EastMoney.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        //允许使用未带引号的字段名
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //允许使用单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static class ColumnTypeSetting implements Function<String, Optional<ColumnType>> {

        @Override
        public Optional<ColumnType> apply(String s) {
            ColumnType ct = null;
            if (s.contains("基金代码")) {
                ct = ColumnType.STRING;
            }
            return Optional.ofNullable(ct);
        }
    }

    public static Table funds() {
        String url = "http://fund.eastmoney.com/js/fundcode_search.js";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");

        try {
            String result = HttpRequest.get(url).headers(headers).body();
            String json = result.substring("var r = ".length(), result.length() - 1);
            JsonNode nodes = mapper.readTree(json);

            List<String> columnNames = Arrays.asList("基金代码", "拼音缩写", "基金简称", "基金类型", "拼音全称");

            List<String[]> dataRows = new ArrayList<>();
            for (JsonNode node : nodes) {
                String[] row = new String[node.size()];
                for (int i = 0, len = node.size(); i < len; i++) {
                    row[i] = node.get(i).asText();
                }
                dataRows.add(row);
            }

            Table table = TableBuildingUtils.build(columnNames, dataRows, EmptyReadOptions.builder().build());
            return table;
        } catch (IOException e) {
            log.error("解析失败", e);
            throw new RuntimeException(e);
        }
    }

    public static Table openFundDaily() {
        String url = "http://fund.eastmoney.com/Data/Fund_JJJZ_Data.aspx";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");

        Map<String, String> params = new HashMap<>();
        params.put("t", "1");
        params.put("lx", "1");
        params.put("letter", "");
        params.put("gsid", "");
        params.put("text", "");
        params.put("sort", "zdf,desc");
        params.put("page", "1,20000");
        params.put("dt", "1580914040623");
        params.put("atfc", "");
        params.put("onlySale", "0");

        try {
            String result = HttpRequest.get(url, params, true).headers(headers).body();
            String json = result.substring("var db=".length());
            // 坑~~~
            json = json
                    .replace("[ ,", "[ \"\",").replace("[,", "[ \"\",")
                    .replace(",,", ",\"\",").replace(", ,", ", \"\",")
                    .replace(", ]", "]").replace(",]", "]");
            JsonNode jsonNode = mapper.readTree(json);
            JsonNode datas = jsonNode.get("datas");
            JsonNode showDay = jsonNode.get("showday");

            List<String> columnNames = Arrays.asList(
                    "基金代码",
                    "基金简称",
                    "c1",
                    showDay.get(0).asText() + "-单位净值",
                    showDay.get(0).asText() + "-累计净值",
                    showDay.get(1).asText() + "-单位净值",
                    showDay.get(1).asText() + "-累计净值",
                    "日增长值",
                    "日增长率",
                    "申购状态",
                    "赎回状态",
                    "c2",
                    "c3",
                    "c4",
                    "c5",
                    "c6",
                    "c7",
                    "手续费",
                    "c8",
                    "c9",
                    "c10");

            List<String[]> dataRows = new ArrayList<>();
            for (JsonNode node : datas) {
                String[] row = new String[node.size()];
                for (int i = 0, len = node.size(); i < len; i++) {
                    row[i] = node.get(i).asText();
                }
                dataRows.add(row);
            }

            Table table = TableBuildingUtils.build(columnNames, dataRows, EmptyReadOptions.builder().build());
            table = table.select(
                    "基金代码",
                    "基金简称",
                    showDay.get(0).asText() + "-单位净值",
                    showDay.get(0).asText() + "-累计净值",
                    showDay.get(1).asText() + "-单位净值",
                    showDay.get(1).asText() + "-累计净值",
                    "日增长值",
                    "日增长率",
                    "申购状态",
                    "赎回状态",
                    "手续费"
            );
            return table;
        } catch (JsonProcessingException e) {
            log.error("解析失败", e);
            throw new RuntimeException(e);
        }
    }

//    // 接口已不存在
//    public static Table openFundInfo(String fundCode){
//        String url=String.format("http://fund.eastmoney.com/pingzhongdata/%s.js",fundCode);
//
//        Map<String,String> headers=new HashMap<>();
//        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
//
//        String result=HttpRequest.get(url).headers(headers).body();
//        System.out.println(result);
//
//        return Table.create();
//    }

    public static Table etf() {
        String url = "http://fund.eastmoney.com/cnjy_dwjz.html";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");

        try {
            Document doc = Jsoup.connect(url).headers(headers).get();

            int cursor = 0;
            List<String> columnNames = new ArrayList<>();
            List<String[]> dataRows = new ArrayList<>();

            Elements tables = doc.select("table");
            Element table = tables.get(1);
            Elements rows = table.select("tr");

            // header
            /* 这个自动类型推到列存在问题，手动指明此列的类型 */
            String codeColumnName = rows.get(0).select("td").get(3).text();
            columnNames.add(codeColumnName);
            columnNames.add(rows.get(0).select("td").get(4).text());
            columnNames.add(rows.get(0).select("td").get(5).text());
            String today = rows.get(0).select("td").get(6).text();
            columnNames.add(today + rows.get(1).select("td").get(0).text());
            columnNames.add(today + rows.get(1).select("td").get(1).text());
            String yesterday = rows.get(0).select("td").get(7).text();
            columnNames.add(yesterday + rows.get(1).select("td").get(2).text());
            columnNames.add(yesterday + rows.get(1).select("td").get(3).text());
            columnNames.add(rows.get(0).select("td").get(8).text());
            columnNames.add(rows.get(0).select("td").get(9).text());
            columnNames.add(rows.get(0).select("td").get(10).text());
            columnNames.add(rows.get(0).select("td").get(11).text());

            for (Element row : rows) {
                Elements cells = row.select("td");
                if (cursor > 1) {
                    String[] dataRow = new String[13];
                    for (int i = 0; i < 11; i++) {
                        if (i + 3 == 4) {
                            dataRow[i] = cells.get(i + 3).selectFirst("a").text();
                        } else {
                            dataRow[i] = cells.get(i + 3).text();
                        }
                    }
                    dataRows.add(dataRow);
                }
                cursor++;
            }

            Map<String, ColumnType> columnTypeByName = new HashMap<>();
            columnTypeByName.put(codeColumnName, ColumnType.STRING);
            Table t = TableBuildingUtils.build(columnNames, dataRows, EmptyReadOptions.builder().columnTypesPartial(columnTypeByName).build());
            return t;
        } catch (IOException e) {
            log.error("解析异常", e);
            throw new RuntimeException(e);
        }
    }

    // 估值
    public static Table estimation() {
        String url = "http://api.fund.eastmoney.com/FundGuZhi/GetFundGZList";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        headers.put("Referer", "http://fund.eastmoney.com/");

//        Map<String, Integer> symbolMap = new HashMap<>();
//        symbolMap.put("全部", 1);
//        symbolMap.put("股票型", 2);
//        symbolMap.put("混合型", 3);
//        symbolMap.put("债券型", 4);
//        symbolMap.put("指数型", 5);
//        symbolMap.put("QDII", 6);
//        symbolMap.put("ETF联接", 7);
//        symbolMap.put("LOF", 8);
//        symbolMap.put("场内交易基金", 9);

        Map<String, String> params = new HashMap<>();
        params.put("type", "1");
        params.put("sort", "3");
        params.put("orderType", "desc");
        params.put("canbuy", "0");
        params.put("pageIndex", "1");
        params.put("pageSize", "20000");
        params.put("_", String.valueOf(System.currentTimeMillis()));

        try {
            String result = HttpRequest.get(url, params).headers(headers).body();
            JsonNode json = mapper.readTree(result);

            String valueDay = json.get("Data").get("gzrq").asText();
            String calDay = json.get("Data").get("gxrq").asText();

            JsonNode data = json.get("Data").get("list");
            List<String[]> dataRows = new ArrayList<>();
            for (JsonNode item : data) {
                String[] row = new String[item.size()];

                if (item.isArray()) {
                    for (int i = 0, len = item.size(); i < len; i++) {
                        JsonNode e = item.get(i);
                        String value = null;
                        if (e != null && !"---".equals(e.asText())) {
                            value = e.asText();
                        }
                        row[i] = value;
                    }
                } else {
                    int cursor = 0;
                    Iterator<JsonNode> iterator = item.elements();
                    while (iterator.hasNext()) {
                        JsonNode e = iterator.next();
                        String value = null;
                        if (e != null && !"---".equals(e.asText())) {
                            value = e.asText();
                        }
                        row[cursor++] = value;
                    }
                }
                dataRows.add(row);
            }

            List<String> columnNames = Arrays.asList(
                    "基金代码",
                    "c1",
                    "c2",
                    "c3",
                    "c4",
                    "c5",
                    "基金类型",
                    "c6",
                    "c7",
                    "c8",
                    "c9",
                    "估算日期",
                    "c10",
                    "c11",
                    "c12",
                    "c13",
                    "c14",
                    "c15",
                    "c16",
                    "估算偏差",
                    calDay + "-估算数据-估算值",
                    calDay + "-估算数据-估算增长率",
                    calDay + "-公布数据-日增长率",
                    valueDay + "-单位净值",
                    calDay + "-公布数据-单位净值",
                    "c17",
                    "基金名称",
                    "c18",
                    "c19",
                    "c20"
            );

            Table table = TableBuildingUtils.build(columnNames, dataRows, EmptyReadOptions.builder().columnTypesPartial(new ColumnTypeSetting()).build());
            table = table.select(
                    "基金代码",
                    "基金类型",
                    "估算日期",
                    "估算偏差",
                    calDay + "-估算数据-估算值",
                    calDay + "-估算数据-估算增长率",
                    calDay + "-公布数据-日增长率",
                    valueDay + "-单位净值",
                    calDay + "-公布数据-单位净值",
                    "基金名称"
            );
            return table;
        } catch (JsonProcessingException e) {
            log.error("解析失败", e);
            throw new RuntimeException(e);
        }
    }

    public static Table companies() {
        String url = "http://fund.eastmoney.com/Data/FundRankScale.aspx";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");

        Map<String, String> params = new HashMap<>();
        params.put("_", String.valueOf(System.currentTimeMillis()));

        try {
            String result = HttpRequest.get(url, params).headers(headers).body();
            String json = result.substring("var json=".length());

            List<String> columnNames = ColumnUtils.transform(
                    "uid,company_name,company_fund_date,manager_crew_num,company_boss_name,company_pinyin,others1,manage_scale,company_rank,company_short_name,others2,update_time".split(",")
            );

            JsonNode node = JsonUtils.json(json, "datas");
            List<String[]> dataRows = JsonUtils.extractArrayData(node);

            Table table = TableBuildingUtils.build(columnNames, dataRows, ReadOptionsUtils.empty());
            return table;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        Table table = companies();
        System.out.println(table.print());

        System.out.println(table.structure().printAll());
    }
}
