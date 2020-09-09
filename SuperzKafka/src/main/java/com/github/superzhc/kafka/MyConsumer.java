package com.github.superzhc.kafka;

import com.github.superzhc.kafka.util.ConsumerRecordUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

/**
 * 2020年04月26日 superz add
 */
public class MyConsumer extends KafkaBrokers implements Closeable
{
    private Consumer<String, String> consumer;
    private boolean stop = false;
    private boolean isConsume = false;

    public MyConsumer(String brokers, String groupId) {
        this(brokers, groupId, null);
    }

    public MyConsumer(String brokers, String groupId, Map<String, String> properties) {
        this(brokers, groupId, null, properties);
    }

    public MyConsumer(String brokers, String groupId, KerberosSASL kerberosSASL, Map<String, String> properties) {
        super(brokers);

        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put("group.id", groupId);

        if (null == properties) {
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        }
        else {
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, properties
                    .getOrDefault(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()));
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, properties
                    .getOrDefault(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()));
            for (Map.Entry<String, String> property : properties.entrySet()) {
                props.put(property.getKey(), property.getValue());
            }
        }

        if (null != kerberosSASL)
            kerberosSASL.config(props);

        consumer = new KafkaConsumer<String, String>(props);
    }

    public void consume(String topic, final Queue<String> container) {
        consume(topic, 1, -1, container);
    }

    /**
     * 消费消息
     * @param topic 消费的主题
     * @param period 轮询的周期
     * @param frequency 轮询的次数
     * @param container 返回消息使用的容器
     */
    public void consume(String topic, int period, int frequency, final Queue<String> container) {
        // 不让阻塞界面
        new Thread(new Runnable()
        {
            @Override
            public void run() {
                container.offer("订阅主题：" + topic);
                /* 订阅主题 */
                consumer.subscribe(Collections.singleton(topic));
                int i = frequency > 0 ? 0 : -1;// 轮询的次数，如果轮询次数<1，则认为轮询次数无效，即无限轮询
                while (!stop) {
                    /* 轮询获取数据 */
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(period));
                    if (null != records && records.count() > 0) {
                        // System.out.println("获取的消息数：" + records.count());
                        for (ConsumerRecord<String, String> record : records) {
                            container.offer(ConsumerRecordUtils.human(record));
                        }
                    }

                    if (i > -1) {
                        i++;
                    }

                    if (i > frequency)
                        break;
                }

                synchronized (consumer) {// 先释放资源再关闭consumer
                    container.offer("停止获取消息");

                    // 释放掉订阅
                    container.offer("释放掉订阅");
                    consumer.unsubscribe();

                    // 还原状态
                    stop = false;

                    // 2020年4月28日 唤醒consumer的等待，进行后续处理
                    // System.out.println("唤醒consumer的等待");
                    // try {
                    // Thread.sleep(1000*5);
                    // }
                    // catch (InterruptedException e) {
                    // e.printStackTrace();
                    // }
                    container.offer("消费者订阅释放完成");
                    consumer.notifyAll();
                }
            }
        }).start();

        isConsume = true;
    }

    @Override
    public void close() throws IOException {
        if (null != consumer) {
            if (isConsume) {
                synchronized (consumer) {
                    this.stop = true;
                    try {
                        // 2020年4月28日 消费者都关闭掉了，优先关闭掉消费者
                        consumer.wait();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            consumer.close();
        }
    }
}
