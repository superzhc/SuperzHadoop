package com.github.superzhc.tool.task.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * @author superz
 * @create 2022/12/1 15:11
 **/
public class GuangDiuCache {
    private static final Logger log = LoggerFactory.getLogger(GuangDiuCache.class);

    private volatile static GuangDiuCache instance = null;
    private Cache<String, Object> cache;

    private GuangDiuCache() {
        cache = Caffeine.newBuilder()
                .maximumSize(100000) //为缓存容量指定特定的大小，当缓存容量超过指定的大小，缓存将尝试逐出最近或经常未使用的条目
                .expireAfterWrite(Duration.ofHours(3)) //写入后3小时过期
                .removalListener((String key, Object value, RemovalCause cause) -> {
                    log.debug("Key [{}] was removed,reason:{}", key, cause);
                })
                .build();
    }

    public static GuangDiuCache getInstance() {
        if (null == instance) {
            synchronized (GuangDiuCache.class) {
                if (null == instance) {
                    instance = new GuangDiuCache();
                }
            }
        }
        return instance;
    }

    public Cache<String, Object> getCache() {
        return cache;
    }
}
