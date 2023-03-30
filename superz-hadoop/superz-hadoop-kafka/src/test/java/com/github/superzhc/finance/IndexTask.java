package com.github.superzhc.finance;

import com.github.superzhc.common.cron.CronScheduledThreadPoolExecutor;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.data.other.AKTools;
import com.github.superzhc.hadoop.kafka.MyStringProducer;
import org.junit.After;
import org.junit.Before;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author superz
 * @create 2023/3/30 22:20
 */
public class IndexTask {
    static CronScheduledThreadPoolExecutor executor = new CronScheduledThreadPoolExecutor(1);

    static CountDownLatch latch = new CountDownLatch(1);

    AKTools akTools;

    String brokers = "127.0.0.1:19092";
    MyStringProducer producer = null;

    @Before
    public void setUp() throws Exception {
        akTools = new AKTools("127.0.0.1", 8080);

        producer = new MyStringProducer(brokers);
    }

    @After
    public void tearDown() throws Exception {
        if (null != producer) {
            producer.close();
        }
    }

    public void stock_zh_index_spot() throws Exception {
        final String topic = "finance_stock_zh_index_spot";
        executor.scheduleWithCron(
                () -> {
                    List<Map<String, Object>> data = akTools.get("stock_zh_index_spot");
                    try {
                        for (Map<String, Object> item : data) {
                            producer.send(topic, JsonUtils.asString(item));
                        }
                    } catch (Exception e) {
                        latch.countDown();
                    }
                },
                "0/5 30-59|*|0-29|* 9|10|11|13,14 ? * MON-FRI"
        );

        latch.await();
    }
}
