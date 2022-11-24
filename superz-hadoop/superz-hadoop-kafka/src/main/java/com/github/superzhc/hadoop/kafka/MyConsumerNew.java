package com.github.superzhc.hadoop.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/11/22 10:39
 **/
public class MyConsumerNew<K, V> extends KafkaBrokers {
    private static final Logger log = LoggerFactory.getLogger(MyConsumerNew.class);

    private static final String DEFAULT_GROUP = "DEFAULT-SUPERZ-CONSUMER";

    private final String groupId;
    private final Map<String, String> properties;

    public MyConsumerNew(String brokers) {
        this(brokers, null);
    }

    public MyConsumerNew(String brokers, String groupId) {
        this(brokers, groupId, null);
    }

    public MyConsumerNew(String brokers, String groupId, Map<String, String> properties) {
        super(brokers);

        this.groupId = null == groupId || groupId.trim().length() == 0 ? DEFAULT_GROUP : groupId;
        this.properties = null == properties ? new HashMap<>() : properties;
    }

    private Properties properties() {
        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
//        // 设定默认值
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        if (null != properties && !properties.isEmpty()) {
            for (Map.Entry<String, String> property : properties.entrySet()) {
                props.put(property.getKey(), property.getValue());
            }
        }
        return props;
    }

    public void consumer(String topic, Function<ConsumerRecords<K, V>, Boolean> function) {
        try (Consumer<K, V> consumer = new KafkaConsumer<>(properties())) {
            // 订阅主题
            consumer.subscribe(Collections.singleton(topic));

            Boolean flag = true;
            while (flag) {
                ConsumerRecords<K, V> records = consumer.poll(Duration.ofSeconds(100));
                flag = function.apply(records);
            }
        }
    }

    public void consumer(String topic, int partition, Function<ConsumerRecords<K, V>, Boolean> function) {
        try (Consumer<K, V> consumer = new KafkaConsumer<>(properties())) {
            // 指定主题分区
            TopicPartition topicPartition = new TopicPartition(topic, partition);
            consumer.assign(Collections.singleton(topicPartition));

            Boolean flag = true;
            while (flag) {
                ConsumerRecords<K, V> records = consumer.poll(Duration.ofSeconds(100));
                flag = function.apply(records);
            }
        }
    }

    public void consumer(String topic, int partition, long start, Function<ConsumerRecords<K, V>, Boolean> function) {
        try (Consumer<K, V> consumer = new KafkaConsumer<>(properties())) {
            // 指定主题分区
            TopicPartition topicPartition = new TopicPartition(topic, partition);
            consumer.assign(Collections.singleton(topicPartition));

            // 从指定偏移量获取数据
            consumer.seek(topicPartition, start);

            Boolean flag = true;
            while (flag) {
                ConsumerRecords<K, V> records = consumer.poll(Duration.ofMillis(100));
                flag = function.apply(records);
            }
        }
    }

    public void multiConsumer(String topic, int partition, long start, long end, int workers, Function<ConsumerRecords<K, V>, Void> function) {
        if (start >= end) {
            throw new RuntimeException("主题[" + topic + "]的分区[" + partition + "]开始偏移量大于结束偏移量");
        }

        // 计算每个分片的数据量
        long diff = end - start;
        long remainder = diff % workers;
        long avgWorkerOffset = diff / workers;
        long[] workersOffset = new long[workers];
        for (int i = 0; i < workers; i++) {
            workersOffset[i] = avgWorkerOffset;
        }
        workersOffset[workers - 1] = avgWorkerOffset + remainder; // 最后一个worker消费多余的一部分偏移量

        // 创建线程池
        ExecutorService pool = new ThreadPoolExecutor(
                0
                , workers + 1
                , 0L // 任务执行完成即释放资源
                , TimeUnit.MILLISECONDS
                , new SynchronousQueue<Runnable>()
        );

        for (int j = 0; j < workers; j++) {
            Properties props = properties();
            props.put(ConsumerConfig.GROUP_ID_CONFIG, String.format("%s-%d", groupId, j));

            // 分段开始的偏移量
            final long splitStart = start + avgWorkerOffset * j;
            final long splitNums = workersOffset[j];

            Runnable task = new Runnable() {
                @Override
                public void run() {
                    try (Consumer<K, V> consumer = new KafkaConsumer<>(properties())) {
                        // 指定主题分区
                        TopicPartition topicPartition = new TopicPartition(topic, partition);
                        consumer.assign(Collections.singleton(topicPartition));

                        // 从指定偏移量获取数据
                        consumer.seek(topicPartition, splitStart);

                        int cursor = 0;
                        while (true) {
                            ConsumerRecords<K, V> records = consumer.poll(Duration.ofMillis(100));
                            function.apply(records);
                            cursor += records.count();
                            if (cursor >= splitNums) {
                                break;
                            }
                        }
                    }
                }
            };

            pool.submit(task);
        }
    }

    public static void main(String[] args) {
        String broker = "127.0.0.1:19092";
        String topic = "fund_eastmoney_real_net";
        int partition = 2;

        MyConsumerNew myConsumerNew = new MyConsumerNew(broker, "superz-202211221405");
//        myConsumerNew.consumer(topic, partition, 0, 32800, 3);
    }
}
