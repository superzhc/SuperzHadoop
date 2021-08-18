package com.github.superzhc.reader.datasource;

import java.util.Map;
import java.util.Properties;

/**
 * @author superz
 * @create 2021/8/16 16:57
 */
public class Datasource {
    protected Map<String, Object> params;

    public Datasource(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getParams() {
        return this.params;
    }

    public Properties convert(String... excludes) {
        Properties properties = new Properties();
        if (null == excludes || excludes.length == 0) {
            properties.putAll(this.params);
        } else {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                for (String exclude : excludes) {
                    if (exclude.equals(param.getKey())) {
                        break;
                    }
                }
                properties.put(param.getKey(), param.getValue());
            }
        }
        return properties;
    }
}
