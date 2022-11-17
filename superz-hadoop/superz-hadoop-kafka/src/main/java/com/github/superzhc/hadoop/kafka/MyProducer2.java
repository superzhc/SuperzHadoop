package com.github.superzhc.hadoop.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author superz
 * @create 2022/11/17 11:44
 **/
public class MyProducer2 {
    private Producer<String, String> producer;

    public MyProducer2(Producer<String,String> producer){
        this.producer=producer;
    }

    public Producer<String, String> getProducer() {
        return producer;
    }

    public Future<RecordMetadata> sendAsync(String topic, String value, Callback
            callback) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
        return producer.send(record, callback);
    }

    public Future<RecordMetadata> sendAsync(String topic, String key, String value, Callback callback) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        return producer.send(record, callback);
    }

    public RecordMetadata send(String topic, String value) throws
            ExecutionException, InterruptedException {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
        return producer.send(record).get();
    }

    public RecordMetadata send(String topic, String key, String value) throws ExecutionException, InterruptedException {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        return producer.send(record).get();
    }
}
