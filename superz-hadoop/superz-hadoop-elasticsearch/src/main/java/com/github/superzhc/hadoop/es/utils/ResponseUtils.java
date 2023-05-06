package com.github.superzhc.hadoop.es.utils;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;

/**
 * 2020年06月17日 superz add
 */
public class ResponseUtils {
    public static String getEntity(Response response) {
        try {
            if (null == response.getEntity()) {
                return null;
            } else {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}