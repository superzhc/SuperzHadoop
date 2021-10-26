package com.github.superzhc.hadoop.kafka.demo;

import com.github.superzhc.hadoop.kafka.tool.MyProducerTool;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author superz
 * @create 2021/10/8 17:10
 */
public class LocalFlinkKafkaTestSource extends MyProducerTool {
    private String date = "";

    public LocalFlinkKafkaTestSource() {
        date = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    @Override
    protected String brokers() {
        return "127.0.0.1:19092";
    }

    @Override
    protected String topic() {
        return "flink-test" + date;
    }

    @Override
    protected String message() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " send message .";
    }

    public static void main(String[] args) {
        new LocalFlinkKafkaTestSource().run(args);
    }
}
