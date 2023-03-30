package com.github.superzhc.finance;

import com.github.superzhc.common.cron.CronScheduledThreadPoolExecutor;
import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.other.AKTools;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author superz
 * @create 2023/3/30 9:46
 **/
public class AKToolsTask {
    final static CronScheduledThreadPoolExecutor executor = new CronScheduledThreadPoolExecutor(1);

    final static CountDownLatch latch = new CountDownLatch(1);

    AKTools api;

    @Before
    public void setUp() {
        api = new AKTools("127.0.0.1", 8080);
    }

    @After
    public void tearDown() {
        executor.shutdown();
    }

    @Test
    public void demo() throws Exception {
        executor.scheduleWithCron(
                () -> {
                    try {
                        MapUtils.show(api.get("stock_zh_index_spot"), 20);
                    } catch (Exception e) {
                        latch.countDown();
                    }
                },
                "0/30 30-59|*|0-29|* 9|10|11|13,14 ? * MON-FRI"
        );

        latch.await();
    }
}
