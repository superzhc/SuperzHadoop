package com.github.superzhc.hadoop.datahub.api;

import com.github.superzhc.common.base.Preconditions;
import com.github.superzhc.common.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/4/13 17:24
 **/
public class OpenAPI {
    private static final Logger LOG = LoggerFactory.getLogger(OpenAPI.class);

    private static final Integer DEFAULT_GMS_PORT = 8080;

    private String host;
    private Integer port;

    public OpenAPI(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String entities(String... urns) {
        Preconditions.checkArgument(null != urns && urns.length > 0);

        String url = url("/entities/v1/latest");

        Map<String, Object> params = new HashMap<>();
        params.put("urns", urns);

        String result = HttpRequest.get(url, params).body();
        return result;
    }

    private String url(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        String url = String.format("http://%s:%d/openapi%s", host, port, path);
        return url;
    }
}
