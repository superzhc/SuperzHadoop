package com.github.superzhc.reader.datasource.impl;

import com.github.superzhc.reader.datasource.Datasource;

import java.util.Map;
import java.util.Properties;

/**
 * @author superz
 * @create 2021/8/16 17:00
 */
public class JDBCDatasource extends Datasource {

    public JDBCDatasource(Map<String, Object> params) {
        super(params);
    }

    public String getDriver() {
        return (String) params.get("driver");
    }

    public String getUrl() {
        return (String) params.get("url");
    }

    public String getUsername() {
        return (String) params.get("user");
    }

    public String getPassword() {
        return (String) params.get("password");
    }

    public Properties info() {
        return convert("driver", "url");
    }
}
