package com.github.superzhc.data.mixture;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.utils.XueQiuUtils;

import java.util.*;

/**
 * @author superz
 * @create 2022/9/13 10:39
 **/
public class XueQiu {
    private static final String UA_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36";

    public static List<Map<String, Object>> hot() {
        String cookies = XueQiuUtils.cookies();

        String url = "https://xueqiu.com/statuses/hots.json";

        Map<String, Object> params = new HashMap<>();
        params.put("a", "1");
        params.put("count", 10);
        params.put("page", 1);
        params.put("scope", "day");
        params.put("type", "status");
        params.put("meigu", "0");

        String result = HttpRequest.get(url, params).userAgent(UA_CHROME).cookies(cookies).body();
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

        return dataRows;
    }

    public static List<Map<String, Object>> userArticle(String userId) {
        String url = "https://xueqiu.com/v4/statuses/user_timeline.json";

        Integer pages = null;

        List<String[]> dataRows = new ArrayList<>();

        /**
         * 超过5页，需要login
         */
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("user_id", userId);

        String result = HttpRequest.get(url, params).cookies(XueQiuUtils.cookies()).body();
        JsonNode json = JsonUtils.json(result);
        Map<String, Object>[] maps = JsonUtils.newObjectArray(json, "statuses");

        List<Map<String, Object>> data = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            MapUtils.removeKeys(map
                    , "pic_sizes"
                    , "is_private"
                    , "forbidden_comment"
                    , "mp_not_show_status"
                    , "is_original_declare"
                    , "is_no_archive"
                    , "answer_question"
                    , "vod_info"
                    , "recommend_cards"
                    , "show_cover_pic"
                    , "legal_user_visible"
                    , "is_column"
            );

            data.add(map);
        }
        return data;
    }

    public static void main(String[] args) {
        // userArticle("9391624441");
        System.out.println(MapUtils.print(hot()));
    }
}
