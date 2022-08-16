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
 * @create 2022/8/16 16:29
 **/
public class CaiXin {

    public static Table articles() {
        String url = "http://mapiv5.caixin.com//m/api/getWapIndexListByPage";

        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("callback", "");
        params.put("_", System.currentTimeMillis());

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data");

        List<Map<String, Object>> dataRows = new ArrayList<>();
        for (JsonNode item : JsonUtils.object(json, "list")) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("id", JsonUtils.string(item, "id"));
            dataRow.put("title", JsonUtils.string(item, "title"));
            dataRow.put("summary", JsonUtils.string(item, "summary"));
            dataRow.put("author", JsonUtils.string(item, "author_name"));
            dataRow.put("time", JsonUtils.string(item, "time"));
            dataRow.put("channel", JsonUtils.string(item, "channel_name"));
            dataRow.put("pic", JsonUtils.string(item, "pics"));
            dataRow.put("link", JsonUtils.string(item, "web_url"));
            dataRow.put("group", JsonUtils.string(item, "group"));
            // dataRow.put("",JsonUtils.string(item,""));

            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table yixian() {
        String url = "http://k.caixin.com/app/v1/list";

        Map<String, Object> params = new HashMap<>();
        params.put("productIdList", "8,28");
        params.put("uid", "");
        params.put("unit", "1");
        params.put("name", "");
        params.put("code", "");
        params.put("deviceType", "");
        params.put("device", "");
        params.put("userTag", "");
        params.put("p", "1");
        params.put("c", "20");

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode json = JsonUtils.json(result, "data", "list");

        List<Map<String, Object>> dataRows = new ArrayList<>();
        for (JsonNode item : json) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", JsonUtils.string(item, "title"));
            dataRow.put("text", JsonUtils.string(item, "text"));
            dataRow.put("link", String.format("http://k.caixin.com/web/detail_%s", JsonUtils.string(item, "oneline_news_code")));
            dataRow.put("time", JsonUtils.string(item, "ts"));
            dataRow.put("date", String.format("%s %s", JsonUtils.string(item, "date"), JsonUtils.string(item, "time")));
//            dataRow.put("",JsonUtils.string(item,""));
//            dataRow.put("",JsonUtils.string(item,""));
//            dataRow.put("",JsonUtils.string(item,""));
//            dataRow.put("",JsonUtils.string(item,""));
//            dataRow.put("",JsonUtils.string(item,""));

            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table = yixian();
        System.out.println(table.print());
        System.out.println(table.shape());

    }
}
