package com.github.superzhc.data.common;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author superz
 * @create 2021/12/9 10:49
 */
public abstract class HttpData {
    private static final String HTTP_JSON = "application/json; charset=utf-8";

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build();

    protected ResultT get(String url) {
        return get(url, null);
    }

    protected ResultT get(String url, Map<String, String> headers) {
        return get(url, headers, null);
    }

    protected ResultT get(String url, Map<String, String> headers, Map<String, String> params) {
        if (null != params && params.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                sb.append("&").append(param.getKey()).append("=").append(param.getValue());
            }
            url = url.contains("?") ? url + sb.toString() : url + "?" + sb.substring(1);
        }
        Request request = commonBuilder(url, headers).get().build();
        return execute(request);
    }

    protected ResultT post(String url, Map<String, String> form) {
        return post(url, null, form);
    }

    protected ResultT post(String url, Map<String, String> headers, Map<String, String> form) {
        // 构建Form参数
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (null != form) {
            for (Map.Entry<String, String> param : form.entrySet()) {
                formBuilder.add(param.getKey(), param.getValue());
            }
        }
        return post(url, headers, formBuilder.build());
    }

    protected ResultT post(String url, String json) {
        return post(url, null, json);
    }

    protected ResultT post(String url, Map<String, String> headers, String json) {
        RequestBody requestBody = RequestBody.create(json, MediaType.parse(HTTP_JSON));
        return post(url, headers, requestBody);
    }

    protected ResultT post(String url, Map<String, String> headers, RequestBody body) {
        Request request = commonBuilder(url, headers).post(body).build();
        return execute(request);
    }

    protected Request.Builder commonBuilder(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (null != headers && 0 < headers.size()) {
            headers.forEach((String key, String value) -> builder.addHeader(key, value));
        }
        return builder;
    }

    protected ResultT execute(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            if(!response.isSuccessful()){
                return ResultT.fail(response.message());
            }

            return ResultT.success(response.body().string());
        } catch (IOException e) {
            return ResultT.fail(e);
        }
    }
}
