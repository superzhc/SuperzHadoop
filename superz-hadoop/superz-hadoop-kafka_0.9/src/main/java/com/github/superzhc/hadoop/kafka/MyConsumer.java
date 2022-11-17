package com.github.superzhc.hadoop.kafka;

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

import java.util.*;

/**
 * @author superz
 * @create 2022/11/8 10:01
 **/
public class MyConsumer {
    private static final Logger log = LoggerFactory.getLogger(MyConsumer.class);

    private static final Class<StringDeserializer> DEFAULT_DESERIALIZER = StringDeserializer.class;

    private String brokers;
    /*key 反序列化*/
    private Class<? extends Deserializer<?>> keyDeserializer = null;
    /*value 反序列化*/
    private Class<? extends Deserializer<?>> valueDeserializer = null;
    /*其他配置*/
    private Map<String, String> configs = null;

    private Properties properties = new Properties();

    public MyConsumer(String brokers) {
        this.brokers = brokers;

        init();
    }

    public MyConsumer(Map<String, String> configs) {
        this.configs = configs;

        init();
    }

    public MyConsumer(String brokers, Map<String, String> configs) {
        this.brokers = brokers;
        this.configs = configs;

        init();
    }

    public MyConsumer(String brokers, Class<? extends Deserializer<?>> keyDeserializer, Class<? extends Deserializer<?>> valueDeserializer, Map<String, String> configs) {
        this.brokers = brokers;
        this.keyDeserializer = keyDeserializer;
        this.valueDeserializer = valueDeserializer;
        this.configs = configs;

        init();
    }

    private void init() {
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, brokers);

        if (null == keyDeserializer) {
            keyDeserializer = DEFAULT_DESERIALIZER;
        }
        if (null == valueDeserializer) {
            valueDeserializer = DEFAULT_DESERIALIZER;
        }
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);

        if (null != configs) {
            for (Map.Entry<String, String> config : configs.entrySet()) {
                properties.put(config.getKey(), config.getValue());
            }
        }
    }

    public <K, V> List<ConsumerRecord<K, V>> consume(String groupId, String topic, int partition, long start, long end) {
        // 设定消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        List<ConsumerRecord<K, V>> lst = new ArrayList<>();
        /*KafkaConsumer是非线程安全的*/
        try (KafkaConsumer<K, V> consumer = new KafkaConsumer<K, V>(properties)) {
            TopicPartition topicPartition = new TopicPartition(topic, partition);
            consumer.assign(Collections.singletonList(topicPartition));
            if (start > 0) {
                consumer.seek(topicPartition, start);
            }

            out:
            while (true) {
                ConsumerRecords<K, V> records = consumer.poll(100);
                for (ConsumerRecord<K, V> record : records) {
                    long offset = record.offset();
                    if (offset > end) {
                        break out;
                    }
                    lst.add(record);
                }
            }
        }
        return lst;
    }

    public <K, V> List<ConsumerRecord<K, V>> consume(String groupId, String topic, long limit) {
        // 设定消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        List<ConsumerRecord<K, V>> lst = new ArrayList<>();
        /*KafkaConsumer是非线程安全的*/
        try (KafkaConsumer<K, V> consumer = new KafkaConsumer<K, V>(properties)) {
            // 订阅主题
            consumer.subscribe(Collections.singletonList(topic));

            int i = 0;
            while (i < limit) {
                ConsumerRecords<K, V> records = consumer.poll(100);
                for (ConsumerRecord<K, V> record : records) {
                    lst.add(record);
                    i++;
                }
            }
        }
        return lst;
    }

    public <K, V> List<ConsumerRecord<K, V>> consume(String groupId, String topic, long start, long end) {
        // 设定消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        List<ConsumerRecord<K, V>> lst = new ArrayList<>();
        /*KafkaConsumer是非线程安全的*/
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

            out:
            while (true) {
                ConsumerRecords<K, V> records = consumer.poll(100);
                for (ConsumerRecord<K, V> record : records) {
                    long offset = record.offset();
                    if (offset > end) {
                        break out;
                    }
                    lst.add(record);
                }
            }
        }
        return lst;
    }

//    public <K, V> List<ConsumerRecord<K, V>> consumeByTimestamp(String groupId, String topic, long start, long end) {
//        // 设定消费者组
//        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
//
//        List<ConsumerRecord<K, V>> lst = new ArrayList<>();
//        /*KafkaConsumer是非线程安全的*/
//        try (KafkaConsumer<K, V> consumer = new KafkaConsumer<K, V>(properties)) {
//            // 订阅主题
//            consumer.subscribe(Collections.singletonList(topic));
//
//            //kafka的分区逻辑是在poll方法里执行的,所以执行seek方法之前先执行一次poll方法
//            //获取当前消费者消费分区的情况
//            Set<TopicPartition> assignment = new HashSet<>();
//            while (assignment.size() == 0) {
//                //如果没有分配到分区,就一直循环下去
//                consumer.poll(100L);
//                assignment = consumer.assignment();
//            }
//            for (TopicPartition tp : assignment) {
//                //消费第当前分区的offset为start的消息
//                consumer.seek(tp, start);
//            }
//
//            //0.9.0.1 不支持通过时间戳获取偏移量
//            Map<TopicPartition, Long> tpAndStartTimestamps = new HashMap<>();
//            for (TopicPartition tp : assignment) {
//                tpAndStartTimestamps.put(tp, start);
//            }
//            Map<TopicPartition, OffsetAndTimestamp> offsets = consumer.offsetsForTimes(map);
//
//
//        }
//        return lst;
//    }

    public <K> List<ConsumerRecord<K, String>> match(String groupId, String topic, String str, int start, int end) {
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        List<ConsumerRecord<K, String>> lst = new ArrayList<>();
        /*KafkaConsumer是非线程安全的*/
        try (KafkaConsumer<K, String> consumer = new KafkaConsumer<K, String>(properties)) {
            // 订阅主题
            consumer.subscribe(Collections.singletonList(topic));

            out:
            while (true) {
                ConsumerRecords<K, String> records = consumer.poll(100);
                for (ConsumerRecord<K, String> record : records) {
                    long offset = record.offset();
                    if (offset > end) {
                        break out;
                    }
                    String value = record.value();
                    if (value.contains(str)) {
                        lst.add(record);
                    }
                }
            }
        }
        return lst;
    }

    public static void main(String[] args) {
        String brokers = "bigdata9:6667,bigdata10:6667,bigdata11:6667";
        String topic = "im_export_Raw";

        Map<String, String> configs = new HashMap<>();
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        MyConsumer myConsumer = new MyConsumer(brokers, configs);
        List<ConsumerRecord<String, String>> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.addAll(myConsumer.<String>match(
                    String.format("superz-202211081436-%d", i)
                    , topic, "91007360819"
                    , 209615583 + (i * 10000)
                    , 209615583 + (i + 1) * 10000)
            );
        }
        for (ConsumerRecord record : data) {
            String str = String.format(
                    "主题[%s]的第[%d]分区的偏移量[%s]的信息[%s]"
                    , record.topic()
                    , record.partition()
                    , record.offset()
                    , (null == record.key() ? record.value() : String.format("%s:%s", record.key(), record.value()))
                    // , record.timestamp()
            );
            System.out.println(str);
        }
    }

}
