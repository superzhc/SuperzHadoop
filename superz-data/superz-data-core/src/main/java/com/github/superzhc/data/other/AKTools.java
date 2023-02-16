package com.github.superzhc.data.other;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/2/16 23:21
 */
public class AKTools {
    private static final Logger LOG = LoggerFactory.getLogger(AKTools.class);

    private static final Integer DEFAULT_PORT = 8080;

    private String host;

    private Integer port;

    public AKTools(String host) {
        this(host, DEFAULT_PORT);
    }

    public AKTools(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public List<Map<String, Object>> get(String api) {
        return get(api, null);
    }

    public List<Map<String, Object>> get(String api, Map<String, Object> params) {
        String url = String.format("http://%s:%d/api/public/%s", host, port, api);

        String result = HttpRequest.get(url, params).body();
        JsonNode json = JsonUtils.loads(result);
        Map<String, Object>[] maps = JsonUtils.newObjectArray(json);
        return Arrays.asList(maps);
    }
}
