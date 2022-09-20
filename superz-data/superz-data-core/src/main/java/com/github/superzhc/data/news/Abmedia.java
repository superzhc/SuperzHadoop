package com.github.superzhc.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.html.UserAgent;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;

import java.util.*;

/**
 * @author superz
 * @create 2022/9/20 15:40
 **/
public class Abmedia {

    public static List<Map<String, String>> news() {
        return news(null);
    }

    public static List<Map<String, String>> news(String category) {
        String categoryUrl = String.format("https://www.abmedia.io/wp-json/wp/v2/categories?slug=%s", category);

        String result = HttpRequest.get(categoryUrl).userAgent(UserAgent.Google()).body();
        JsonNode json = JsonUtils.json(result);
        Integer categoryId = JsonUtils.integer(json.get(0), "id");

        String url = "https://www.abmedia.io/wp-json/wp/v2/posts";

        Map<String, Object> params = new HashMap<>();
        if (null != category && category.trim().length() > 0) {
            params.put("categories", categoryId);
        }
        params.put("page", 1);
        params.put("per_page", 100);

        result = HttpRequest.get(url, params).userAgent(UserAgent.Google()).body();

        List<Map<String, String>> dataRows = new ArrayList<>();

        JsonNode data = JsonUtils.json(result);
        for (JsonNode item : data) {
            Map<String, String> dataRow = new LinkedHashMap<>();
            dataRow.put("title", JsonUtils.string(item, "title", "rendered"));
            dataRow.put("date", JsonUtils.string(item, "date"));
            dataRow.put("link", JsonUtils.string(item, "link"));
            dataRow.put("content", JsonUtils.string(item, "content", "rendered"));
            dataRow.put("excerpt", JsonUtils.string(item, "excerpt", "rendered"));

            dataRows.add(dataRow);
        }

        return dataRows;
    }

    public static void main(String[] args) {
        String category = "technology-development";

    }
}
