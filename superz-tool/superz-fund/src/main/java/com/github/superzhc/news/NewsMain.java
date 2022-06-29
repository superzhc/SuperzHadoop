package com.github.superzhc.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.html.UserAgent;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2022/6/23 16:15
 **/
public class NewsMain {
    public static void main(String[] args) {
//        String url = "https://www.zhihu.com/api/v4/search/top_search";
//
//        String result = HttpRequest.get(url).userAgent(UserAgent.random()).body();
//        JsonNode json = JsonUtils.json(result, "top_search", "words");
//
//        String[] words = JsonUtils.objectOneArray(json, "display_query");
//        System.out.println(Arrays.asList(words));
//
//        String url2 = "https://www.zhihu.com/search";
//
//        Map<String, String> map = new HashMap<>();
//        map.put("q", words[0]);
//
//        result = HttpRequest.get(url2, map).body();
//        System.out.println(result);

        String url = "https://is-lq.snssdk.com/api/suggest_words/?business_id=10016";

        String result = HttpRequest.get(url).body();
        System.out.println(result);
    }
}
