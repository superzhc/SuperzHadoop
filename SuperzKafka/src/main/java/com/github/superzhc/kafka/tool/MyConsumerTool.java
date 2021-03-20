package com.github.superzhc.kafka.tool;

import com.github.superzhc.kafka.MyProducer;
import com.github.superzhc.kafka.util.ConsumerRecordUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Pattern;

public class MyConsumerTool {
    private static final Logger log = LoggerFactory.getLogger(MyConsumerTool.class);

    public static void main(String[] args) {
//        Properties props = new Properties();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, jsdz_brokers);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "cloud4control-user:superz1");
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//
//        /*创建kafka消费者对象*/
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
//        /*订阅主题*/
////        String topic = "^(V2_Up_)([A-Za-z0-9]+)(_Event_ObjectPTC)$";
////        consumer.subscribe(Pattern.compile(topic));
//
//        consumer.subscribe(Collections.singletonList("V2_Up_Camera_Event_ObjectPTC"));
//
//        /*每隔一段时间到服务器拉取数据*/
//        ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
//        log.info(ConsumerRecordUtils.human(consumerRecords));
        MyKafkaTest kafkaTest=new MyKafkaTest(MyKafkaConfigs.JSDZ_BROKER,"superz-test");
        kafkaTest.test();
    }
}
