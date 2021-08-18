package com.github.superzhc.reader.datasource.impl;

import com.github.superzhc.reader.datasource.Datasource;

import java.util.Map;

/**
 * @author superz
 * @create 2021/8/17 19:26
 */
public class MqttDatasource extends Datasource {
    public MqttDatasource(Map<String, Object> params) {
        super(params);
    }

    public String broker() {
        return (String) params.get("broker");
    }

    public String clientId() {
        return (String) params.get("clientId");
    }

    public String username() {
        return (String) params.get("username");
    }

    public String password() {
        return (String) params.get("password");
    }

    public Integer qos() {
        return Integer.valueOf(params.get("qos").toString());
    }
}
