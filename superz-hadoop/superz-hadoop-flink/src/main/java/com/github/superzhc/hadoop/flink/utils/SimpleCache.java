package com.github.superzhc.hadoop.flink.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author superz
 * @create 2021/10/28 1:17
 */
public class SimpleCache {
    private static final Map<String, Object> cache = new ConcurrentHashMap<>();

    public static Object get(String key) {
        return cache.get(key);
    }

    public static Object getOrDefault(String key, Object defaultValue) {
        return cache.getOrDefault(key, defaultValue);
    }

    public static void set(String key, Object value) {
        cache.put(key, value);
    }
}
