package com.github.superzhc.hadoop.datahub.api;

import com.github.superzhc.common.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author superz
 * @create 2023/4/13 17:24
 **/
public class GraphQLAPI {
    private static final Logger LOG = LoggerFactory.getLogger(GraphQLAPI.class);

    private static final Integer DEFAULT_GMS_PORT = 8080;

    private String host;
    private Integer port;

    public GraphQLAPI(String host) {
        this(host, DEFAULT_GMS_PORT);
    }

    public GraphQLAPI(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String execute(String path) {
        String url = String.format("http://%s:%d/api/graphql/%s", host, port, path);

        String result= HttpRequest.get(url).body();
        return result;
    }
}
