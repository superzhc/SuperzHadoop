package com.github.superzhc.hadoop.kafka.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * 2020年04月28日 superz add
 */
public class ConsumerRecordUtils {
    public static String human(ConsumerRecords<String, String> records) {
        if (null == records) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("数据条数：").append(records.count()).append("\n");
        for (ConsumerRecord<String, String> record : records) {
            sb.append(human(record)).append("\n");
        }
        return sb.toString();
    }

    public static String human(ConsumerRecord<String, String> record) {
        return String.format("主题[%s]的第[%d]分区的偏移量[%s]的信息[%s:%s(%s)]", record.topic(), record.partition(),
                record.offset(), record.key(), record.value(), record.timestamp());
    }
}
