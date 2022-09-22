package com.github.superzhc.hadoop.flink.streaming.connector.customsource;

import com.github.superzhc.common.http.HttpRequest;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.github.superzhc.common.http.HttpRequest.*;

/**
 * Http Source
 *
 * @author superz
 * @create 2022/2/21 10:52
 */
public class JavaHttpSource extends RichSourceFunction<String> {
    private static final Logger log = LoggerFactory.getLogger(JavaHttpSource.class);

    public static final String CONNECT_TIMEOUT = "connect.timeout";
    public static final String READ_TIMEOUT = "read.timeout";
    // public static final String WRITE_TIMEOUT = "write.timeout";

    public static final String URL = "url";
    public static final String METHOD = "method";
    public static final String HEADERS = "headers";
    public static final String DATA = "data";

    public static final String PERIOD = "period";

    private volatile boolean cancelled = false;

    private Map<String, Object> configs;

    public JavaHttpSource(Map<String, Object> configs) {
        this.configs = configs;
    }

//    public JavaHttpSource(String url, String method) {
//        this(url, method, null, null);
//    }
//
//    public JavaHttpSource(String url, String method, Object data) {
//        this(url, method, null, data);
//    }

    public JavaHttpSource(String url, String method, Map<String, String> headers, Object data) {
        this(url, method, headers, data, null);
    }

    @Deprecated
    public JavaHttpSource(String url, String method, Map<String, String> headers, Object data, Map<String, Object> configs) {
        if (null != configs) {
            this.configs = configs;
        } else {
            this.configs = new HashMap<>();
        }

        this.configs.put(URL, url);
        this.configs.put(METHOD, method);
        if (null != headers) {
            this.configs.put(HEADERS, headers);
        }
        if (null != data) {
            this.configs.put(DATA, data);
        }
    }

    public static JavaHttpSource get(String url) {
        return get(url, null);
    }

    public static JavaHttpSource get(String url, Map<String, String> headers) {
        return new JavaHttpSource(url, HttpRequest.METHOD_GET, headers, null, null);
    }

    public static JavaHttpSource post(String url, Map<String, Object> form) {
        return post(url, null, form);
    }

    public static JavaHttpSource post(String url, Map<String, String> headers, Map<String, Object> form) {
        return new JavaHttpSource(url, HttpRequest.METHOD_POST, headers, form, null);
    }

    public static JavaHttpSource post(String url, String json) {
        return post(url, null, json);
    }

    public static JavaHttpSource post(String url, Map<String, String> headers, String json) {
        return new JavaHttpSource(url, HttpRequest.METHOD_POST, headers, json, null);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        if (!configs.containsKey(URL) || null == configs.get(URL)) {
            throw new IllegalArgumentException("参数【url】必须存在且不为空");
        }
    }

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        while (!cancelled) {
            String url = (String) configs.get(URL);
            String method = (!configs.containsKey(METHOD) || null == configs.get(METHOD)) ? "GET" : (String) configs.get(METHOD);
            HttpRequest request = new HttpRequest(url, method);

            if (configs.containsKey(HEADERS) && null != configs.get(HEADERS)) {
                Map<String, String> headers = (Map<String, String>) configs.get(HEADERS);
                request.headers(headers);
            }

            if (configs.containsKey(READ_TIMEOUT) && null != configs.get(READ_TIMEOUT)) {
                int timeout = (int) configs.get(READ_TIMEOUT);
                request.readTimeout(timeout);
            }

            if (configs.containsKey(CONNECT_TIMEOUT) && null != configs.get(CONNECT_TIMEOUT)) {
                int timeout = (int) configs.get(CONNECT_TIMEOUT);
                request.connectTimeout(timeout);
            }

            if (configs.containsKey(HEADER_COOKIE) && null != configs.get(HEADER_COOKIE)) {
                Object value = configs.get(HEADER_COOKIE);
                if (value instanceof Map) {
                    request.cookies((Map<String, String>) value);
                } else {
                    request.cookies((String) value);
                }
            }

            if (configs.containsKey(HEADER_USER_AGENT) && null != configs.get(HEADER_USER_AGENT)) {
                request.userAgent((String) configs.get(HEADER_USER_AGENT));
            }

            if (configs.containsKey(HEADER_REFERER) && null != configs.get(HEADER_REFERER)) {
                request.referer((String) configs.get(HEADER_REFERER));
            }

            if (configs.containsKey(HEADER_ACCEPT_ENCODING) && null != configs.get(HEADER_ACCEPT_ENCODING)) {
                request.acceptEncoding((String) configs.get(HEADER_ACCEPT_ENCODING));
            }

            if (configs.containsKey(HEADER_ACCEPT_CHARSET) && null != configs.get(HEADER_ACCEPT_CHARSET)) {
                request.acceptCharset((String) configs.get(HEADER_ACCEPT_CHARSET));
            }

            if (configs.containsKey(HEADER_AUTHORIZATION) && null != configs.get(HEADER_AUTHORIZATION)) {
                request.authorization((String) configs.get(HEADER_AUTHORIZATION));
            }

            if (configs.containsKey(HEADER_PROXY_AUTHORIZATION) && null != configs.get(HEADER_PROXY_AUTHORIZATION)) {
                request.proxyAuthorization((String) configs.get(HEADER_PROXY_AUTHORIZATION));
            }

            if (configs.containsKey(HEADER_IF_NONE_MATCH) && null != configs.get(HEADER_IF_NONE_MATCH)) {
                request.ifNoneMatch((String) configs.get(HEADER_IF_NONE_MATCH));
            }

            if (configs.containsKey(HEADER_CONTENT_TYPE) && null != configs.get(HEADER_CONTENT_TYPE)) {
                request.contentType((String) configs.get(HEADER_CONTENT_TYPE));
            }

            if (configs.containsKey(HEADER_ACCEPT) && null != configs.get(HEADER_ACCEPT)) {
                request.accept((String) configs.get(HEADER_ACCEPT));
            }

            if (configs.containsKey(DATA) && null != configs.get(DATA)) {
                Object obj = configs.get(DATA);
                if (obj instanceof byte[]) {
                    request.send((byte[]) obj);
                } else if (obj instanceof InputStream) {
                    request.send((InputStream) obj);
                } else {
                    request.send(String.valueOf(obj));
                }
            }

            String ret = request.body();
            ctx.collect(ret);

            Thread.sleep(1000 * ((int) this.configs.getOrDefault(PERIOD, 60)));
        }
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStream<String> ds = env.addSource(JavaHttpSource.get(String.format("https://api.tophub.fun/v2/GetAllInfoGzip?id=%s&page=0&type=pc", "11")));
        ds.print();

        env.execute("flink http source");
    }
}
