package com.github.superzhc.data.shopping;

import com.cronutils.model.CronType;
import com.github.superzhc.common.cron.CronScheduledThreadPoolExecutor;
import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.common.utils.MapUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author superz
 * @create 2023/5/12 0:22
 */
public class SMZDMTest {
    private static final Logger LOG = LoggerFactory.getLogger(SMZDMTest.class);

    private static final String DATE_FORMATTER = "E,dd MMM yyyy HH:mm:ss";

    private String url = "jdbc:mysql://localhost:3306/superz-ruoyi?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8";
    private String username = "root";
    private String password = "123456";

    JdbcHelper jdbc;

    static CountDownLatch latch = new CountDownLatch(1);

    @Before
    public void setUp() throws Exception {
        jdbc = new JdbcHelper(url, username, password);
    }

    @After
    public void tearDown() throws Exception {
        jdbc.close();
    }

    @Test
    public void testSmzdm() {
        List<Map<String, Object>> data = SMZDM.smzdm();
        MapUtils.show(data);
    }

    @Test
    public void testFaxian() throws Exception {
        CronScheduledThreadPoolExecutor cronScheduledCronThreadPoolExecutor = new CronScheduledThreadPoolExecutor(1);
        cronScheduledCronThreadPoolExecutor.scheduleWithCron(() -> {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMATTER, Locale.US);

            String sql = "insert into t_smzdm_faxian (guid, title, link, publish_date, focus_pic, note, content) values (?,?,?,?,?,?,?)";
            List<List<Object>> sqlData = new ArrayList<>();
            try {
                List<Map<String, Object>> data = SMZDM.faxian();
                for (Map<String, Object> item : data) {
                    long exist = jdbc.aggregate("select count(*) from t_smzdm_faxian where guid=?", item.get("guid"));
                    if (exist < 1) {
                        List<Object> sqlItem = new ArrayList<>();
                        sqlItem.add(item.get("guid"));
                        sqlItem.add(item.get("title"));
                        sqlItem.add(item.get("link"));
                        sqlItem.add(format.parse(item.get("pubDate").toString()));
                        sqlItem.add(item.get("focus_pic"));
                        sqlItem.add(item.get("description"));
                        sqlItem.add(item.get("content_encoded"));

                        sqlData.add(sqlItem);
                    }
                }

                jdbc.batchUpdate(sql, sqlData);
            } catch (Exception e) {
                LOG.error("异常", e);
                latch.countDown();
            }
        }, "0 0/1 * * * ? *", CronType.QUARTZ);

        latch.await();
    }

    @Test
    public void testPost() throws Exception {
        List<Map<String, Object>> data = SMZDM.post();
        MapUtils.show(data);
    }

    @Test
    public void testNews() throws Exception {
        List<Map<String, Object>> data = SMZDM.news();
        MapUtils.show(data);
    }
}
