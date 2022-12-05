package com.github.superzhc.data.mixture;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.MapUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Stream;


/**
 * @author superz
 * @create 2022/4/20 0:27
 */
public class WallStreet {
    private static final String UA_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36";

    public static List<Map<String, Object>> news() {
        return news(null);
    }

    public static List<Map<String, Object>> news(LocalDateTime dt) {
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

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode json = JsonUtils.json(result, "data", "items");

        Map<String, Object>[] dataRow = JsonUtils.newObjectArray(json);
        return Arrays.asList(dataRow);
    }

    public static List<Map<String, Object>> hot() {
        String url = "https://api-one-wscn.awtmt.com/apiv1/content/articles/hot";

        Map<String, Object> params = new HashMap<>();
        params.put("period", "all");

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode json = JsonUtils.json(result, "data");

        List<Map<String, Object>> dataRows = new ArrayList<>();
        dataRows.addAll(Arrays.asList(MapUtils.constant("type", "day_items", JsonUtils.newObjectArray(json, "day_items"))));
        dataRows.addAll(Arrays.asList(MapUtils.constant("type", "week_items", JsonUtils.newObjectArray(json, "week_items"))));

        return dataRows;
    }

    public static List<Map<String, Object>> liveGlobal() {
        return live("global");
    }

    public static List<Map<String, Object>> liveAStock() {
        return live("a-stock");
    }

    public static List<Map<String, Object>> liveUSStock() {
        return live("us-stock");
    }

    public static List<Map<String, Object>> liveHKStock() {
        return live("hk-stock");
    }

    public static List<Map<String, Object>> liveForex() {
        return live("forex");
    }

    public static List<Map<String, Object>> liveCommodity() {
        return live("commodity");
    }

    public static List<Map<String, Object>> liveFinancing() {
        return live("financing");
    }

    private static List<Map<String, Object>> live(String category) {
        String url = "https://api-one.wallstcn.com/apiv1/content/lives";

        Map<String, Object> params = new HashMap<>();
        params.put("channel", String.format("%s-channel", category));
        params.put("limit", 100);

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode data = JsonUtils.json(result, "data", "items");

        List<Map<String, Object>> dataRows = new ArrayList<>();

        for (JsonNode item : data) {
            Map<String, Object> dataRow = JsonUtils.map(item);
            MapUtils.removeKeys(dataRow, "ban_comment", "channels", "comment_count", "cover_images", "global_channel_name");

//            Map<String, Object> dataRow = new LinkedHashMap<>();
//            dataRow.put("title", JsonUtils.string(item, "title"));
//            dataRow.put("author", JsonUtils.string(item, "author", "display_name"));
//            dataRow.put("content", JsonUtils.string(item, "content"));
//            dataRow.put("content2", JsonUtils.string(item, "content_more"));
//            dataRow.put("channels", JsonUtils.text(item, "channels"));
//            dataRow.put("pubDate", JsonUtils.aLong(item, "display_time"));
//            dataRow.put("link", JsonUtils.string(item, "uri"));

            dataRows.add(dataRow);
        }

        return dataRows;
    }

    public static List<Map<String, Object>> latest() {
        return informationFlow("global");
    }

    public static List<Map<String, Object>> shares() {
        return informationFlow("shares");
    }

    public static List<Map<String, Object>> bonds() {
        return informationFlow("bonds");
    }

    public static List<Map<String, Object>> commodities() {
        return informationFlow("commodities");
    }

    public static List<Map<String, Object>> forex() {
        return informationFlow("forex");
    }

    public static List<Map<String, Object>> enterprise() {
        return informationFlow("enterprise");
    }

    public static List<Map<String, Object>> assetManage() {
        return informationFlow("asset-manage");
    }

    public static List<Map<String, Object>> tmt() {
        return informationFlow("tmt");
    }

    public static List<Map<String, Object>> estate() {
        return informationFlow("estate");
    }

    public static List<Map<String, Object>> car() {
        return informationFlow("car");
    }

    public static List<Map<String, Object>> medicine() {
        return informationFlow("medicine");
    }

    private static List<Map<String, Object>> informationFlow(String category) {
        String url = "https://api-one.wallstcn.com/apiv1/content/information-flow";

        Map<String, Object> params = new HashMap<>();
        params.put("channel", String.format("%s-channel", category));
        params.put("accept", "article");
        params.put("limit", 25);

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode data = JsonUtils.json(result, "data", "items");

        List<Map<String, Object>> dataRows = new ArrayList<>();
        for (JsonNode item : data) {
            Map<String, Object> dataRow = JsonUtils.map(item, "resource");
            MapUtils.removeKeys(dataRow, "channels", "cover_images", "images");

//            Map<String, Object> dataRow = new LinkedHashMap<>();
//            dataRow.put("title", JsonUtils.string(item, "resource", "title"));
//            dataRow.put("author", JsonUtils.string(item, "resource", "author", "display_name"));
//            dataRow.put("pubDate", JsonUtils.string(item, "resource", "display_time"));
//            dataRow.put("content", JsonUtils.string(item, "resource", "content_short"));
//            dataRow.put("type", JsonUtils.string(item, "resource_type"));
//            dataRow.put("link", JsonUtils.string(item, "resource", "uri"));

            dataRows.add(dataRow);
        }

        return dataRows;
    }

    public static void main(String[] args) {
        System.out.println(MapUtils.print(liveGlobal()));
    }
}
