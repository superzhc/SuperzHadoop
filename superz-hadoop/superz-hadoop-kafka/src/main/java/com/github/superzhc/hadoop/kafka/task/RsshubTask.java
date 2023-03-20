package com.github.superzhc.hadoop.kafka.task;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.news.Rsshub;
import com.github.superzhc.hadoop.kafka.MyAdminClient;
import com.github.superzhc.hadoop.kafka.MyStringProducer;

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

    public static void main(String[] args) {
        final Rsshub api = new Rsshub("http://127.0.0.1:1200", "127.0.0.1", 10809);
//        List<Map<String, Object>> data = api.get("/caijing/roll");
//        System.out.println(MapUtils.print(data));

        String brokers = "127.0.0.1:19092";
        String topic = "rsshub";
        try (MyAdminClient admin = new MyAdminClient(brokers)) {
            admin.create(topic);
        }


        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        final MyStringProducer producer = new MyStringProducer(brokers);

        executor.scheduleWithFixedDelay(new Runnable() {

            @Override
            public void run() {
                List<Map<String, Object>> data = api.get("/caijing/roll");
                for (Map<String, Object> item : data) {
                    producer.send(topic, "caijing_roll", JsonUtils.asString(item));
                }
            }
        }, 0, 5, TimeUnit.MINUTES);
    }

}
