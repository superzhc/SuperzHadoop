package com.github.superzhc.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/9/22 14:17
 **/
public class AnyKnew {

    public static List<Map<String, String>> weibo() {
        return execute("weibo");
    }

    public static List<Map<String, String>> zhihu() {
        return execute("zhihu");
    }

    public static List<Map<String, String>> smzdm() {
        return execute("smzdm");
    }

    public static List<Map<String, String>> finance() {
        List<Map<String, String>> data = new ArrayList<>();
        data.addAll(xueqiu());
        data.addAll(investing());
        data.addAll(wallstreetcn());
        data.addAll(eastmoney());
        data.addAll(caixin());
        return data;
    }

    public static List<Map<String, String>> xueqiu() {
        return execute("xueqiu");
    }

    public static List<Map<String, String>> investing() {
        return execute("investing");
    }

    public static List<Map<String, String>> wallstreetcn() {
        return execute("wallstreetcn");
    }

    public static List<Map<String, String>> eastmoney() {
        return execute("eastmoney");
    }

    public static List<Map<String, String>> caixin() {
        return execute("caixin");
    }

    public static List<Map<String, String>> execute(String type) {
        String url = String.format("https://www.anyknew.com/api/v1/sites/%s", type);

        String result = HttpRequest.get(url).body();
        JsonNode json = JsonUtils.json(result, "data", "site");
        String platform = JsonUtils.string(json, "site");
        String platformCN = JsonUtils.string(json, "attrs", "cn");
        String siteUrl = JsonUtils.string(json, "attrs", "url");

        JsonNode data = JsonUtils.object(json, "subs").get(0).get("items");
        List<Map<String, String>> lst = new ArrayList<>();
        Map<String, String>[] maps = JsonUtils.objectArray2Map(data);
        for (Map<String, String> map : maps) {
            map.put("platform", platform);
            map.put("platformCN", platformCN);
            map.put("siteUrl", siteUrl);
            lst.add(map);
        }
        return lst;
    }

    public static void main(String[] args) {
        System.out.println(JsonUtils.asString(finance()));
    }
}
