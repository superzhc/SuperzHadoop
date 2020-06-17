package com.github.superzhc.es;

import java.io.Closeable;
import java.io.IOException;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

/**
 * 2020年04月21日 superz add
 */
public class ESClient implements Closeable
{
    private static final String DEFAULT_PROTOCOL = "http";

    private HttpHost[] httpHosts;
    private RestClient client;

    public ESClient(String protocol, String host, Integer port) {
        httpHosts = new HttpHost[] {new HttpHost(host, port, protocol) };
        this.client = RestClient.builder(httpHosts).build();
    }

    public ESClient(String host, Integer port) {
        this(DEFAULT_PROTOCOL, host, port);
    }

    public ESClient(HttpHost... httpHost) {
        this.httpHosts = httpHost;
        this.client = RestClient.builder(httpHost).build();
    }

    @Override
    public void close() throws IOException {
        if (null != client)
            client.close();
    }

    public void ping() {
        Response response = get("/");
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
        return execute("DELETE", url, null);
    }

    public Response execute(String method, String url, String json) {
        try {
            // 请求参数不允许为空
            // 6.3.2 版本
            // Map<String, String> params = Collections.emptyMap();
            // HttpEntity entity = null;
            // if (null != json) {
            // entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
            // }
            // return client.performRequest(method, url, params, entity);

            // 7.1.1 版本
            Request request = new Request(method, url);
            if (null != json)
                request.setJsonEntity(json);
            return client.performRequest(request);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RestClient getRestClient() {
        return client;
    }
}
