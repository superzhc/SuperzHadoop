package com.github.superzhc.xxljob.handler.param;

import lombok.Data;

import java.util.Map;

/**
 * @author superz
 * @create 2021/7/30 21:07
 */
@Data
public class HttpRequestParam {
    private String url;
    private String method;
    private Map<String,String> headers;
    private Map<String,String> params;
}
