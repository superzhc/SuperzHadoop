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
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.liveGlobal());
    }

    public static Table liveAStock() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.liveAStock());
    }

    public static Table liveUSStock() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.liveUSStock());
    }

    public static Table liveHKStock() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.liveHKStock());
    }

    public static Table liveForex() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.liveForex());
    }

    public static Table liveCommodity() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.liveCommodity());
    }

    public static Table liveFinancing() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.liveFinancing());
    }

    public static Table latest() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.latest());
    }

    public static Table shares() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.shares());
    }

    public static Table bonds() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.bonds());
    }

    public static Table commodities() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.commodities());
    }

    public static Table forex() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.forex());
    }

    public static Table enterprise() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.enterprise());
    }

    public static Table assetManage() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.assetManage());
    }

    public static Table tmt() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.tmt());
    }

    public static Table estate() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.estate());
    }

    public static Table car() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.car());
    }

    public static Table medicine() {
        return TableUtils.buildByMap(com.github.superzhc.data.mixture.WallStreet.medicine());
    }

    public static void main(String[] args) {
        Table table = car();
        System.out.println(table.structure().printAll());
        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
