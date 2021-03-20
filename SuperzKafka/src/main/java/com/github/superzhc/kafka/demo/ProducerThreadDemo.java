package com.github.superzhc.kafka.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * 2020年07月23日 superz add
 */
public class ProducerThreadDemo implements Runnable
{
    private static final String DEFAULT_TOPIC = "superzhc";

    /**
     * KafkaProducer是线程安全的，可以在多个线程中共享单个KafkaProducer实例。
     */
    private KafkaProducer<String, String> producer;
    private String topic;

    public ProducerThreadDemo(KafkaProducer<String, String> producer) {
        this(producer, DEFAULT_TOPIC);
    }

    public ProducerThreadDemo(KafkaProducer<String, String> producer, String topic) {
        this.producer = producer;
        this.topic = topic;
    }

    public static KafkaProducer<String, String> create(String brokers) {
        /**
         * 配置生产者客户端参数及创建相应的生产者实例
         */
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        // 序列化参数无默认值，创建生产者必须填写序列化器的名称
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 配置拦截器
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, ProducerInterceptorPrefix.class.getName());
        return new KafkaProducer<String, String>(props);
    }

    public static void close(KafkaProducer<String, String> producer) {
        System.out.println("生产者关闭开始。。。");
        // 关闭生产者实例
        producer.close();
        System.out.println("生产者关闭完成！！！");
    }

    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // 构建所需要发送的消息
                String message = String.format("%s send the message at %s", Thread.currentThread().getName(),
                        sdf.format(new Date()));
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
                // 发送消息
                producer.send(record, new Callback()
                {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (null != exception) {
                            exception.printStackTrace();
                        }
                    }
                });
                Thread.sleep(1000);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
