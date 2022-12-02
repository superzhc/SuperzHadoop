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
        return execute("web_news");
    }

    public static List<Map<String, Object>> recommend() {
        return execute("web_recommend");
    }

    /**
     * 创投
     *
     * @return
     */
    public static List<Map<String, Object>> vc() {
        return execute("contact");
    }

    public static List<Map<String, Object>> car() {
        return execute("travel");
    }

    public static List<Map<String, Object>> technology() {
        return execute("technology");
    }

    /**
     * 企服
     *
     * @return
     */
    public static List<Map<String, Object>> enterpriseService() {
        return execute("enterpriseservice");
    }

    /**
     * 创新
     *
     * @return
     */
    public static List<Map<String, Object>> innovate() {
        return execute("innovate");
    }

    /**
     * 房产
     *
     * @return
     */
    public static List<Map<String, Object>> realEstate() {
        return execute("real_estate");
    }

    public static List<Map<String, Object>> life() {
        return execute("happy_life");
    }

    public static List<Map<String, Object>> other() {
        return execute("other");
    }

    /**
     * 财经
     *
     * @return
     */
    public static List<Map<String, Object>> finance() {
        return execute("ccs");
    }

    public static List<Map<String, Object>> execute(String category) {
        String url = "https://www.36kr.com/information/ccs";

        String result = HttpRequest.get(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36").body();
        Document document = Jsoup.parse(result);

        String data = null;
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(document.data());
        if (matcher.find()) {
            data = matcher.group(1);
        }

        if (null == data) {
            return null;
        }

        JsonNode json = JsonUtils.json(data);
        JsonNode dataNode = JsonUtils.jsonpath(json, "$..templateMaterial");
        return Arrays.asList(JsonUtils.newObjectArray(dataNode));
    }

    public static void main(String[] args) {
        List<Map<String, Object>> maps = latest();
        System.out.println(MapUtils.print(maps));
    }
}
