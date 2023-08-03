package com.github.superzhc.db.sqlite3;

import com.cronutils.model.CronType;
import com.github.superzhc.common.cron.CronScheduledThreadPoolExecutor;
import com.github.superzhc.data.news.AnyKnew;
import com.github.superzhc.data.shopping.SMZDM;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class AnyKnewDataMain {

    final static Integer NUMBER=5;
    static CountDownLatch latch = new CountDownLatch(NUMBER);

    public static void main(String[] args) throws Exception {
        String path="D:/notebook/notebook.db";

        String dbUrl = String.format("jdbc:sqlite:%s", path);
        final Jdbi jdbi = Jdbi.create(dbUrl);

        final String upsert = "INSERT OR IGNORE INTO t_news_anyknew(id,title,add_date,platform,platform_cn) VALUES(:iid,:title,:add_date,:platform,:platformCN)";

        CronScheduledThreadPoolExecutor cronScheduledCronThreadPoolExecutor = new CronScheduledThreadPoolExecutor(NUMBER);
        cronScheduledCronThreadPoolExecutor.scheduleWithCron(() -> {
            try {
                jdbi.useHandle(handle -> {
                    List<Map<String, String>> data = AnyKnew.weibo();
                    for (Map<String, String> record : data) {
                        handle.createUpdate(upsert).bindMap(record).execute();
                    }
                });
            } catch (Exception e) {
                latch.countDown();
            }
        }, "0 1/5 * * * ? *", CronType.QUARTZ);

        cronScheduledCronThreadPoolExecutor.scheduleWithCron(() -> {
            try {
                jdbi.useHandle(handle -> {
                    List<Map<String, String>> data = AnyKnew.zhihu();
                    for (Map<String, String> record : data) {
                        handle.createUpdate(upsert).bindMap(record).execute();
                    }
                });
            } catch (Exception e) {
                latch.countDown();
            }
        }, "0 5 0/1 * * ? *", CronType.QUARTZ);

        cronScheduledCronThreadPoolExecutor.scheduleWithCron(() -> {
            try {
                jdbi.useHandle(handle -> {
                    List<Map<String, String>> data = AnyKnew.xueqiu();
                    for (Map<String, String> record : data) {
                        handle.createUpdate(upsert).bindMap(record).execute();
                    }
                });
            } catch (Exception e) {
                latch.countDown();
            }
        }, "0 10 0/1 * * ? *", CronType.QUARTZ);

        cronScheduledCronThreadPoolExecutor.scheduleWithCron(() -> {
            try {
                jdbi.useHandle(handle -> {
                    List<Map<String, String>> data = AnyKnew.investing();
                    for (Map<String, String> record : data) {
                        handle.createUpdate(upsert).bindMap(record).execute();
                    }
                });
            } catch (Exception e) {
                latch.countDown();
            }
        }, "0 15 0/1 * * ? *", CronType.QUARTZ);

        cronScheduledCronThreadPoolExecutor.scheduleWithCron(() -> {
            try {
                jdbi.useHandle(handle -> {
                    List<Map<String, String>> data = AnyKnew.wallstreetcn();
                    for (Map<String, String> record : data) {
                        handle.createUpdate(upsert).bindMap(record).execute();
                    }
                });
            } catch (Exception e) {
                latch.countDown();
            }
        }, "0 20 0/1 * * ? *", CronType.QUARTZ);

        latch.await();
    }
}
