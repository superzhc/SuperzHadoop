package com.github.superzhc.finance;

import com.github.superzhc.hadoop.kafka.MyAdminClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/3/30 22:40
 */
public class TopicManager {

    String brokers = "127.0.0.1:19092";

    MyAdminClient adminClient = null;

    @Before
    public void setUp() throws Exception {
        adminClient = new MyAdminClient(brokers);
    }

    @After
    public void tearDown() throws Exception {
        if (null != adminClient)
            adminClient.close();
    }

    @Test
    public void stock_zh_index_spot() {
        String topic = "finance_stock_zh_index_spot";
        if (!adminClient.exist(topic)) {
            adminClient.create(topic);
        }
    }
}
