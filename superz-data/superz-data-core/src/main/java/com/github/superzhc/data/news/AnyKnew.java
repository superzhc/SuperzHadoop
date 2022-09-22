package com.github.superzhc.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/9/22 14:17
 **/
public class AnyKnew {


    public static List<Map<String, String>> execute(String type) {
        String url = String.format("https://www.anyknew.com/api/v1/sites/%s", type);

        String result = HttpRequest.get(url).body();
        JsonNode json = JsonUtils.json(result, "data", "site", "subs");
        json = json.get(0).get("items");
        List<Map<String, String>> lst = Arrays.asList(JsonUtils.objectArray2Map(json));
        return lst;
    }

    public static void main(String[] args) {
        System.out.println(execute("zhihu"));
    }
}
