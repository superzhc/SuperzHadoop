package com.github.superzhc.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.MapUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2022/8/15 0:35
 */
public class News36kr {
    private static final Logger log = LoggerFactory.getLogger(News36kr.class);

    private static final String pattern = "\"itemList\":(\\[.*?\\])";

    /**
     * 最新资讯
     *
     * @return
     */
    public static List<Map<String, Object>> latest() {
        return information("web_news");
    }

    public static List<Map<String, Object>> recommend() {
        return information("web_recommend");
    }

    /**
     * 创投
     *
     * @return
     */
    public static List<Map<String, Object>> vc() {
        return information("contact");
    }

    public static List<Map<String, Object>> car() {
        return information("travel");
    }

    public static List<Map<String, Object>> technology() {
        return information("technology");
    }

    /**
     * 企服
     *
     * @return
     */
    public static List<Map<String, Object>> enterpriseService() {
        return information("enterpriseservice");
    }

    /**
     * 创新
     *
     * @return
     */
    public static List<Map<String, Object>> innovate() {
        return information("innovate");
    }

    /**
     * 房产
     *
     * @return
     */
    public static List<Map<String, Object>> realEstate() {
        return information("real_estate");
    }

    public static List<Map<String, Object>> life() {
        return information("happy_life");
    }

    public static List<Map<String, Object>> other() {
        return information("other");
    }

    /**
     * 财经
     *
     * @return
     */
    public static List<Map<String, Object>> finance() {
        return information("ccs");
    }

    public static List<Map<String, Object>> information(String category) {
        String path = String.format("/information/%s", category);
        JsonNode json = execute(path);
        JsonNode dataNode = JsonUtils.jsonPath(json, "$..templateMaterial");
        return Arrays.asList(JsonUtils.newObjectArray(dataNode));
    }

    public static List<Map<String, Object>> topic(String id) {
        String path = String.format("/motif/%s", id);
        JsonNode json = execute(path);
        JsonNode dataNode = JsonUtils.jsonPath(json, "$..templateMaterial");
        return Arrays.asList(JsonUtils.newObjectArray(dataNode));
    }

    public static List<Map<String, Object>> user(String id) {
        String path = String.format("/user/%s", id);
        JsonNode json = execute(path);
        JsonNode dataNode = JsonUtils.jsonPath(json, "$..templateMaterial");
        return Arrays.asList(JsonUtils.newObjectArray(dataNode));
    }

    public static List<Map<String, Object>> search(String keyword) {
        String path = String.format("/search/articles/%s", keyword);
        JsonNode json = execute(path);
        return Arrays.asList(JsonUtils.newObjectArray(json));
    }

    public static List<Map<String, Object>> searchInformation(String keyword) {
        String path = String.format("/search/information/%s", keyword);
        JsonNode json = execute(path);
        return Arrays.asList(JsonUtils.newObjectArray(json));
    }

    private static JsonNode execute(String path) {
        String url = String.format("https://www.36kr.com%s", path);
        String result = HttpRequest.get(HttpRequest.encode(url)).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36").body();
        Document document = Jsoup.parse(result);

        String data = null;
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(document.data().replaceAll("<\\/?em>", ""));
        if (matcher.find()) {
            data = matcher.group(1);
        }

        if (null == data) {
            return null;
        }

        JsonNode json = JsonUtils.json(data);
        return json;
    }

    public static void main(String[] args) {
        List<Map<String, Object>> maps = null;
        // maps=searchInformation("南京");;
        // maps = topic("327685996545");
        maps = user("5258135");
        System.out.println(MapUtils.print(maps));
    }
}
