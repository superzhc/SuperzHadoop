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
 * @create 2022/8/14 22:33
 */
public class Bilibili {
    public static void main(String[] args) {
        // 注意事项：视频标识是以BV开头的
        String bvid = "BV1Ea411D7VA";

        String url1 = "https://api.bilibili.com/x/web-interface/view";

        Map<String, Object> params1 = new HashMap<>();
        params1.put("bvid", bvid);

        String result1 = HttpRequest.get(url1, params1).userAgent(UA_CHROME).body();
        String aid = JsonUtils.string(result1, "data", "aid");

        String url = "https://api.bilibili.com/x/v2/reply";

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("type", 1);
        params.put("oid", aid);
        // 分页参数好像不对
        // params.put("next", 2);
        params.put("sort", 0);

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).body();
        JsonNode replies = JsonUtils.json(result, "data", "replies");

        String[] columnNames = new String[]{"username", "message"};
        List<String[]> dataRows = new ArrayList<>();
        for (JsonNode reply : replies) {
            String username = JsonUtils.string(reply, "member", "uname");
            String message = JsonUtils.string(reply, "content", "message");
            dataRows.add(new String[]{username, message});
        }

        Table table = TableUtils.build(columnNames, dataRows);

        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
