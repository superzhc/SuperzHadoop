package com.github.superzhc.tool.task.jobs;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.data.mixture.XueQiu;
import com.github.superzhc.hadoop.kafka.MyStringProducer;
import com.github.superzhc.tool.task.cache.XueQiuCache;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/12/2 15:23
 **/
public class XueQiuJob implements Job {
    private static final String DEFAULT_TOPIC = "news_user_xueqiu";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String brokers = context.getMergedJobDataMap().getString("brokers");

        String topic = context.getMergedJobDataMap().getString("topic");
        if (null == topic || topic.trim().length() == 0) {
            topic = DEFAULT_TOPIC;
        }

        String userIds = context.getMergedJobDataMap().getString("users");
        if (null == userIds || userIds.trim().length() == 0) {
            throw new JobExecutionException("users不能为空，多个请用英文逗号隔开");
        }
        String[] userIdArr = userIds.split(",");

        try (MyStringProducer producer = new MyStringProducer(brokers)) {
            for (String userId : userIdArr) {
                List<Map<String, Object>> lst = XueQiu.userArticle(userId);
                for (Map<String, Object> map : lst) {
                    String id = String.valueOf(map.get("id"));
                    Object v = XueQiuCache.getInstance().getCache().getIfPresent(id);
                    if (null != v) {
                        continue;
                    }
                    XueQiuCache.getInstance().getCache().put(id, true);

                    String key = String.valueOf(map.get("user_id"));
                    String value = JsonUtils.asString(map);
                    producer.send(topic, key, value);
                }
            }
        } catch (IOException e) {
            throw new JobExecutionException(e);
        }
    }
}
