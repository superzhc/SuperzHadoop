package com.github.superzhc.e_commerce;

import com.github.superzhc.common.cron.CronScheduledThreadPoolExecutor;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.data.shopping.GuangDiu;
import com.github.superzhc.hadoop.es.ESClient;
import com.github.superzhc.hadoop.es.document.ESDocument;
import com.github.superzhc.hadoop.es.index.ESIndex;
import com.github.superzhc.hadoop.es.search.ESNewSearch;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author superz
 * @create 2023/4/12 14:04
 **/
public class GuangDiuTest {
    static final String HOST = "1.117.173.217";

    static final Integer PORT = 19200;

    static final String NAME = "platform_guangdiu";

    ESClient client = null;

    @Before
    public void setUp() throws Exception {
        client = ESClient.create(HOST, PORT);
    }

    @After
    public void tearDown() throws Exception {
        if (null != client) {
            client.close();
        }
    }

    @Test
    public void createIndex() {
        ESIndex indexClient = new ESIndex(client);
        if (!indexClient.exist(NAME)) {
            indexClient.create(NAME);
        }
    }

    static CronScheduledThreadPoolExecutor executor = new CronScheduledThreadPoolExecutor(1);
    static CountDownLatch latch = new CountDownLatch(1);

    @Test
    public void upsertData() throws Exception {
        final ESDocument documentClient = new ESDocument(client);

        executor.scheduleWithCron(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<Map<String, Object>> data = GuangDiu.all();
                            for (Map<String, Object> item : data) {
                                String id = String.valueOf(item.get("id"));
                                documentClient.upsert(NAME, id, JsonUtils.asString(item));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            latch.countDown();
                        }
                    }
                }, "0/30 * * * * ? *"
        );

        latch.await();
    }

    @Test
    public void count() {
        ESDocument documentClient = new ESDocument(client);
        String result = documentClient.count(NAME);
        System.out.println(result);
    }

    @Test
    public void queryAll() {
        ESNewSearch searchClient = new ESNewSearch(client);
        String result = searchClient.queryAll(NAME);
        System.out.println(result);
    }

    @Test
    public void query() {
        ESNewSearch searchClient = new ESNewSearch(client);
        String dsl = new SearchSourceBuilder().query(QueryBuilders.matchQuery("title", "酒")).toString();
        System.out.println(dsl);
        String result = searchClient.queryDSL(dsl, NAME);
        System.out.println(result);
    }
}
