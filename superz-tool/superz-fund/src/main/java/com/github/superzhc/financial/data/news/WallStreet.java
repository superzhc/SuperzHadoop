package com.github.superzhc.financial.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * @author superz
 * @create 2022/4/20 0:27
 */
public class WallStreet {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36";

    public static Table news() {
        return news(null);
    }

    public static Table news(LocalDateTime dt) {
        String url = "https://api-one.wallstcn.com/apiv1/content/lives";

        Map<String, Object> params = new HashMap<>();
        params.put("channel", "global-channel");
        params.put("client", "pc");
        params.put("limit", 100);
        if (null == dt) {
            params.put("first_page", true);
        } else {
            // 时间转换成时间戳（秒）
            params.put("cursor", dt.toEpochSecond(ZoneOffset.of("+8")));
            params.put("first_page", false);
        }
        params.put("accept", "live,vip-live");

        String result = HttpRequest.get(url, params).userAgent(USER_AGENT).body();
        JsonNode json = JsonUtils.json(result, "data", "items");

        List<String> columnNames = //JsonUtils.extractObjectColumnName(json);
                Arrays.asList(
                        "title",
                        "content",
                        //"content_text",
                        "author/display_name",
                        "display_time",
                        "id",
                        "uri"
                );

        List<String[]> dataRows = new ArrayList<>();
        for (JsonNode item : json) {
            int len = columnNames.size();

            String[] row = new String[len];
            for (int i = 0; i < len; i++) {
                String columnName = columnNames.get(i);
                JsonNode node = item.at(String.format("/%s", columnName));
                row[i] = null == node ? null : node.asText();
            }
            dataRows.add(row);
        }

        List<String> columnNames2 = new ArrayList<>();
        for (String columnName : columnNames) {
            columnNames2.add(columnName.replace("/", "_"));
        }

        Table table = TableUtils.build(columnNames2, dataRows);

        return table;
    }

    public static Table hot() {
        String url = "https://api-one-wscn.awtmt.com/apiv1/content/articles/hot";

        Map<String, Object> params = new HashMap<>();
        params.put("period", "all");

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode json = JsonUtils.json(result, "data");

        List<Map<String, Object>> dataRows = new ArrayList<>();
        List<String> tags = Arrays.asList("day_items", "week_items");
        for (String tag : tags) {
            for (JsonNode item : JsonUtils.object(json, tag)) {
                Map<String, Object> dataRow = new LinkedHashMap<>();
                dataRow.put("title", JsonUtils.string(item, "title"));
                dataRow.put("link", JsonUtils.string(item, "uri"));
                dataRow.put("pubDate", JsonUtils.aLong(item, "display_time"));
                dataRows.add(dataRow);
            }
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table liveGlobal() {
        return live("global");
    }

    public static Table liveAStock() {
        return live("a-stock");
    }

    public static Table liveUSStock() {
        return live("us-stock");
    }

    public static Table liveHKStock() {
        return live("hk-stock");
    }

    public static Table liveForex() {
        return live("forex");
    }

    public static Table liveCommodity() {
        return live("commodity");
    }

    public static Table liveFinancing() {
        return live("financing");
    }

    private static Table live(String category) {
        String url = "https://api-one.wallstcn.com/apiv1/content/lives";

        Map<String, Object> params = new HashMap<>();
        params.put("channel", String.format("%s-channel", category));
        params.put("limit", 100);

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode data = JsonUtils.json(result, "data", "items");

        List<Map<String, Object>> dataRows = new ArrayList<>();

        for (JsonNode item : data) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", JsonUtils.string(item, "title"));
            dataRow.put("author", JsonUtils.string(item, "author", "display_name"));
            dataRow.put("content", JsonUtils.string(item, "content"));
            dataRow.put("content2", JsonUtils.string(item, "content_more"));
            dataRow.put("channels", JsonUtils.text(item, "channels"));
            dataRow.put("pubDate", JsonUtils.aLong(item, "display_time"));
            dataRow.put("link", JsonUtils.string(item, "uri"));

            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table latest() {
        return informationFlow("global");
    }

    public static Table shares() {
        return informationFlow("shares");
    }

    public static Table bonds() {
        return informationFlow("bonds");
    }

    public static Table commodities() {
        return informationFlow("commodities");
    }

    public static Table forex() {
        return informationFlow("forex");
    }

    public static Table enterprise() {
        return informationFlow("enterprise");
    }

    public static Table assetManage() {
        return informationFlow("asset-manage");
    }

    public static Table tmt() {
        return informationFlow("tmt");
    }

    public static Table estate() {
        return informationFlow("estate");
    }

    public static Table car() {
        return informationFlow("car");
    }

    public static Table medicine() {
        return informationFlow("medicine");
    }

    private static Table informationFlow(String category) {
        String url = "https://api-one.wallstcn.com/apiv1/content/information-flow";

        Map<String, Object> params = new HashMap<>();
        params.put("channel", String.format("%s-channel", category));
        params.put("accept", "article");
        params.put("limit", 25);

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode data = JsonUtils.json(result, "data", "items");

        List<Map<String, Object>> dataRows = new ArrayList<>();
        for (JsonNode item : data) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", JsonUtils.string(item, "resource", "title"));
            dataRow.put("author", JsonUtils.string(item, "resource", "author", "display_name"));
            dataRow.put("pubDate", JsonUtils.string(item, "resource", "display_time"));
            dataRow.put("content", JsonUtils.string(item, "resource", "content_short"));
            dataRow.put("type", JsonUtils.string(item, "resource_type"));
            dataRow.put("link", JsonUtils.string(item, "resource", "uri"));

            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table = informationFlow("global");
        System.out.println(table.structure().printAll());
        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
