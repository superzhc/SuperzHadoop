package com.github.superzhc.financial.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.financial.utils.XueQiuUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.*;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * @author superz
 * @create 2022/8/17 15:29
 **/
public class XueQiu {
    public static Table hot() {
        String url = "https://xueqiu.com/statuses/hots.json";

        Map<String, Object> params = new HashMap<>();
        params.put("a", "1");
        params.put("count", 10);
        params.put("page", 1);
        params.put("scope", "day");
        params.put("type", "status");
        params.put("meigu", "0");

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).cookies(XueQiuUtils.cookies()).body();
        JsonNode data = JsonUtils.json(result);

        List<Map<String, Object>> dataRows = new ArrayList<>(data.size());
        for (JsonNode item : data) {
            Map<String, Object> dataRow = new LinkedHashMap<>();
            dataRow.put("title", JsonUtils.string(item, "title"));
            dataRow.put("description", JsonUtils.string(item, "text"));
            dataRow.put("pubDate", JsonUtils.aLong(item, "created_at"));
            dataRow.put("author", JsonUtils.string(item, "user", "screen_name"));
            dataRow.put("link", String.format("https://xueqiu.com%s", JsonUtils.string(item, "target")));

            dataRows.add(dataRow);
        }

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table = Table.create();

        table = hot();

        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
