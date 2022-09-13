package com.github.superzhc.data.mixture;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.data.utils.XueQiuUtils;

import java.util.*;

/**
 * @author superz
 * @create 2022/9/13 10:39
 **/
public class XueQiu {
    public static void userArticle(String userId) {
        String url = "https://xueqiu.com/v4/statuses/user_timeline.json";

        Integer pages = null;

        List<String[]> dataRows = new ArrayList<>();

        /**
         * 超过5页，需要login ???
         */

        for (int i = 0; ((pages == null || i < pages) && i < 5); i++) {
            Map<String, Object> params = new HashMap<>();
            params.put("page", (i + 1));
            params.put("user_id", userId);

            String result = HttpRequest.get(url, params).cookies(XueQiuUtils.cookies()).body();

            // TODO 解析结果

//            JsonNode json = JsonUtils.json(result);
//
//            if (null == pages) {
//                pages = JsonUtils.integer(json, "maxPage");
//            }
//
//            JsonNode articles = json.get("statuses");
//            for (int j = 0, len = articles.size(); j < len; j++) {
//                JsonNode article = articles.get(j);
//                String[] dataRow = new String[]{JsonUtils.asString(article)};
//                dataRows.add(dataRow);
//            }
        }
    }

    public static void main(String[] args) {
        // userArticle("9391624441");
    }
}
