package com.github.superzhc.common.cron;

import com.cronutils.model.CronType;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author superz
 * @create 2023/3/30 2:03
 */
public class CronScheduledThreadPoolExecutorTest {

    CountDownLatch latch=new CountDownLatch(100);
    @Test
    public void test1() throws Exception {
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
}
