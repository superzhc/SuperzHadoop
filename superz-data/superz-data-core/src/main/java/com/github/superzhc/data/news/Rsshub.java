package com.github.superzhc.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.dom4j.XmlUtils;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author superz
 * @create 2023/3/17 11:21
 **/
public class Rsshub {
    private static final Logger LOG = LoggerFactory.getLogger(Rsshub.class);

    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36";

    private String url;
    private String proxyHost = null;
    private Integer proxyPort = null;

    public Rsshub(String url) {
        this.url = url;
    }

    public Rsshub(String url, String proxyHost, Integer proxyPort) {
        this.url = url;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    public List<Map<String, Object>> get(String path) {
        return get(path, null, null);
    }

    public List<Map<String, Object>> get(String path, Map<String, Object> params) {
        return get(path, null, params);
    }

    public List<Map<String, Object>> get(String path, Map<String, String> headers, Map<String, Object> params) {
        HttpRequest request = execute(path, headers, params);

        Element root = XmlUtils.load(request.body());
        root = root.element("channel");

        String type = XmlUtils.text(root, "title");
        String homePage = XmlUtils.text(root, "link");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
        LocalDateTime date = XmlUtils.localDateTime(root, formatter, "lastBuildDate");
        date = date.atZone(ZoneId.of("+0")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

        List<Element> items = XmlUtils.elements(root, "item");
        List<Map<String, Object>> data = new ArrayList<>();
        for (Element item : items) {
//            Map<String, Object> map = new LinkedHashMap<>();
//            map.put("title", XmlUtils.text(item, "title"));
//            map.put("type", type);
//            map.put("link", XmlUtils.text(item, "link"));
//
//            LocalDateTime pubDate = XmlUtils.localDateTime(item, formatter, "pubDate");
//            map.put("date", null == pubDate ? date : pubDate);
//
//            map.put("homePage", homePage);
//            map.put("description", XmlUtils.text(item, "description"));
//            data.add(map);

            // 无需知道返回什么节点，但返回数据类型无法自动转换，看取舍
            Map<String, Object> map = XmlUtils.map(item);

            if (!map.containsKey("guid")) {
                map.put("guid", UUID.randomUUID().toString());
            }

            if (map.containsKey("pubDate")) {
                map.put("pubDate", LocalDateTime.parse(map.get("pubDate").toString(), formatter)
                        .atZone(ZoneId.of("+0")).withZoneSameInstant(ZoneId.systemDefault())
                        .toLocalDateTime());
            } else {
                map.put("pubDate", date);
            }

            map.put("sourceType", type);
            map.put("syncDate", date);
            data.add(map);
        }

        return data;
    }

    public List<Map<String, Object>> getJson(String path) {
        return getJson(path, null, null);
    }

    public List<Map<String, Object>> getJson(String path, Map<String, Object> params) {
        return getJson(path, null, params);
    }

    public List<Map<String, Object>> getJson(String path, Map<String, String> headers, Map<String, Object> params) {
        HttpRequest request = execute(path, headers, params);

        JsonNode json = JsonUtils.loads(request.body());
        Map<String, Object>[] array = JsonUtils.newObjectArray(json, "items");
        return Arrays.asList(array);
    }

    private HttpRequest execute(String path, Map<String, String> headers, Map<String, Object> params) {
        String fullUrl = fullUrl(path);

        HttpRequest request = HttpRequest.get(fullUrl, params);

        if (null != proxyHost && null != proxyPort) {
            request.useProxy(proxyHost, proxyPort);
        }

        boolean isUserAgent = false;
        if (null != headers) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                if ("userAgent".equalsIgnoreCase(header.getKey()) || "User-Agent".equalsIgnoreCase(header.getKey())) {
                    isUserAgent = true;
                }
                request.header(header.getKey(), header.getValue());
            }
        }

        if (!isUserAgent) {
            request.userAgent(DEFAULT_USER_AGENT);
        }
        return request;
    }

    private String fullUrl(String path) {
        String fullUrl = this.url;
        if (!fullUrl.endsWith("/")) {
            fullUrl += "/";
        }

        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        fullUrl += path;
        return fullUrl;
    }
}
