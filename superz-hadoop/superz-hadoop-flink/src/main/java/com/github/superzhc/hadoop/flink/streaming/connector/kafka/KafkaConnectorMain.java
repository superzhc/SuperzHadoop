package com.github.superzhc.hadoop.flink.streaming.connector.kafka;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;

/**
 * Kafka 连接器
 *
 * @author superz
 * @create 2021/10/12 10:48
 */
public class KafkaConnectorMain {
    private static final String LOCAL_BROKERS = "localhost:9092";

    public static FlinkKafkaConsumer<ObjectNode> sourceWithJson(String groupId, String topic) {
        return source(groupId, topic, new JSONKeyValueDeserializationSchema(true));
    }

    public static FlinkKafkaConsumer<String> source(String groupId, String topic) {
        return source(groupId, topic, new SimpleStringSchema());
    }

    public static <T> FlinkKafkaConsumer<T> source(String groupId, String topic, KafkaDeserializationSchema<T> kafkaDeserializationSchema) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, LOCAL_BROKERS);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 5000);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId/*"consumer-flink-user:superz"*/);
        FlinkKafkaConsumer<T> consumer = new FlinkKafkaConsumer<T>(
                topic
                , kafkaDeserializationSchema
                , props);
        consumer.setStartFromLatest();
        return consumer;
    }

    public static <T> FlinkKafkaConsumer<T> source(String groupId, String topic, DeserializationSchema<T> valueDeserializer) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, LOCAL_BROKERS);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 5000);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId/*"consumer-flink-user:superz"*/);
        FlinkKafkaConsumer<T> consumer = new FlinkKafkaConsumer<T>(
                topic
                , valueDeserializer
                , props);
        consumer.setStartFromLatest();
        return consumer;
    }

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // DataStream<String> ds = env.addSource(source("superz-" + KafkaConnectorMain.class.getSimpleName(), "flink-test2"));
        DataStream<ObjectNode> ds = env.addSource(sourceWithJson("superz-" + KafkaConnectorMain.class.getSimpleName(), "flink-test2"));
        ds.print();
        env.execute(KafkaConnectorMain.class.getName());
    }
}
