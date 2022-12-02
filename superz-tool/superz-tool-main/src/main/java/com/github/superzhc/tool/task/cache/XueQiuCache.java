package com.github.superzhc.tool.task.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author superz
 * @create 2022/12/2 14:55
 **/
public class XueQiuCache {
    private static final Logger log = LoggerFactory.getLogger(XueQiuCache.class);

    private volatile static XueQiuCache instance = null;
    private Cache<String, Object> cache;

    private XueQiuCache() {
        cache = Caffeine.newBuilder()
                .maximumSize(100000) //为缓存容量指定特定的大小，当缓存容量超过指定的大小，缓存将尝试逐出最近或经常未使用的条目
                // .expireAfterWrite(Duration.ofHours(3)) //写入后3小时过期
                .removalListener((String key, Object value, RemovalCause cause) -> {
                    log.debug("Key [{}] was removed,reason:{}", key, cause);
                })
                .build();
    }

    public static XueQiuCache getInstance() {
        if (null == instance) {
            synchronized (XueQiuCache.class) {
                if (null == instance) {
                    instance = new XueQiuCache();
                }
            }
        }
        return instance;
    }

    public Cache<String, Object> getCache() {
        return cache;
    }
}
