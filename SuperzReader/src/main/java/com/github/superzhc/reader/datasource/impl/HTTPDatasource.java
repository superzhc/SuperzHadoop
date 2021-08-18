package com.github.superzhc.reader.datasource.impl;

import com.github.superzhc.reader.datasource.Datasource;

import java.util.Map;

/**
 * @author superz
 * @create 2021/8/17 10:10
 */
public class HTTPDatasource extends Datasource {
    private String url;
    private String method;
    private Map<String, String> headers;
    private String data;

    public HTTPDatasource(Map<String, Object> params) {
        super(params);
    }
}
