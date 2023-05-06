package com.github.superzhc.hadoop.es.utils;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author superz
 * @create 2023/5/6 15:25
 **/
public class ESClientInitUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ESClientInitUtils.class);

    public static RestClient create(String scheme, String host, Integer port, String username, String password) {
        HttpHost httpHost = new HttpHost(host, port, scheme);
        return create(username, password, httpHost);
    }

    public static RestClient create(String uris, String username, String password) {
        String[] uriArr = uris.split(",");
        HttpHost[] httpHosts = new HttpHost[uriArr.length];
        for (int i = 0, len = uriArr.length; i < len; i++) {
            httpHosts[i] = HttpHost.create(uriArr[i]);
        }
        return create(username, password, httpHosts);
    }

    private static RestClient create(String username, String password, HttpHost... httpHosts) {
        RestClientBuilder builder = RestClient.builder(httpHosts);
        if (null != username && username.trim().length() > 0 && null != password && password.trim().length() > 0) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

            builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                @Override
                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                    return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }
            });
        }
        return builder.build();
    }
}
