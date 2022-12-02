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
        userArticle("9391624441");
    }
}
