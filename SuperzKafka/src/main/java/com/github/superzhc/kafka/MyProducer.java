package com.github.superzhc.kafka;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * 2020年04月26日 superz add
 */
public class MyProducer extends KafkaBrokers implements Closeable
{
    private Producer<String, String> producer;

    public MyProducer(String brokers) {
        this(brokers, null);
    }

    public MyProducer(String brokers, Map<String, String> properties) {
        super(brokers);

        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, brokers);

        if (null == properties) {
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        }
        else {
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, properties
                    .getOrDefault(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()));
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, properties
                    .getOrDefault(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()));

            for (Map.Entry<String, String> property : properties.entrySet()) {
                props.put(property.getKey(), property.getValue());
            }
        }

        producer = new KafkaProducer<String, String>(props);
    }

    // public Future<RecordMetadata> sendAsync(String topic, String value, Callback
    // callback) {
    // ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
    // return producer.send(record, callback);
    // }

    public Future<RecordMetadata> sendAsync(String topic, String key, String value, Callback callback) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        return producer.send(record, callback);
    }

    // public RecordMetadata send(String topic, String value) throws
    // ExecutionException, InterruptedException {
    // ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
    // return producer.send(record).get();
    // }

    public RecordMetadata send(String topic, String key, String value) throws ExecutionException, InterruptedException {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        return producer.send(record).get();
    }

    @Override
    public void close() throws IOException {
        if (null != producer)
            producer.close();
    }
}
