package com.github.superzhc.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 华人头条
 *
 * @author superz
 * @create 2022/9/20 9:33
 **/
public class News52HRTT {
    public static List<Map<String,String>> global(){
        String area = "global";
        String type = null;

        return news(area,type);
    }

    public static List<Map<String, String>> news(String area, String type) {
        String url = String.format("https://www.52hrtt.com/s/webapi/%s/n/w", area);

        Map<String, String> params = new HashMap<>();
        if (null != type && type.trim().length() > 0) {
            params.put("infoTypeId", type);
        }

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.json(result, "data", "infosMap", "infoList");
        List<Map<String, String>> dataRows = Arrays.asList(JsonUtils.objectArray2Map(json));
        return dataRows;
    }

    public static void main(String[] args) {
        String area = "global";
        String type = null;

        List<Map<String, String>> dataRows = news(area, type);
        System.out.println(dataRows);
    }
}
