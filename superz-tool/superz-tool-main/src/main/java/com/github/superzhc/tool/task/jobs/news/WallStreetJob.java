package com.github.superzhc.tool.task.jobs.news;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.LocalDateTimeUtils;
import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.mixture.WallStreet;
import com.github.superzhc.hadoop.kafka.MyStringProducer;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author superz
 * @create 2022/12/6 16:22
 **/
public class WallStreetJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(WallStreetJob.class);

    public static class JobCache {

        private volatile static JobCache instance = null;
        private Cache<String, Object> cache;

        private JobCache() {
            cache = Caffeine.newBuilder()
                    .maximumSize(100000) //为缓存容量指定特定的大小，当缓存容量超过指定的大小，缓存将尝试逐出最近或经常未使用的条目
                    .expireAfterWrite(Duration.ofHours(5))
                    .removalListener((String key, Object value, RemovalCause cause) -> {
                        log.debug("Key [{}] was removed,reason:{}", key, cause);
                    })
                    .build();
        }

        public static JobCache getInstance() {
            if (null == instance) {
                synchronized (JobCache.class) {
                    if (null == instance) {
                        instance = new JobCache();
                    }
                }
            }
            return instance;
        }

        public Cache<String, Object> getCache() {
            return cache;
        }
    }

    private static final String DEFAULT_TOPIC = "news_wallstreet";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String brokers = context.getMergedJobDataMap().getString("brokers");
        String topic = context.getMergedJobDataMap().getString("topic");
        if (null == topic || topic.trim().length() == 0) {
            topic = DEFAULT_TOPIC;
        }

        try (MyStringProducer producer = new MyStringProducer(brokers)) {
            List<Map<String, Object>> data = WallStreet.news();
            for (int i = data.size() - 1; i > -1; i--) {
                Map<String, Object> item = data.get(i);
                String id = String.valueOf(item.get("id"));
                if (null != JobCache.getInstance().getCache().getIfPresent(id)) {
                    continue;
                }
                JobCache.getInstance().getCache().put(id, true);

                preprocessing(item);

                producer.send(topic, id, JsonUtils.asString(item));
            }
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    private void preprocessing(final Map<String, Object> item) {
        // 去掉无用key
        MapUtils.removeKeys(item, "article", "comment_count", "ban_comment", "channels", "fund_codes", "cover_images", "is_calendar", "is_favourite", "is_priced", "is_scaling", "reference", "related_themes", "symbols", "tags", "calendar_key", "wscn_ticker");

        item.replaceAll(new BiFunction<String, Object, Object>() {
            @Override
            public Object apply(String s, Object o) {
                if (o instanceof String) {
                    String str = (String) o;
                    str = str.trim().replaceAll("[\r\n\t]", "");
                    return str;
                } else {
                    return o;
                }
            }
        });

        String authorInfo = (String) item.get("author");
        item.put("author", null == authorInfo ? null : JsonUtils.string(authorInfo, "display_name"));
        item.put("display_time", LocalDateTimeUtils.convert4secondTimestamp((int) item.get("display_time")));
    }

    public static void main(String[] args) {
        List<Map<String, Object>> data = WallStreet.news();

        WallStreetJob wallStreetJob = new WallStreetJob();
        for (Map<String, Object> item : data) {
            wallStreetJob.preprocessing(item);
        }
        System.out.println(MapUtils.print(data));
    }
}
