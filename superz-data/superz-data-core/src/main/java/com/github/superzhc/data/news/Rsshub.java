package com.github.superzhc.data.news;

import com.github.superzhc.common.dom4j.XmlUtils;
import com.github.superzhc.common.http.HttpRequest;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
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
        String fullUrl = this.url;
        if (!fullUrl.endsWith("/")) {
            fullUrl += "/";
        }

        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        fullUrl += path;

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

        Element root = XmlUtils.load(request.stream());
        root = root.element("channel");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
        LocalDateTime date = XmlUtils.localDateTime(root, formatter, "lastBuildDate");

        List<Element> items = XmlUtils.elements(root, "item");
        List<Map<String, Object>> data = new ArrayList<>();
        for (Element item : items) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("title", XmlUtils.text(item, "title"));
            map.put("description", XmlUtils.text(item, "description"));
            map.put("link", XmlUtils.text(item, "link"));
            map.put("date", date);
            data.add(map);
        }

        return data;
    }
}
