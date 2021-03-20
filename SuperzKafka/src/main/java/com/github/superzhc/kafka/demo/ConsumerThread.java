package com.github.superzhc.kafka.demo;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * 2020年07月23日 superz add
 */
public class ConsumerThread implements Runnable
{
    private static final String DEFAULT_TOPIC = "superzhc";
    private static final String DEFAULT_GROUP_ID = "superz";

    private KafkaConsumer<String, String> consumer;
    private String brokers;
    private String topic;
    private String groupId;

    public ConsumerThread(String brokers) {
        this(brokers, DEFAULT_GROUP_ID, DEFAULT_TOPIC);
    }

    public ConsumerThread(String brokers, String groupId) {
        this(brokers, groupId, DEFAULT_TOPIC);
    }

    public ConsumerThread(String brokers, String groupId, String topic) {
        this.brokers = brokers;
        this.groupId = groupId;
        this.topic = topic;

        init();
    }

    private void init() {
        // 创建消费者
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumer = new KafkaConsumer<String, String>(props);
    }

    @Override
    public void run() {
        // 查询指定主题的元数据信息
        // List<PartitionInfo> partitionInfos=consumer.partitionsFor(topic);

        // 订阅主题
        consumer.subscribe(Collections.singleton(topic));
        // consumer.subscribe(Pattern.compile("superz.*"));// 正则表达式订阅主题
        // consumer.assign(Collections.singleton(new TopicPartition(topic,1)));//订阅主题的特定分区

        try {
            System.out.println("消费者[" + Thread.currentThread().getName() + "]开始消费");
            while (!Thread.currentThread().isInterrupted()) {// 使用中断位来判断结束任务执行
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                System.out.println("消费者[" + Thread.currentThread().getName() + "]拉取的消息数：" + records.count());
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("消费者[" + Thread.currentThread().getName() + "]获取的消息：["+record.partition()+"]" + record.value());
                }
            }
        }
        finally {
            // Fixme:线程中断会报错
            // 解除订阅
            consumer.unsubscribe();
            consumer.close();
            System.out.printf("消费者[%s]解除订阅并关闭\n", Thread.currentThread().getName());
        }
    }
}
