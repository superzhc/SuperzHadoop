package com.github.superzhc.fund.akshare;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.common.http.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.TableBuildingUtils;
import tech.tablesaw.io.html.HtmlReadOptions;
import tech.tablesaw.io.json.JsonReadOptions;

import java.io.IOException;
import java.util.*;

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

            Table table = TableBuildingUtils.build(columnNames, dataRows, JsonReadOptions.builderFromString(json).build());
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
                    "d1",
                    showDay.get(0).asText() + "-单位净值",
                    showDay.get(0).asText() + "-累计净值",
                    showDay.get(1).asText() + "-单位净值",
                    showDay.get(1).asText() + "-累计净值",
                    "日增长值",
                    "日增长率",
                    "申购状态",
                    "赎回状态",
                    "d2",
                    "d3",
                    "d4",
                    "d5",
                    "d6",
                    "d7",
                    "手续费",
                    "d8",
                    "d9",
                    "d10");

            List<String[]> dataRows = new ArrayList<>();
            for (JsonNode node : datas) {
                String[] row = new String[node.size()];
                for (int i = 0, len = node.size(); i < len; i++) {
                    row[i] = node.get(i).asText();
                }
                dataRows.add(row);
            }

            Table table = TableBuildingUtils.build(columnNames, dataRows, JsonReadOptions.builderFromString(json).build());
            table.removeColumns("d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8", "d9", "d10");
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

    public static Table e() {
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
            columnNames.add(rows.get(0).select("td").get(3).text());
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

            Table t = TableBuildingUtils.build(columnNames, dataRows, HtmlReadOptions.builderFromUrl(url).build());
            return t;
        } catch (IOException e) {
            log.error("解析异常", e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Table table = e();
        System.out.println(table.print());
    }
}
