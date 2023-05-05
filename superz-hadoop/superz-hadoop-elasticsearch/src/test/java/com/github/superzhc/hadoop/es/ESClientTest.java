package com.github.superzhc.hadoop.es;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/4/12 11:07
 **/
public class ESClientTest {
    static final String HOST = "127.0.0.1";

    static final Integer PORT = 9200;

    protected ESClient client = null;

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
    public void testPing() {
        String result = client.ping();
        System.out.println(result);
    }
}
