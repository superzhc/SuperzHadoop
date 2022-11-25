package com.github.superzhc.hadoop.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.collection.FixedQueue;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.LocalDateTimeUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/11/22 10:39
 **/
public class MyConsumerNew<K, V> {
    private static final Logger log = LoggerFactory.getLogger(MyConsumerNew.class);

    public final Function<ConsumerRecords<K, V>, Boolean> DEFAULT_CONSUMER_PRINT = (ConsumerRecords<K, V> records) -> {
        for (ConsumerRecord<K, V> record : records) {
            System.out.println(record);
        }
        return true;
    };

//    @Deprecated
//    public static class ConsumerLastN<K, V> implements Function<ConsumerRecords<K, V>, Boolean> {
//
//        private final int nums;
//        private final List<ConsumerRecord<K, V>> container;
//
//        public ConsumerLastN(int nums) {
//            this(nums, new ArrayList<>());
//        }
//
//        public ConsumerLastN(int nums, List<ConsumerRecord<K, V>> container) {
//            this.nums = nums;
//            this.container = container;
//        }
//
//        public List<ConsumerRecord<K, V>> get() {
//            return container;
//        }
//
//        @Override
//        public Boolean apply(ConsumerRecords<K, V> records) {
//            for (ConsumerRecord<K, V> record : records) {
//                container.add(record);
//
//                if (container.size() > nums) {
//                    return false;
//                }
//            }
//            return true;
//        }
//    }
//
//    @Deprecated
//    public final Function<ConsumerRecords<K, V>, Boolean> DEFAULT_CONSUMER_Last1000 = new ConsumerLastN<>(1000);

    private static final String DEFAULT_GROUP = "DEFAULT-SUPERZ-CONSUMER";

    private final String brokers;
    private final String groupId;
    private final Map<String, String> properties;

    public MyConsumerNew(String brokers) {
        this(brokers, null);
    }

    public MyConsumerNew(String brokers, String groupId) {
        this(brokers, groupId, null);
    }

    public MyConsumerNew(String brokers, String groupId, Map<String, String> properties) {
        this.brokers = brokers;
        this.groupId = null == groupId || groupId.trim().length() == 0 ? DEFAULT_GROUP : groupId;
        this.properties = null == properties ? new HashMap<>() : properties;
    }

    private Properties properties() {
        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

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
                ConsumerRecords<K, V> records = consumer.poll(100);
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
                ConsumerRecords<K, V> records = consumer.poll(100);
                flag = function.apply(records);
            }
        }
    }

    public void consumer4Beginning(String topic, int partition, Function<ConsumerRecords<K, V>, Boolean> function) {
        try (Consumer<K, V> consumer = new KafkaConsumer<>(properties())) {
            // 指定主题分区
            TopicPartition topicPartition = new TopicPartition(topic, partition);
            consumer.assign(Collections.singleton(topicPartition));

            // 从指定偏移量获取数据
            consumer.seekToBeginning(Collections.singletonList(topicPartition));

            Boolean flag = true;
            while (flag) {
                ConsumerRecords<K, V> records = consumer.poll(1000);
                flag = function.apply(records);
            }
        }
    }

    public void consumer4End(String topic, int partition, Function<ConsumerRecords<K, V>, Boolean> function) {
        try (Consumer<K, V> consumer = new KafkaConsumer<>(properties())) {
            // 指定主题分区
            TopicPartition topicPartition = new TopicPartition(topic, partition);
            consumer.assign(Collections.singleton(topicPartition));

            // 从指定偏移量获取数据
            consumer.seekToEnd(Collections.singletonList(topicPartition));

            Boolean flag = true;
            while (flag) {
                ConsumerRecords<K, V> records = consumer.poll(100);
                flag = function.apply(records);
            }
        }
    }

    public void consumer(String topic, int partition, long start, final Function<ConsumerRecords<K, V>, Boolean> function) {
        try (Consumer<K, V> consumer = new KafkaConsumer<>(properties())) {
            // 指定主题分区
            TopicPartition topicPartition = new TopicPartition(topic, partition);
            consumer.assign(Collections.singleton(topicPartition));

            // 从指定偏移量获取数据
            consumer.seek(topicPartition, start);

            Boolean flag = true;
            while (flag) {
                ConsumerRecords<K, V> records = consumer.poll(100);
                flag = function.apply(records);
            }
        }
    }

    public void multiConsumer(String topic, int partition, long start, long end, int workers, final java.util.function.Consumer<ConsumerRecords<K, V>> function) {
        if (start >= end) {
            throw new RuntimeException("主题[" + topic + "]的分区[" + partition + "]开始偏移量大于结束偏移量");
        }

        // 计算每个分片的数据量
        long diff = end - start;
        long remainder = diff % workers;
        long avgWorkerOffset = diff / workers;
        long[] workersOffset = new long[workers];
        for (int i = 0; i < workers - 1; i++) {
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
                            ConsumerRecords<K, V> records = consumer.poll(1000);
                            function.accept(records);
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
        String broker = "bigdata9:6667,bigdata10:6667,bigdata11:6667";
        String topic = "im_export_Work"/*"im_export_Raw"*/;
        int partition = 0;
        long start = 135885935;
        final long end = 139246922;

        //ConcurrentHashMap<Integer, AtomicLong> cmdTypes = new ConcurrentHashMap<>();
        Map<String, String> properties = new HashMap<>();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        final FixedQueue<ConsumerRecord<String, String>> queue = new FixedQueue<>(1000);

        MyConsumerNew<String, String> myConsumerNew = new MyConsumerNew<>(broker, String.format("superz-%s", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))), properties);
        myConsumerNew.multiConsumer(topic, partition, start, end, 8, new java.util.function.Consumer<ConsumerRecords<String, String>>() {
            @Override
            public void accept(ConsumerRecords<String, String> records) {
                if (null == records || records.isEmpty()) {
                    return;
                }

                int counter = 0;
                int total = records.count();
                long minOffset = Long.MAX_VALUE;
                long maxOffset = -1;
                for (ConsumerRecord<String, String> record : records) {
                    minOffset = Math.min(record.offset(), minOffset);
                    maxOffset = Math.max(record.offset(), maxOffset);
                    String value = record.value();
                    JsonNode json = JsonUtils.json(value);
                    String devSn = JsonUtils.string(json, "productID");
                    if ("A04FA021800403".equals(devSn)) {
                        counter++;
                        queue.offer(record);
                    }
                }
                log.debug("获取数据条数：{}[{} ~ {}]，匹配条数：{}", total, minOffset, maxOffset, counter);
            }
        });

        for (ConsumerRecord<String, String> record : queue) {
            System.out.println(record);
        }
    }
}
