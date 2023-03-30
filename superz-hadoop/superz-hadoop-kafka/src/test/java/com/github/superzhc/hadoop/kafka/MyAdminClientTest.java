package com.github.superzhc.hadoop.kafka;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

/**
 * @author superz
 * @create 2023/3/30 11:51
 **/
public class MyAdminClientTest {
    MyAdminClient adminClient = null;

    @Before
    public void setUp() {
        adminClient = new MyAdminClient("127.0.0.1:19092");
    }

    @After
    public void tearDown() {
        adminClient.close();
    }

    public void createTopic() {
        String topic = "";
        adminClient.create(topic);
    }

    public void createTopic2() {
        adminClient.create("", 3, (short) 1, null);
    }

    @Test
    public void topics() {
        Set<String> topics = adminClient.list();
        System.out.println(topics);
    }
}
