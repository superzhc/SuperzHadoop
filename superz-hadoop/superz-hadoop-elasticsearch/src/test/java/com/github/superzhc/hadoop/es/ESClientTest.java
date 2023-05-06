package com.github.superzhc.hadoop.es;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/4/12 11:07
 **/
public class ESClientTest {
    static final String HOST1 = "10.90.9.38";
    static final String HOST2 = "10.90.9.39";
    static final String HOST3 = "10.90.9.40";
    static final String HOST = HOST3/*"127.0.0.1"*/;


    static final Integer PORT = 9200;

    static final String USERNAME = "elastic";

    static final String PASSWORD = "xgxx@elastic";

    protected ESClient client = null;

    @Before
    public void setUp() throws Exception {
        client = ESClient.create(HOST, PORT, USERNAME, PASSWORD);
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
