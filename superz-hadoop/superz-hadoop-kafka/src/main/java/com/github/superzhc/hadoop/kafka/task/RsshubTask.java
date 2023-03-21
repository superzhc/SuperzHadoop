package com.github.superzhc.hadoop.kafka.task;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.news.Rsshub;
import com.github.superzhc.hadoop.kafka.MyAdminClient;
import com.github.superzhc.hadoop.kafka.MyStringProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author superz
 * @create 2023/3/20 16:46
 **/
public class RsshubTask {

    public static class RsshubThread implements Runnable {
        private static final Logger LOG = LoggerFactory.getLogger(RsshubThread.class);

        private final MyStringProducer producer;

        private String topic;

        private final Rsshub api;

        private String path;

        private Map<String, Object> params = null;

        public RsshubThread(MyStringProducer producer, String topic, Rsshub api, String path) {
            this(producer, topic, api, path, null);
        }

        public RsshubThread(MyStringProducer producer, String topic, Rsshub api, String path, Map<String, Object> params) {
            this.producer = producer;
            this.topic = topic;
            this.api = api;
            this.path = path;
            this.params = params;
        }

        @Override
        public void run() {
            List<Map<String, Object>> data = api.get(path, params);
            try {
                String key = path.replace('/', '_');
                for (Map<String, Object> item : data) {
                    item.put("rsshubKey", key);
                    producer.send(topic, key, JsonUtils.asString(item));
                }
            } catch (Exception e) {
                //ignore
                LOG.error("发送异常", e);
            }
        }
    }

    public static void main(String[] args) {
        final Rsshub api = new Rsshub("http://127.0.0.1:1200", "127.0.0.1", 10809);
//        List<Map<String, Object>> data = api.get("/caijing/roll");
//        System.out.println(MapUtils.print(data));

        String brokers = "127.0.0.1:19092";
        String topic = "rsshub_finance";
        try (MyAdminClient admin = new MyAdminClient(brokers)) {
            if (!admin.exist(topic)) {
                admin.create(topic, 10, (short) 1, null);
            }
        }


        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        final MyStringProducer producer = new MyStringProducer(brokers);

        // 智通财经网
        executor.scheduleWithFixedDelay(new RsshubThread(producer, topic, api, "/zhitongcaijing/ajj"), 0, 30, TimeUnit.MINUTES);
        executor.scheduleWithFixedDelay(new RsshubThread(producer, topic, api, "/zhitongcaijing/focus"), 0, 30, TimeUnit.MINUTES);
        executor.scheduleWithFixedDelay(new RsshubThread(producer, topic, api, "/zhitongcaijing/research"), 0, 1, TimeUnit.DAYS);
        // 证券时报网
        executor.scheduleWithFixedDelay(new RsshubThread(producer, topic, api, "/stcn/fund"), 0, 3, TimeUnit.HOURS);
        executor.scheduleWithFixedDelay(new RsshubThread(producer, topic, api, "/stcn/data"), 0, 12, TimeUnit.HOURS);
        executor.scheduleWithFixedDelay(new RsshubThread(producer, topic, api, "/stcn/finance"), 0, 6, TimeUnit.HOURS);
        // 新浪
        executor.scheduleWithFixedDelay(new RsshubThread(producer, topic, api, "/sina/finance"), 0, 1, TimeUnit.HOURS);
        // 雪球
        executor.scheduleWithFixedDelay(new RsshubThread(producer, topic, api, "/xueqiu/today"), 0, 30, TimeUnit.MINUTES);
        // 格隆汇
        executor.scheduleWithFixedDelay(new RsshubThread(producer, topic, api, "/gelonghui/home/fund"), 0, 1, TimeUnit.HOURS);
        executor.scheduleWithFixedDelay(new RsshubThread(producer, topic, api, "/gelonghui/live"), 0, 20, TimeUnit.MINUTES);
        executor.scheduleWithFixedDelay(new RsshubThread(producer, topic, api, "/gelonghui/hot-article"), 0, 20, TimeUnit.MINUTES);
        // 财联社
        executor.scheduleWithFixedDelay(new RsshubThread(producer, topic, api, "/cls/telegraph/fund"), 0, 3, TimeUnit.HOURS);
        executor.scheduleWithFixedDelay(new RsshubThread(producer, topic, api, "/cls/depth/1110"), 0, 24, TimeUnit.HOURS);
    }

}
