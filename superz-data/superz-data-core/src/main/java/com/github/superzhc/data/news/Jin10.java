package com.github.superzhc.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.MapUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/12/2 18:08
 **/
public class Jin10 {
    public static List<Map<String, Object>> news() {
        return news(LocalDateTime.now());
    }

    public static List<Map<String, Object>> news(LocalDateTime dt) {
        String url = "https://flash-api.jin10.com/get_flash_list";

        Map<String, Object> params = new HashMap<>();
        params.put("channel", "-8200");
        params.put("vip", 1);
        params.put("max_time", dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        params.put("t", System.currentTimeMillis());

        String result = HttpRequest.get(url, params)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36")
                .header("x-app-id", "bVBF4FyRTn5NJF5n")
                .header("x-version", "1.0.0")
                .body();
        JsonNode json = JsonUtils.json(result, "data");
        List<Map<String, Object>> maps = Arrays.asList(JsonUtils.newObjectArray(json));
        return maps;
    }

    public static void main(String[] args) {
        System.out.println(MapUtils.print(news()));
    }
}
