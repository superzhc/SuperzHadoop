package com.github.superzhc.hadoop.clickhouse.client;

import com.github.superzhc.common.http.HttpRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/4/10 16:27
 **/
public class HttpClientTest {
    HttpClient client;

    @Before
    public void setUp() throws Exception {
        client = new HttpClient("10.90.18.76", 18123);
    }

    @Test
    public void testPing() {
        System.out.println(client.ping());
    }

    @Test
    public void testQuery() {
        // client.setFormat("JSONEachRow");
        String result = client.query("SELECT 1");
        System.out.println(result);
    }

    // note：用法很奇葩，不推荐使用
//    @Test
//    public void queryParams() {
//        String url = fullUrl();
//
//        Map<String, Object> params = new HashMap<>();
//        // 注意：参数必须使用param_开头
//        params.put("param_id", 1);
//        params.put("param_name", "2");
//
//        // 列名是区分大小写的
//        String data = "SELECT * FROM (SELECT 1 AS col1,'2' AS col2) WHERE col1={id:UInt8} AND col2={name:String}";
//
//        HttpRequest.get(url, params).send(data).body();
//    }

//    @Test
//    public void test(){
//        //String url="http://10.90.18.76:18123/?query_id=f58ee041-de77-47e2-909e-6822ee645862&database=my_dw&default_format=TabSeparatedWithNamesAndTypes";
//        String url="http://10.90.18.76:18123/?database=my_dw&default_format=TabSeparatedWithNamesAndTypes";
//
//        String result=HttpRequest.get(url).body();
//        System.out.println(result);
//    }
}
