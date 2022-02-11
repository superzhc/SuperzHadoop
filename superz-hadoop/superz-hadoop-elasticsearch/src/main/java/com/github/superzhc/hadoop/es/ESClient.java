package com.github.superzhc.hadoop.es;

import java.io.Closeable;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.BasicHttpEntity;
import org.elasticsearch.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2020年04月21日 superz add
 */
public class ESClient implements Closeable
{
    private static final Logger logger = LoggerFactory.getLogger(ESClient.class);
    private static final String DEFAULT_PROTOCOL = "http";

    private HttpHost[] httpHosts;
    private RestClient client;
    /* Elasticsearch高级别客户端 */
    private RestHighLevelClient highLevelClient;

    public static ESClient create(String protocol, String host, Integer port) {
        return new ESClient(new HttpHost[] {new HttpHost(host, port, protocol) });
    }

    public static ESClient create(String host, Integer port) {
        return create(DEFAULT_PROTOCOL, host, port);
    }

    public ESClient(HttpHost... httpHost) {
        this.httpHosts = httpHost;
        RestClientBuilder builder = RestClient.builder(httpHost);
        this.highLevelClient = new RestHighLevelClient(builder);
        this.client = highLevelClient.getLowLevelClient();
    }

    @Override
    public void close() throws IOException {
        if (null != client) {
            client.close();
        }
        if (null != highLevelClient) {
            highLevelClient.close();
        }
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
        return delete(url, null);
    }

    public Response delete(String url, String json) {
        return execute("DELETE", url, json);
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
            if (null != json) {
                request.setJsonEntity(json);
            }

            if (logger.isDebugEnabled()) {
                logger.debug(request.toString() + (null == json ? "" : ",请求体内容：" + json));
            }
            return client.performRequest(request);
        }
        catch (Exception e) {
            logger.error("执行Elasticsearch的请求异常！", e);
            throw new RuntimeException(e);
        }
    }

    public RestClient getRestClient() {
        return client;
    }

    public RestHighLevelClient getHighLevelClient() {
        return highLevelClient;
    }
}
