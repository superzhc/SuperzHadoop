package com.github.superzhc.common.httpclient;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/3/21 14:08
 **/
public class HttpClientUtils {
    // 编码格式。发送编码格式统一用UTF-8
    private static final String ENCODING = "UTF-8";

    // 设置连接超时时间，单位毫秒。
    private static final int CONNECT_TIMEOUT = 6000;

    // 请求获取数据的超时时间(即响应时间)，单位毫秒。
    private static final int SOCKET_TIMEOUT = 6000;

    /**
     * 发送get请求；不带请求头和请求参数
     *
     * @param url 请求地址
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, null, null);
    }

    /**
     * 发送get请求；带请求参数
     *
     * @param url    请求地址
     * @param params 请求参数集合
     * @return
     */
    public static String doGet(String url, Map<String, String> params) {
        return doGet(url, null, params);
    }

    /**
     * 发送get请求；带请求头和请求参数
     *
     * @param url     请求地址
     * @param headers 请求头集合
     * @param params  请求参数集合
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, String> headers, Map<String, String> params) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null) {
                Set<Map.Entry<String, String>> entrySet = params.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            return body(httpGet, headers);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 发送post请求；不带请求头和请求参数
     *
     * @param url 请求地址
     * @return
     */
    public static String doPost(String url) {
        return doPost(url, null, (Map<String, String>) null);
    }

    /**
     * 发送post请求；带请求参数
     *
     * @param url    请求地址
     * @param params 参数集合
     * @return
     */
    public static String doPost(String url, Map<String, String> params) {
        return doPost(url, null, params);
    }

    /**
     * 发送post请求；带请求头和请求参数
     *
     * @param url     请求地址
     * @param headers 请求头集合
     * @param params  请求参数集合
     * @return
     */
    public static String doPost(String url, Map<String, String> headers, Map<String, String> params) {
        HttpPost httpPost = new HttpPost(url);
        return body(httpPost, headers, params);
    }

    public static String doPost(String url, String json) {
        return doPost(url, null, json);
    }

    public static String doPost(String url, Map<String, String> headers, String json) {
        HttpPost httpPost = new HttpPost(url);
        return body(httpPost, headers, json);
    }

    /**
     * 发送put请求；不带请求参数
     *
     * @param url 请求地址
     * @return
     */
    public static String doPut(String url) {
        return doPut(url, null);
    }

    /**
     * 发送put请求；带请求参数
     *
     * @param url    请求地址
     * @param params 参数集合
     * @return
     */
    public static String doPut(String url, Map<String, String> params) {
        HttpPut httpPut = new HttpPut(url);
        return body(httpPut, null, params);
    }

    /**
     * 发送delete请求；不带请求参数
     *
     * @param url 请求地址
     * @return
     */
    public static String doDelete(String url) {
        HttpDelete httpDelete = new HttpDelete(url);
        return body(httpDelete);
    }

    /**
     * 发送delete请求；带请求参数
     *
     * @param url    请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public static String doDelete(String url, Map<String, String> params) throws Exception {
        if (params == null) {
            params = new HashMap<String, String>();
        }

        params.put("_method", "delete");
        return doPost(url, params);
    }

    private static String body(HttpEntityEnclosingRequestBase httpMethod, Map<String, String> headers, Map<String, String> params) {
        try {
            if (params != null) {
                List<NameValuePair> nvps = new ArrayList<>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }

                // 设置到请求的http对象中
                httpMethod.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
            }

            return body(httpMethod, headers);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String body(HttpEntityEnclosingRequestBase httpMethod, Map<String, String> headers, String json) {
        try {
            if (null != json && json.trim().length() > 0) {
                httpMethod.setEntity(new StringEntity(json));
            }
            return body(httpMethod, headers);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String body(HttpRequestBase httpMethod, Map<String, String> headers) {
        // 封装请求头
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(header.getKey(), header.getValue());
            }
        }

        return body(httpMethod);
    }

    public static String body(HttpRequestBase httpMethod) {
        // 创建 HttpClient 对象
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            /**
             * setConnectTimeout：设置连接超时时间，单位毫秒。
             * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
             * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
             */
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    .build();
            httpMethod.setConfig(requestConfig);

            // 创建 httpResponse 对象
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpMethod)) {
                // 获取返回结果
                if (httpResponse != null && httpResponse.getStatusLine() != null) {
                    if (httpResponse.getEntity() != null) {
                        return EntityUtils.toString(httpResponse.getEntity(), ENCODING);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] bytes(HttpRequestBase httpMethod, Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(header.getKey(), header.getValue());
            }
        }

        return bytes(httpMethod);
    }

    public static byte[] bytes(HttpRequestBase httpMethod) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            /**
             * setConnectTimeout：设置连接超时时间，单位毫秒。
             * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
             * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
             */
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    .build();
            httpMethod.setConfig(requestConfig);

            // 创建 httpResponse 对象
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpMethod)) {
                // 获取返回结果
                if (httpResponse != null && httpResponse.getStatusLine() != null) {
                    if (httpResponse.getEntity() != null) {
                        return EntityUtils.toByteArray(httpResponse.getEntity());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void stream(HttpRequestBase httpMethod, Map<String, String> headers, Function<InputStream,Void> function) {
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(header.getKey(), header.getValue());
            }
        }
        stream(httpMethod,function);
    }

    public static void stream(HttpRequestBase httpMethod, Function<InputStream,Void> function) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            /**
             * setConnectTimeout：设置连接超时时间，单位毫秒。
             * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
             * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
             */
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    .build();
            httpMethod.setConfig(requestConfig);

            // 创建 httpResponse 对象
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpMethod)) {
                // 获取返回结果
                if (httpResponse != null && httpResponse.getStatusLine() != null) {
                    if (httpResponse.getEntity() != null) {
                        InputStream stream= httpResponse.getEntity().getContent();
                        function.apply(stream);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
