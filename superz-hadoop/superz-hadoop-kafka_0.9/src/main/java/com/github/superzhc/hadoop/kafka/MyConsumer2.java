package com.github.superzhc.hadoop.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.LocalDateTimeUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author superz
 * @create 2022/11/9 10:52
 **/
public class MyConsumer2 {
    private static final Logger log = LoggerFactory.getLogger(MyConsumer2.class);

    public static class MyConsumerBuilder {
        private Properties properties;

        public MyConsumerBuilder() {
            this(null);
        }

        public MyConsumerBuilder(Properties properties) {
            if (null == properties) {
                this.properties = new Properties();
            } else {
                this.properties = properties;
            }
        }

        public MyConsumerBuilder brokers(String brokers) {
            properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, brokers);
            return this;
        }

        public MyConsumerBuilder groupId(String groupId) {
            properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            return this;
        }

        public MyConsumerBuilder keyDeserializer(Class<? extends Deserializer<?>> keyDeserializer) {
            properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
            return this;
        }

        public MyConsumerBuilder valueDeserializer(Class<? extends Deserializer<?>> valueDeserializer) {
            properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
            return this;
        }

        public MyConsumerBuilder autoOffsetRest(String autoOffsetRest) {
            properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, /*"earliest"*/autoOffsetRest);
            return this;
        }

        public MyConsumerBuilder enableAutoCommit(boolean enableAutoCommit) {
            properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(enableAutoCommit));
            return this;
        }

        public MyConsumer2 build() {
            return new MyConsumer2(properties);
        }
    }

    private Properties properties;

    public MyConsumer2(Properties properties) {
        this.properties = properties;
    }

    public <K, V> void consume(String topic, long start, Consumer<ConsumerRecords<K, V>> customConsumer) {
        try (KafkaConsumer<K, V> consumer = new KafkaConsumer<K, V>(properties)) {
            // 订阅主题
            consumer.subscribe(Collections.singletonList(topic));

            //kafka的分区逻辑是在poll方法里执行的,所以执行seek方法之前先执行一次poll方法
            //获取当前消费者消费分区的情况
            Set<TopicPartition> assignment = new HashSet<>();
            while (assignment.size() == 0) {
                //如果没有分配到分区,就一直循环下去
                consumer.poll(100L);
                assignment = consumer.assignment();
            }
            for (TopicPartition tp : assignment) {
                //消费第当前分区的offset为start的消息
                consumer.seek(tp, start);
            }

            while (true) {
                ConsumerRecords<K, V> records = consumer.poll(1000);
                log.info("获取消息条数：{}", records.count());
                if (!records.isEmpty()) {
                    customConsumer.accept(records);
                }
            }
        }
    }

    public <K, V> void consume(String topic, Consumer<ConsumerRecords<K, V>> customConsumer) {
        try (KafkaConsumer<K, V> consumer = new KafkaConsumer<K, V>(properties)) {
            consumer.subscribe(Collections.singletonList(topic));

            while (true) {
                ConsumerRecords<K, V> records = consumer.poll(1000);
                log.info("获取消息条数：{}", records.count());
                if (!records.isEmpty()) {
                    customConsumer.accept(records);
                }
            }
        }
    }

    public static void main(String[] args) {
        MyConsumerBuilder builder = new MyConsumerBuilder();
        MyConsumer2 consumer = builder.brokers("bigdata9:6667,bigdata10:6667,bigdata11:6667")
                .groupId("superz-2022111111435")
                .keyDeserializer(StringDeserializer.class)
                .valueDeserializer(StringDeserializer.class)
                .autoOffsetRest("earliest")
//                .autoOffsetRest("latest")
                .build();

        String topic = "im_export_Raw";
        topic = "im_export_tech";
        long end = new MyAdminClient("10.90.9.4:2181").earliestOffset(topic, 0);
        consumer.consume(topic, end, new Consumer<ConsumerRecords<String, String>>() {
            @Override
            public void accept(ConsumerRecords<String, String> consumerRecords) {
                // 91007360819 有数据
                // 91007360808 有数据
                List<String> filters = Arrays.asList("91007360813", "91007360808", "91007360819", "91007360818", "91007360811", "91007360814", "91007360812", "91007360820", "91007360817", "91007360815", "91007360810", "91007360806", "91007360809", "91007360816", "91007360807");
                for (ConsumerRecord<String, String> record : consumerRecords) {
                    String value = record.value();
                    for (String filter : filters) {
                        if (value.contains(filter)) {
                            JsonNode json = JsonUtils.json(value);
                            long cmdReciveTime = JsonUtils.aLong(json, "cmdReciveTime");
                            long timestamp = record.timestamp();

                            System.out.printf("主题【%s:%d】的偏移量【%d】的数据上报时间：【%s，Kafka's Timestamp：%s，时延：%s】，原始数据：%s\n",
                                    record.topic()
                                    , record.partition()
                                    , record.offset()
                                    , LocalDateTimeUtils.format(LocalDateTimeUtils.convert4timestamp(cmdReciveTime))
                                    , LocalDateTimeUtils.format(LocalDateTimeUtils.convert4timestamp(timestamp))
                                    , (timestamp - cmdReciveTime) / (1000 * 60.0)
                                    , record.value());
                        }
                    }
                }
            }
        });
    }
}
