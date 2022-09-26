package com.github.superzhc.hadoop.flink.streaming.connector.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.apache.flink.util.Preconditions.checkNotNull;

/**
 * @author superz
 * @create 2022/9/24 11:12
 **/
public class HttpConfig implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(HttpConfig.class);

    private String url;
    private String method;

    /*设置请求头*/
    private Map<String, String> headers = new HashMap<>();

    private Object body = null;

    public HttpConfig() {
    }

    public HttpConfig setUrl(String url) {
        this.url = checkNotNull(url, "url");
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HttpConfig setMethod(String method) {
        this.method = checkNotNull(method.toUpperCase(), "Method Support:GET、POST、PUT、DELETE、HEAD、OPTIONS、TRACE");
        return this;
    }

    public String getMethod() {
        return method;
    }

    public HttpConfig setHeaders(Map<String, String> headers) {
        // this.headers = headers;
        this.headers.putAll(headers);
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpConfig setHost(String host) {
        this.headers.put("Host", host);
        return this;
    }

    public HttpConfig setUserAgent(String userAgent) {
        this.headers.put("User-Agent", userAgent);
        return this;
    }

    public HttpConfig setAccept(String accept) {
        this.headers.put("Accept", accept);
        return this;
    }

    public HttpConfig setAcceptEncoding(String acceptEncoding) {
        this.headers.put("Accept-Encoding", acceptEncoding);
        return this;
    }

    public HttpConfig setAcceptCharset(String acceptCharset) {
        this.headers.put("Accept-Charset", acceptCharset);
        return this;
    }

    public HttpConfig setContentType(String contentType) {
        this.headers.put("Content-Type", contentType);
        return this;
    }

    public String getContentType() {
        if (!this.headers.containsKey("Content-Type")) {
            return null;
        }
        return this.headers.get("Content-Type");
    }

    public HttpConfig setAuthorization(String authorization) {
        this.headers.put("Authorization", authorization);
        return this;
    }

    public HttpConfig setCookie(String cookie) {
        this.headers.put("Cookie", cookie);
        return this;
    }

    public HttpConfig setReferer(String referer) {
        this.headers.put("Referer", referer);
        return this;
    }

    public HttpConfig setBody(Object body) {
        this.body = body;
        return this;
    }

    public Object getBody() {
        return body;
    }
}
