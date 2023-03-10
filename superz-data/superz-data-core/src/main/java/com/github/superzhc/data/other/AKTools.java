package com.github.superzhc.data.other;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author superz
 * @create 2023/2/16 23:21
 */
public class AKTools {
    private static final Logger LOG = LoggerFactory.getLogger(AKTools.class);

    // private static Properties localeProperties = new Properties();
    private static Map<String, String> ch2enMap = new HashMap<>();

    static {
        synchronized (AKTools.class) {
            try {
                Properties localeProperties = new Properties();
                InputStream stream = AKTools.class.getClassLoader().getResourceAsStream("akshare.properties");
                localeProperties.load(stream);

                for (String key : localeProperties.stringPropertyNames()) {
                    String value = localeProperties.getProperty(key);
                    String[] arr = value.split(",");
                    for (String item : arr) {
                        if (null == item || item.trim().length() == 0) {
                            continue;
                        }

                        ch2enMap.put(item.trim(), key);
                    }
                }
                LOG.info("加载资源【{}】成功","akshare.properties");
            } catch (IOException e) {
                LOG.error("加载资源【{}】失败！", "akshare.properties", e);
                // ignore
            }
        }
    }

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

        if (null == maps || maps.length == 0) {
            return null;
        }

        List<Map<String, Object>> lst = new ArrayList<>(maps.length);

        for (Map<String, Object> map : maps) {
            Map<String, Object> newMap = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                key=key.trim();
                if (ch2enMap.containsKey(key)) {
                    key = ch2enMap.get(key);
                }
                newMap.put(key, entry.getValue());
            }
            lst.add(newMap);
        }

        return lst;
    }
}
