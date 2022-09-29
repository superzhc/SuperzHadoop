package com.github.superzhc.hadoop.flink.streaming.connector.kafka;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
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
@Deprecated
public class KafkaConnectorMain {
    private static final String LOCAL_BROKERS = "localhost:19092";

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

        /**
         * Flink 消费 Kafka 的策略：
         * 1. setStartFromGroupOffsets()【默认消费策略】，默认读取上次保存的offset信息 如果是应用第一次启动，读取不到上次的offset信息，则会根据这个参数auto.offset.reset的值来进行消费数据
         * 2. setStartFromEarliest() 从最早的数据开始进行消费，忽略存储的offset信息
         * 3. setStartFromLatest() 从最新的数据进行消费，忽略存储的offset信息
         * 4. setStartFromSpecificOffsets(Map<KafkaTopicPartition, Long>) 从指定位置进行消费
         */
        consumer.setStartFromLatest();

        /**
         * 当checkpoint机制开启的时候，KafkaConsumer会定期把kafka的offset信息还有其他operator的状态信息一块保存起来。当job失败重启的时候，Flink会从最近一次的checkpoint中进行恢复数据，重新消费kafka中的数据。
         * 为了能够使用支持容错的kafka Consumer，需要开启checkpoint env.enableCheckpointing(5000); // 每5s checkpoint一次
         */

        return consumer;
    }

    public static FlinkKafkaProducer<String> sink(String topic) {
        return sink(topic, new SimpleStringSchema());
    }

    /**
     * 向 Kafka 中写入数据
     * @param topic
     * @param serializationSchema
     * @param <T>
     * @return
     */
    public static <T> FlinkKafkaProducer<T> sink(String topic, SerializationSchema<T> serializationSchema) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, LOCAL_BROKERS);
        FlinkKafkaProducer<T> producer = new FlinkKafkaProducer<T>(topic, serializationSchema, props);
        // event time 时间戳
        producer.setWriteTimestampToKafka(true);
        return producer;
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
