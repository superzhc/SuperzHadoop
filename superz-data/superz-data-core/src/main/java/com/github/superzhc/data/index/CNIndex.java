package com.github.superzhc.data.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;

import java.util.*;

/**
 * @author superz
 * @create 2022/11/20 22:33
 */
public class CNIndex {
    public static List<Map<String, Object>> indices() {
        String url = "http://www.cnindex.com.cn/index/indexList";

        Map<String, Object> params = new HashMap<>();
        params.put("channelCode", -1);
        params.put("rows", 2000);
        params.put("pageNum", 1);

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data", "rows");
        Map<String, Object>[] data = JsonUtils.newObjectArray(json);
        return Arrays.asList(data);
    }

    /**
     * 很多指数数据读取不到
     *
     * @param symbol
     *
     * @return
     */
    @Deprecated
    public static List<Map<String, Object>> history(String symbol) {
        return history(symbol, "");
    }

    /**
     * 很多指数读取不到
     *
     * @param symbol
     * @param date
     *
     * @return
     */
    @Deprecated
    public static List<Map<String, Object>> history(String symbol, String date) {
        String url = "http://hq.cnindex.com.cn/market/market/getIndexDailyDataWithDataFormat";

        Map<String, Object> params = new HashMap<>();
        params.put("indexCode", transform(symbol));
        params.put("startDate", date);
        params.put("endDate", date);
        params.put("frequency", "day");

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data");
        String indexCode = JsonUtils.string(json, "indexCode");
        String indexName = JsonUtils.string(json, "indexName");
        String indexEName = JsonUtils.string(json, "indexEName");

        String[] keys = JsonUtils.stringArray(json, "item");
        Object[][] values = JsonUtils.newArrayArray(json, "data");

        List<Map<String, Object>> data = new ArrayList<>(values.length);
        for (int i = 0, len = values.length; i < len; i++) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("indexCode", indexCode);
            item.put("itemName", indexName);
            item.put("itemEName", indexEName);

            Object[] value = values[i];
            for (int j = 0, valueLen = keys.length; j < valueLen; j++) {
                item.put(keys[j], value[j]);
            }

            data.add(item);
        }
        return data;
    }

    private static String transform(String symbol) {
        String[] ss = symbol.split("\\.");
        return ss[0];
    }

    public static void main(String[] args) {
        // System.out.println(indices());
        System.out.println(JsonUtils.asString(history("399001", "2022-11-17")));
    }
}
