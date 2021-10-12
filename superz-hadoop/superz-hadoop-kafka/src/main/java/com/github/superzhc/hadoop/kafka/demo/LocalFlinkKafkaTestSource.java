package com.github.superzhc.hadoop.kafka.demo;

import com.github.superzhc.hadoop.kafka.tool.MyProducerTool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author superz
 * @create 2021/10/8 17:10
 */
public class LocalFlinkKafkaTestSource extends MyProducerTool {

    @Override
    protected String brokers() {
        return "localhost:9092";
    }

    @Override
    protected String topic() {
        return "flink-test";
    }

    @Override
    protected String message() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " send message .";
    }

    public static void main(String[] args) {
        new LocalFlinkKafkaTestSource().run(args);
    }
}
