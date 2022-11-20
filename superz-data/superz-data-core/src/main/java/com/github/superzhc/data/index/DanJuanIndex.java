package com.github.superzhc.data.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.LocalDateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/11/20 23:51
 */
public class DanJuanIndex {
    private static final Logger log = LoggerFactory.getLogger(DanJuanIndex.class);

    /**
     * 指数估值
     *
     * @return
     */
    public static List<Map<String, Object>> indexEva() {
        String url = "https://danjuanfunds.com/djapi/index_eva/dj";
        String result = HttpRequest.get(url).body();
        JsonNode json = JsonUtils.json(result, "data", "items");

        Map<String, Object>[] arr = JsonUtils.newObjectArray(json);

        List<Map<String, Object>> data = new ArrayList<>();
        for (Map<String, Object> map : arr) {
            map.put("ts", LocalDateTimeUtils.convert4timestamp((long) map.get("ts")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            map.put("begin_at", LocalDateTimeUtils.convert4timestamp((long) map.get("begin_at")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            map.put("created_at", LocalDateTimeUtils.convert4timestamp((long) map.get("created_at")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            map.put("updated_at", LocalDateTimeUtils.convert4timestamp((long) map.get("updated_at")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            data.add(map);
        }
        return data;
    }

    /**
     * 单个指数估值
     *
     * @param indexCode 示例：000905.SH
     *
     * @return
     */
    public static Map<String, Object> indexEva(String indexCode) {
        // https://danjuanapp.com/djapi/index_eva/detail/SH000905
        String url = String.format("https://danjuanapp.com/djapi/index_eva/detail/%s", transform(indexCode));

        String result = HttpRequest.get(url).body();
        JsonNode json = JsonUtils.json(result, "data");

        Map<String, Object> map = JsonUtils.map(json);
        return map;
    }

    /**
     * 指数历史
     *
     * @param indexCode
     * @param type 数据量，可选值：1y,3y,5y,10y
     *
     * @return
     */
    public static List<Map<String, Object>> indexHistory(String indexCode, String type) {
        String url = String.format("https://danjuanapp.com/djapi/fundx/base/index/nav/growth?symbol=%s&day=%s", transform(indexCode), type);

        String result = HttpRequest.get(url).body();
        JsonNode json = JsonUtils.json(result, "data", "symbol_nav_growth");
        List<Map<String, Object>> data = Arrays.asList(JsonUtils.newObjectArray(json));
        return data;
    }

    /**
     * @param indexCode
     * @param type 数据量，可选值：3y,5y,all
     *
     * @return
     */
    public static List<Map<String, Object>> peHistory(String indexCode, String type) {
        String url = String.format("https://danjuanapp.com/djapi/index_eva/pe_history/%s?day=%s", transform(indexCode), type);
        String result = HttpRequest.get(url).body();
        JsonNode json = JsonUtils.json(result, "data", "index_eva_pe_growths");
        List<Map<String, Object>> data = Arrays.asList(JsonUtils.newObjectArray(json));

        return data;
    }

    public static List<Map<String, Object>> pbHistory(String indexCode, String type) {
        String url = String.format("https://danjuanapp.com/djapi/index_eva/pb_history/%s?day=%s", transform(indexCode), type);
        String result = HttpRequest.get(url).body();
        JsonNode json = JsonUtils.json(result, "data", "index_eva_pb_growths");
        List<Map<String, Object>> data = Arrays.asList(JsonUtils.newObjectArray(json));

        return data;
    }

    public static List<Map<String, Object>> roeHistory(String indexCode, String type) {
        String url = String.format("https://danjuanapp.com/djapi/index_eva/roe_history/%s?day=%s", transform(indexCode), type);
        String result = HttpRequest.get(url).body();
        JsonNode json = JsonUtils.json(result, "data", "index_eva_roe_growths");
        List<Map<String, Object>> data = Arrays.asList(JsonUtils.newObjectArray(json));
        return data;
    }

    /**
     * 转换成蛋卷的指数表示方式
     *
     * @param indexCode 示例 000905.SH
     *
     * @return
     */
    private static String transform(String indexCode) {
        String[] ss = indexCode.split("\\.");
        return String.format("%s%s", ss[1], ss[0]);
    }

    public static void main(String[] args) {
        indexHistory("000905.SH", "1y");
    }
}
