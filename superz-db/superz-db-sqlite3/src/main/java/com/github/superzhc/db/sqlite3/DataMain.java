package com.github.superzhc.db.sqlite3;

import com.cronutils.model.CronType;
import com.github.superzhc.common.cron.CronScheduledThreadPoolExecutor;
import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.shopping.SMZDM;
import org.jdbi.v3.core.Jdbi;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class DataMain {
    static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        String dbPath = String.format("D:/data/smzdm_%s.db", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            dbFile.createNewFile();
        }

        String dbUrl = String.format("jdbc:sqlite:%s", dbFile.getAbsolutePath());
        final Jdbi jdbi = Jdbi.create(dbUrl);

        String tableName = "t_smzdm_faxian";

        // 创建表
        final String ddl = "CREATE TABLE IF NOT EXISTS t_smzdm_faxian(ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE VARCHAR(1024) NOT NULL,LINK VARCHAR(1024),COMMENTS VARCHAR(1024),PUB_DATE VARCHAR(50),FOCUS_PIC VARCHAR(1024),GUID VARCHAR(256) NOT NULL UNIQUE,DESCRIPTION TEXT,CONTENT_ENCODED BLOB,CREATE_TIME TIMESTAMP DEFAULT (datetime('now','localtime')),UPDATE_TIME TIMESTAMP)";
        jdbi.useHandle(handle -> handle.execute(ddl));

        CronScheduledThreadPoolExecutor cronScheduledCronThreadPoolExecutor = new CronScheduledThreadPoolExecutor(1);
        cronScheduledCronThreadPoolExecutor.scheduleWithCron(() -> {
            try {
                jdbi.useHandle(handle -> {
                    String upsert = "INSERT INTO t_smzdm_faxian(TITLE,LINK,COMMENTS,PUB_DATE,FOCUS_PIC,GUID,DESCRIPTION,CONTENT_ENCODED) " +
                            "VALUES(:title,:link,:comments,:pubDate,:focus_pic,:guid,:description,:content_encoded) " +
                            "ON CONFLICT(GUID) DO UPDATE SET UPDATE_TIME=datetime('now','localtime')";

                    List<Map<String, Object>> data = SMZDM.faxian();
                    for (Map<String, Object> record : data) {
                        handle.createUpdate(upsert).bindMap(record).execute();
                    }
                });
            } catch (Exception e) {
                latch.countDown();
            }
        }, "0/30 * * * * ? *", CronType.QUARTZ);

        latch.await();
    }
}
