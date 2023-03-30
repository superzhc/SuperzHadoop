package com.github.superzhc.common.cron;

import com.cronutils.model.CronType;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author superz
 * @create 2023/3/30 2:03
 */
public class CronScheduledThreadPoolExecutorTest {

    CountDownLatch latch = new CountDownLatch(20);

    @Test
    public void test() throws Exception {
        CronScheduledThreadPoolExecutor cronScheduledCronThreadPoolExecutor = new CronScheduledThreadPoolExecutor(2);
        cronScheduledCronThreadPoolExecutor.scheduleWithCron(() -> {
            System.out.println("hello");
            latch.countDown();
        }, "/1 * * * * ? *", CronType.QUARTZ);
        cronScheduledCronThreadPoolExecutor.scheduleWithCron(() -> {
            System.out.println("------");
            latch.countDown();
        }, "/2 * * * * ? *", CronType.QUARTZ);

        latch.await();
    }

    public void multipieCrons() {
        // 不支持如下写法：
        //"0/30 30-59 9 ? * MON-FRI,0/30 0-29 11 ? * MON-FRI,0/30 * 10,13,14 ? * MON-FRI"
        // 将上面写法改成如下写法
        String crons = "0/30 30-59|*|0-29|* 9|10|11|13,14 ? * MON-FRI";
    }
}
