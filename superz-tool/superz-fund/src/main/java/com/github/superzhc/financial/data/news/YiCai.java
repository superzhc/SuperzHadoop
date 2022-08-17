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
 * @create 2022/8/17 14:24
 **/
public class YiCai {
    private static final String ROOT_URL = "https://www.yicai.com";

    public static Table brief() {
        String url = String.format("%s/api/ajax/getbrieflist", ROOT_URL);

        Map<String, Object> params = new HashMap<>();
        params.put("type", "0");
        params.put("page", 1);
        params.put("pagesize", 50);

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode data = JsonUtils.json(result);

        List<Map<String, Object>> dataRows = new ArrayList<>(data.size());
        for (JsonNode item : data) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", JsonUtils.string(item, "indexTitle"));
            dataRow.put("link", String.format("%s%s", ROOT_URL, JsonUtils.string(item, "url")));
            dataRow.put("content", JsonUtils.text(item, "newcontent"));
            dataRow.put("pubDate", String.format("%s %s", JsonUtils.string(item, "datekey"), JsonUtils.string(item, "hm")));
            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table author(String id) {
        String url = String.format("%s/api/ajax/getlistbysid", ROOT_URL);

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("page", 1);
        params.put("pagesize", 30);

        return execute(url, params);
    }

    public static Table feeds(String id) {
        String url = String.format("%s/api/ajax/getlistbytid", ROOT_URL);

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("page", 1);
        params.put("pagesize", 30);

        return execute(url, params);
    }

    public static Table toutiao() {
        String url = String.format("%s/api/ajax/getlistbycid", ROOT_URL);

        Map<String, Object> params = new HashMap<>();
        params.put("cid", "48");
        params.put("type", "1");
        params.put("page", 1);
        params.put("pagesize", 30);

        return execute(url, params);
    }

    public static Table latest() {
        String url = String.format("%s/api/ajax/getlatest", ROOT_URL);

        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("pagesize", 30);

        return execute(url, params);
    }

    public static Table channel(String cid) {
        String url = String.format("%s/api/ajax/getlistbycid", ROOT_URL);

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("page", 1);
        params.put("pagesize", 30);

        return execute(url, params);
    }

    public static Table news() {
        String url = String.format("%s/api/ajax/getjuhelist", ROOT_URL);

        Map<String, Object> params = new HashMap<>();
        params.put("action", "news");
        params.put("page", 1);
        params.put("pagesize", 30);

        return execute(url, params);
    }

    public static Table video() {
        String url = String.format("%s/api/ajax/getjuhelist", ROOT_URL);

        Map<String, Object> params = new HashMap<>();
        params.put("action", "video");
        params.put("page", 1);
        params.put("pagesize", 30);

        return execute(url, params);
    }

    private static Table execute(String url, Map<String, ?> params) {
        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode data = JsonUtils.json(result);

        List<Map<String, Object>> dataRows = new ArrayList<>(data.size());

        for (JsonNode item : data) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", JsonUtils.string(item, "NewsTitle"));

            String itemUrl = JsonUtils.string(item, "url");
            if (!itemUrl.startsWith("http:")) {
                int appId = JsonUtils.integer(item, "AppID");
                itemUrl = String.format("%s%s%s", ROOT_URL, appId == 0 ? "/vip" : "", itemUrl);
            }
            dataRow.put("url", itemUrl);

            String author = JsonUtils.string(item, "NewsAuthor");
            if (null == author || author.trim().length() == 0) {
                author = JsonUtils.string(item, "NewsSource");
                if (null == author || author.trim().length() == 0) {
                    author = JsonUtils.string(item, "CreaterName");
                }
            }
            dataRow.put("author", author);

            dataRow.put("pubDate", JsonUtils.aLong(item, "CreateDate"));
            dataRow.put("category", JsonUtils.text(item, "ChannelName"));

            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table = Table.create();

//         table = author("100005663");
//        table = feeds("669");
//        table = toutiao();
        table = news();

        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
