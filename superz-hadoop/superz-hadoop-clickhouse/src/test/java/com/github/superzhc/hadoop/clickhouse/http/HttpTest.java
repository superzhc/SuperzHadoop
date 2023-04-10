package com.github.superzhc.hadoop.clickhouse.http;

import com.github.superzhc.common.http.HttpRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/4/10 16:27
 **/
public class HttpTest {
    String baseUrl = null;

    @Before
    public void setUp() throws Exception {
        baseUrl = "http://127.0.0.1:8123";
    }

    private String fullUrl() {
        return fullUrl("/");
    }

    private String fullUrl(String path) {
        return String.format("%s%s", baseUrl, path);
    }

    @Test
    public void health() {
        String url = fullUrl("/");
        HttpRequest.get(url).body();
    }

    @Test
    public void ping() {
        String url = fullUrl("/ping");
        HttpRequest.get(url).body();
    }

    @Test
    public void query() {
        String url = fullUrl("/");

        Map<String, String> params = new HashMap<>();
        params.put("query", "SELECT 1");

        HttpRequest.get(url, params).body();
    }

    // note：用法很奇葩，不推荐使用
//    @Test
    public void queryParams() {
        String url = fullUrl();

        Map<String, Object> params = new HashMap<>();
        // 注意：参数必须使用param_开头
        params.put("param_id", 1);
        params.put("param_name", "2");

        // 列名是区分大小写的
        String data = "SELECT * FROM (SELECT 1 AS col1,'2' AS col2) WHERE col1={id:UInt8} AND col2={name:String}";

        HttpRequest.get(url, params).send(data).body();
    }
}
