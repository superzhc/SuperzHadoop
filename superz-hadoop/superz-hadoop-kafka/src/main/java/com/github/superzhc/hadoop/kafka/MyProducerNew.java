package com.github.superzhc.hadoop.kafka;

import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author superz
 * @create 2022/11/24 17:28
 **/
public class MyProducerNew<K, V> extends KafkaBrokers implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(MyProducerNew.class);

    private volatile Producer<K, V> producer = null;
    protected Map<String, String> properties = null;

    public MyProducerNew(String brokers) {
        this(brokers, null);
    }

    public MyProducerNew(String brokers, Map<String, String> properties) {
        super(brokers);

        this.properties = properties;
    }

    /**
     * @param brokers
     * @param keySerializer   优先级高于 properties 中设定的序列化参数
     * @param valueSerializer 优先级高于 properties 中设定的序列化参数
     * @param properties
     */
    public MyProducerNew(String brokers, String keySerializer, String valueSerializer, Map<String, String> properties) {
        super(brokers);

        if (null == properties) {
            properties = new HashMap<>();
        }

        if (null != keySerializer && keySerializer.trim().length() > 0) {
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        }

        if (null != valueSerializer && valueSerializer.trim().length() > 0) {
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        }

        this.properties = properties;
    }

    public Producer<K, V> getProducer() {
        if (null == producer) {
            synchronized (MyProducerNew.class) {
                if (null == producer) {
                    Properties props = new Properties();
                    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);

                    if (null != properties && !properties.isEmpty()) {
                        for (Map.Entry<String, String> property : properties.entrySet()) {
                            props.put(property.getKey(), property.getValue());
                        }
                    }

                    producer = new KafkaProducer<K, V>(props);
                }
            }
        }
        return producer;
    }

    public Future<RecordMetadata> sendAsync(String topic, V value, Callback callback) {
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, value);
        return sendAsync(record, callback);
    }

    public Future<RecordMetadata> sendAsync(String topic, K key, V value, Callback callback) {
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, key, value);
        return sendAsync(record, callback);
    }

    public Future<RecordMetadata> sendAsync(String topic, int partition, K key, V value, Callback callback) {
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, partition, key, value);
        return sendAsync(record, callback);
    }

    public Future<RecordMetadata> sendAsync(ProducerRecord<K, V> record, Callback callback) {
        log.debug("发送消息：{}", record);
        return getProducer().send(record, callback);
    }

    public RecordMetadata send(String topic, V value) throws ExecutionException, InterruptedException {
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, value);
        return send(record);
    }

    public RecordMetadata send(String topic, K key, V value) {
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, key, value);
        return send(record);
    }

    public RecordMetadata send(String topic, int partition, K key, V value) {
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, partition, key, value);
        return send(record);
    }

    public RecordMetadata send(ProducerRecord<K, V> record) {
        try {
            log.debug("发送消息：{}", record);
            return getProducer().send(record).get();
        } catch (ExecutionException | InterruptedException e) {
            log.error("发送消息异常", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        if (null != producer) {
            producer.close();
            producer = null;
        }
    }
}
