package com.github.superzhc.hadoop.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/30 14:06
 **/
public class MyProducerNewTest {
    MyProducerNew<String, String> producer = null;

    @Before
    public void setUp() {
        Map<String, String> properties = new HashMap<>();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new MyProducerNew<>("127.0.0.1:9092", properties);
    }

    @After
    public void tearDown() throws Exception {
        producer.close();
    }

    @Test
    public void send() throws Exception{
        producer.send("test_20230525","{}");
    }
}
