package com.github.superzhc.hadoop.es.utils;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author superz
 * @create 2023/5/5 11:16
 **/
public class ESUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ESUtils.class);

    private RestClient client;

    public ESUtils(RestClient client) {
        this.client = client;
    }

    public Response get(String url) {
        return execute("GET", url, null);
    }

    public Response get(String url, String json) {
        return execute("GET", url, json);
    }

    public Response post(String url, String json) {
        return execute("POST", url, json);
    }

    public Response put(String url, String json) {
        return execute("PUT", url, json);
    }

    public Response head(String url) {
        return execute("HEAD", url, null);
    }

    public Response delete(String url) {
        return delete(url, null);
    }

    public Response delete(String url, String json) {
        return execute("DELETE", url, json);
    }

    public Response execute(String method, String url, String json) {
        try {
            Request request = new Request(method, url);
            if (null != json) {
                request.setJsonEntity(json);
            }

            LOG.debug("[{} {}] {}", request.getMethod(), request.getEndpoint(), (null == json ? "" : json));

            Response response = client.performRequest(request);

            LOG.debug("[{} {}] {}", response.getRequestLine().getMethod(),response.getRequestLine().getUri(), response.getStatusLine());

            return response;
        } catch (Exception e) {
            LOG.error("执行Elasticsearch的请求异常！", e);
            throw new RuntimeException(e);
        }
    }
}
