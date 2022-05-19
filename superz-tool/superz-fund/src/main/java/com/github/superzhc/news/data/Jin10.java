package com.github.superzhc.news.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.superzhc.common.HttpConstant.UA;

/**
 * @author superz
 * @create 2022/5/8 15:57
 **/
public class Jin10 {
    public static Table news() {
        return news(LocalDateTime.now());
    }

    public static Table news(LocalDateTime dt) {
        String url = "https://flash-api.jin10.com/get_flash_list";

        Map<String, Object> params = new HashMap<>();
        params.put("channel", "-8200");
        params.put("vip", 1);
        params.put("max_time", dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        params.put("t", System.currentTimeMillis());

        String result = HttpRequest.get(url, params)
                .userAgent(UA)
                .header("x-app-id", "bVBF4FyRTn5NJF5n")
                .header("x-version", "1.0.0")
                .body();
        JsonNode json = JsonUtils.json(result, "data");

        List<String> columnNames = JsonUtils.extractObjectColumnName(json);
        List<String[]> dataRows = JsonUtils.extractObjectData(json, columnNames);

        Table table = TableUtils.build(columnNames, dataRows);

        StringColumn content = table.stringColumn("data").map(str -> {
            if (null == str || str.trim().length() == 0) {
                return "";
            }

            JsonNode data = JsonUtils.json(str);
            if (data.has("content")) {
                return data.get("content").asText();
            } else {
                return "";
            }
        }).setName("content");
        table.replaceColumn("data", content);

        return table;
    }

    public static void main(String[] args) {
        Table table = news();

        System.out.println(table.print());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
    }
}
