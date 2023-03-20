package com.github.superzhc.hadoop.kafka.task;

import com.github.superzhc.data.news.Rsshub;
import com.github.superzhc.hadoop.kafka.MyAdminClient;
import com.github.superzhc.hadoop.kafka.MyStringProducer;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author superz
 * @create 2023/3/20 23:59
 */
public class RsshubShoppingTask {
    public static void main(String[] args) {
        final Rsshub api = new Rsshub("http://127.0.0.1:1200", "127.0.0.1", 10809);

        String brokers = "127.0.0.1:19092";
        String topic = "rsshub_shopping";
        try (MyAdminClient admin = new MyAdminClient(brokers)) {
            if (!admin.exist(topic)) {
                admin.create(topic, 10, (short) 1, null);
            }
        }


        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        final MyStringProducer producer = new MyStringProducer(brokers);

        // 0818团
        executor.scheduleWithFixedDelay(new RsshubTask.RsshubThread(producer, topic, api, "/0818tuan/1"), 0, 10, TimeUnit.MINUTES);
        // 逛丢
        executor.scheduleWithFixedDelay(new RsshubTask.RsshubThread(producer, topic, api, "/guangdiu/rank"), 0, 30, TimeUnit.MINUTES);
    }
}
