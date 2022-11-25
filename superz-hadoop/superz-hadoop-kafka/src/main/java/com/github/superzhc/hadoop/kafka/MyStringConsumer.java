package com.github.superzhc.hadoop.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2022/11/25 10:22
 **/
public class MyStringConsumer extends MyConsumerNew<String, String> {
    public MyStringConsumer(String brokers) {
        this(brokers, null);
    }

    public MyStringConsumer(String brokers, String groupId) {
        this(brokers, groupId, null);
    }

    public MyStringConsumer(String brokers, String groupId, Map<String, String> properties) {
        super(brokers, groupId, checkProperties(properties));
    }

    private static Map<String, String> checkProperties(Map<String, String> properties) {
        if (null == properties) {
            properties = new HashMap<>();
        }

        // 这个是固定的
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return properties;
    }
}
