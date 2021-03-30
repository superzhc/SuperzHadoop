package com.github.superzhc.kafka.tool;

import com.github.superzhc.kafka.MyAdminClient;
import com.github.superzhc.kafka.MyProducer;
import com.github.superzhc.kafka.util.ConsumerRecordUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * 测试 Kafka 是否可用
 */
public abstract class MyKafkaTest extends MyBasicTool {
    private static final Logger log = LoggerFactory.getLogger(MyKafkaTest.class);

    public void test() {
        try (MyAdminClient adminClient = new MyAdminClient(brokers())) {
            if (!adminClient.exist(topic())) {
                adminClient.create(topic(), 1, (short) 0, null);
            }
        }

        // 生产者产生消息
        Thread producerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (MyProducer producer = new MyProducer(brokers())) {

                    while (true) {
                        String key = key(), message = message();
                        producer.send(topic(), key, message);
                        log.info("消息：【{}】发送成功", message);

                        Thread.sleep(1000);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        // 消费者消费消息
        Thread consumerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Properties props = new Properties();
                props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers());
                props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId());
                props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
                props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
                props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
                props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

                KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
                /*订阅主题*/
                consumer.subscribe(Collections.singletonList(topic()));

                while (true) {
                    ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
                    log.info(ConsumerRecordUtils.human(consumerRecords));
                }
            }
        });

        producerThread.start();
        consumerThread.start();
    }

    protected String topic() {
        return String.format("MyKafkaTest-%s", UUID.randomUUID().toString());
    }

    protected String key() {
        return null;
    }

    protected String message() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String message = String.format("%s send the message at %s", "Producer",
                sdf.format(new Date()));
        return message;
    }

    protected String groupId() {
        return "consumer-user:superz";
    }
}
