package com.github.superzhc.common.http.okhttp3;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author superz
 * @create 2021/7/30 21:52
 */
public class OkHttpUtils {
    private static final String HTTP_JSON = "application/json; charset=utf-8";
    // private static final String HTTP_FORM = "application/x-www-form-urlencoded; charset=utf-8";

    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";
    private static final String HTTP_PUT = "PUT";
    private static final String HTTP_DELETE = "DELETE";
    private static final String HTTP_HEAD = "HEAD";

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build();

    public static OkHttpResponse request(String url, String method, Object params) throws IOException {
        return request(url, method, null, params);
    }

    public static OkHttpResponse request(String url, String method, Map<String, String> headers, /* Map<String, String> */ Object params) throws IOException {
        OkHttpResponse response = null;
        switch (method.toUpperCase()) {
            case HTTP_POST:
                if (null == params) {
                    response = postForm(url, headers, null);
                } else if (params instanceof Map) {
                    response = postForm(url, headers, (Map<String, String>) params);
                } else {
                    response = postJson(url, headers, params.toString());
                }
                break;
            case HTTP_PUT:
                if (null == params) {
                    response = putForm(url, headers, null);
                } else if (params instanceof Map) {
                    response = putForm(url, headers, (Map<String, String>) params);
                } else {
                    response = putJson(url, headers, params.toString());
                }
                break;
            case HTTP_DELETE:
                response = delete(url, headers);
                break;
            case HTTP_HEAD:
                response = head(url, headers);
                break;
            case HTTP_GET:
            default:
                response = get(url, headers);
                break;
        }
        return response;
    }

    public static OkHttpResponse get(String url) throws IOException {
        return get(url, null);
    }

    public static OkHttpResponse get(String url, Map<String, String> headers) throws IOException {
        return get(url, headers, null);
    }

    public static OkHttpResponse get(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        if (null != params && params.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                sb.append("&").append(param.getKey()).append("=").append(param.getValue());
            }
            url = url.contains("?") ? url + sb.toString() : url + "?" + sb.substring(1);
        }
        Request request = baseBuilder(url, headers).get().build();
        return execute(request);
    }

    public static OkHttpResponse postJson(String url, String json) throws IOException {
        return postJson(url, null, json);
    }

    public static OkHttpResponse postJson(String url, Map<String, String> headers, String json) throws IOException {
        RequestBody requestBody = RequestBody.create(MediaType.parse(HTTP_JSON), json);
        return post(url, headers, requestBody);
    }

    public static OkHttpResponse postForm(String url, Map<String, String> params) throws IOException {
        return postForm(url, null, params);
    }

    public static OkHttpResponse postForm(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        // 构建Form参数
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (null != params) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                formBuilder.add(param.getKey(), param.getValue());
            }
        }
        return post(url, headers, formBuilder.build());
    }

    public static OkHttpResponse post(String url, Map<String, String> headers, RequestBody requestBody) throws IOException {
        Request request = baseBuilder(url, headers)
                .post(requestBody)
                .build();

        return execute(request);
    }

    public static OkHttpResponse putJson(String url, String json) throws IOException {
        return putJson(url, null, json);
    }

    public static OkHttpResponse putJson(String url, Map<String, String> headers, String json) throws IOException {
        RequestBody requestBody = RequestBody.create(MediaType.parse(HTTP_JSON), json);
        return put(url, headers, requestBody);
    }

    public static OkHttpResponse putForm(String url, Map<String, String> params) throws IOException {
        return putForm(url, null, params);
    }

    public static OkHttpResponse putForm(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        // 构建Form参数
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (null != params) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                formBuilder.add(param.getKey(), param.getValue());
            }
        }
        return put(url, headers, formBuilder.build());
    }

    public static OkHttpResponse put(String url, Map<String, String> headers, RequestBody requestBody) throws IOException {
        Request request = baseBuilder(url, headers)
                .put(requestBody)
                .build();

        return execute(request);
    }

    public static OkHttpResponse delete(String url) throws IOException {
        return delete(url, null);
    }

    public static OkHttpResponse delete(String url, Map<String, String> headers) throws IOException {
        Request request = baseBuilder(url, headers)
                .delete()
                .build();
        return execute(request);
    }

    public static OkHttpResponse head(String url) throws IOException {
        return head(url, null);
    }

    public static OkHttpResponse head(String url, Map<String, String> headers) throws IOException {
        Request request = baseBuilder(url, headers)
                .head()
                .build();
        return execute(request);
    }

    private static Request.Builder baseBuilder(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (null != headers && 0 < headers.size()) {
            headers.forEach((String key, String value) -> builder.addHeader(key, value));
        }
        return builder;
    }

    private static OkHttpResponse execute(Request request) throws IOException {
        try (Response response = okHttpClient.newCall(request).execute()) {
            OkHttpResponse response1 = new OkHttpResponse(response.code(), response.body().string());
            return response1;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OkHttpResponse {
        private Integer code;
        private String body;

        @Override
        public String toString() {
            return "OkHttpResponse{" +
                    "code=" + code + "\n" +
                    ", body=" + body +
                    '}';
        }
    }
}
