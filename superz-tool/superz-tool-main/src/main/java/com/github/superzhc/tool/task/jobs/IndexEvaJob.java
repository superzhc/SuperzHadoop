package com.github.superzhc.tool.task.jobs;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.data.index.DanJuanIndex;
import com.github.superzhc.hadoop.kafka.MyProducer;
import com.github.superzhc.hadoop.kafka.MyStringProducer;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/11/21 0:01
 */
public class IndexEvaJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(IndexEvaJob.class);

    private static final String BROKERS = "127.0.0.1:19092";
    private static final String TOPIC = "index_danjuan_eva";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String broker = context.getMergedJobDataMap().getString("brokers");
        String topic = context.getMergedJobDataMap().getString("topic");
        if (null == topic || topic.trim().length() == 0) {
            topic = TOPIC;
        }

        List<Map<String, Object>> data = DanJuanIndex.indexEva();

        try (MyStringProducer producer = new MyStringProducer(broker)) {
            int len = data.size();
            for (int i = 1; i <= len; i++) {
                Map<String, Object> item = data.get(len - i);
                String key = String.valueOf(item.get("index_code"));
                String value = JsonUtils.asString(item);
                producer.send(topic, key, value);
            }
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        List<Map<String, Object>> data = DanJuanIndex.indexEva();

        try (MyProducer producer = new MyProducer(BROKERS)) {
            int len = data.size();
            for (int i = 1; i <= len; i++) {
                Map<String, Object> item = data.get(len - i);
                String key = String.valueOf(item.get("index_code"));
                String value = JsonUtils.asString(item);
                producer.send(TOPIC, key, value);
            }
        }
    }
}
