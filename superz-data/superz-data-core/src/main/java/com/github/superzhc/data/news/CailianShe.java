package com.github.superzhc.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.MapUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/12/2 17:51
 **/
public class CailianShe {
    public static List<Map<String, Object>> telegraph() {
        String url = "https://www.cls.cn/nodeapi/updateTelegraphList";

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("app", "CailianpressWeb");
        params.put("category", "");
        params.put("hasFirstVipArticle", "1");
//        params.put("lastTime",);
        params.put("os", "web");
        params.put("rn", 100);//数量
        params.put("subscribedColumnIds", "");
        params.put("sv", "7.7.5");
        // params.put("sign","b2c3f844c6867e28951c61c8e889de36");

        String result = HttpRequest.get(url, params)
                .header("Host", "www.cls.cn")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36")
                .body();
        JsonNode json = JsonUtils.json(result, "data", "roll_data");

        return Arrays.asList(JsonUtils.newObjectArray(json));
    }

    public static void main(String[] args) {
        System.out.println(MapUtils.print(telegraph()));
    }
}
