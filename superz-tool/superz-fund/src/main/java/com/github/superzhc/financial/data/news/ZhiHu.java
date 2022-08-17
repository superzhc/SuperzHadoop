package com.github.superzhc.financial.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.*;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * @author superz
 * @create 2022/8/17 0:24
 */
public class ZhiHu {
    public static Table hotlist() {
        String url = "https://www.zhihu.com/api/v3/explore/guest/feeds";

        Map<String, Object> params = new HashMap<>();
        params.put("limit", 40);

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode data = JsonUtils.json(result, "data");

        List<Map<String, Object>> dataRows = new ArrayList<>();

        for (JsonNode item : data) {
            String type = JsonUtils.string(item, "target", "type");

            String guid = null;
            String title = null;
            String author = null;
            // 摘要
            String excerpt = null;
            String content = null;
            Long pubDate = null;
            String link = null;
            if ("answer".equals(type)) {
                guid = JsonUtils.string(item, "target", "id");
                title = JsonUtils.string(item, "target", "question", "title");
                author = JsonUtils.string(item, "target", "author", "name");
                excerpt = JsonUtils.string(item, "target", "excerpt");
                content = JsonUtils.string(item, "target", "content");
                pubDate = JsonUtils.aLong(item, "target", "updated_time");
                String questionId = JsonUtils.string(item, "target", "question", "id");
                link = String.format("https://www.zhihu.com/question/%s/answer/%s", questionId, guid);
            } else if ("article".equals(type)) {
                guid = JsonUtils.string(item, "target", "id");
                title = JsonUtils.string(item, "target", "title");
                author = JsonUtils.string(item, "target", "author", "name");
                content = JsonUtils.string(item, "target", "content");
                pubDate = JsonUtils.aLong(item, "updated");
                link = String.format("https://zhuanlan.zhihu.com/p/%s", JsonUtils.string(item, "target", "id"));
            } else {
                content = JsonUtils.asString(item);
            }

            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", title);
            dataRow.put("author", author);
            dataRow.put("excerpt", excerpt);
            dataRow.put("content", content);
            dataRow.put("pubDate", pubDate);
            dataRow.put("link", link);
            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table hot() {
        return hot("total");
    }

    /**
     * 可选值如下：
     * 全站	 国际	科学	汽车	视频	时尚	时事	数码	体育	校园	影视
     * total focus	science	car	    zvideo	fashion	depth	digital	sport	school	film
     *
     * @param category
     * @return
     */
    public static Table hot(String category) {
        String url = String.format("https://www.zhihu.com/api/v3/feed/topstory/hot-lists/%s", category);

        Map<String, Object> params = new HashMap<>();
        params.put("limit", 50);

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode data = JsonUtils.json(result, "data");

        List<Map<String, Object>> dataRows = new ArrayList<>();

        for (JsonNode item : data) {
            String title = JsonUtils.string(item, "target", "title");
            Long pubDate = JsonUtils.aLong(item, "target", "created");
            String description = JsonUtils.string(item, "target", "excerpt");
            String link = String.format("https://www.zhihu.com/question/%s", JsonUtils.string(item, "target", "id"));

            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", title);
            dataRow.put("content", description);
            dataRow.put("pubDate", pubDate);
            dataRow.put("link", link);
            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table news() {
        String url = "https://api.zhihu.com/pins/special/972884951192113152/moments";

        Map<String, Object> params = new HashMap<>();
        params.put("order_by", "newest");
        params.put("reverse_order", "0");
        params.put("limit", "50");

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode data = JsonUtils.json(result, "data");
        List<Map<String, Object>> dataRows = convert(data);
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table pinHot() {
        String url = "https://api.zhihu.com/pins/hot_list";

        Map<String, Object> params = new HashMap<>();
        params.put("reverse_order", 0);

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode data = JsonUtils.json(result, "data");
        Table table = TableUtils.buildByMap(convert(data));
        return table;
    }

    private static List<Map<String, Object>> convert(JsonNode data) {
        List<Map<String, Object>> dataRows = new ArrayList<>();

        for (JsonNode item : data) {
            JsonNode target = item.has("target") ? JsonUtils.object(item, "target") : item;
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", JsonUtils.string(target, "excerpt_title"));
            dataRow.put("author", JsonUtils.string(target, "author", "name"));
            dataRow.put("pubDate", JsonUtils.aLong(target, "created"));
            dataRow.put("link", String.format("https://www.zhihu.com/pin/%s", JsonUtils.string(target, "id")));

            StringBuilder sb = new StringBuilder();
            JsonNode contents = JsonUtils.object(target, "content");
            for (JsonNode content : contents) {
                String type = JsonUtils.string(content, "type");
                if ("text".equals(type)) {
                    sb.append(JsonUtils.string(content, "content"));
                } else if ("image".equals(type)) {
                    sb.append(JsonUtils.string(content, "url"));
                } else if ("video".equals(type)) {
                    sb.append(JsonUtils.text(content));
                } else if ("link".equals(type)) {
                    sb.append("[").append(JsonUtils.string(content, "title")).append("]").append("(").append(JsonUtils.string(content, "url")).append(")");
                } else {
                    sb.append(JsonUtils.text(content));
                }
                sb.append("\n");
            }
            dataRow.put("content", sb.toString());
            dataRows.add(dataRow);
        }

        return dataRows;
    }

    public static void main(String[] args) {
        Table table = pinHot();
        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
