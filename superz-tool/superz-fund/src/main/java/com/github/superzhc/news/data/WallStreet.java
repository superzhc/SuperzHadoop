package com.github.superzhc.news.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

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

        // List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);
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

    public static void main(String[] args) {
        Table table = news();
        System.out.println(table.structure().printAll());
        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
