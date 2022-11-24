package com.github.superzhc.hadoop.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2022/11/24 17:38
 **/
public class MyStringProducer extends MyProducerNew<String, String> {
    public MyStringProducer(String brokers) {
        this(brokers, null);
    }

    public MyStringProducer(String brokers, Map<String, String> properties) {
        super(brokers, StringSerializer.class.getName(), StringSerializer.class.getName(), properties);
    }
}
