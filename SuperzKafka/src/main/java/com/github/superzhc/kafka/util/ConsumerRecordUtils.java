package com.github.superzhc.kafka.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * 2020年04月28日 superz add
 */
public class ConsumerRecordUtils
{
    public static String human(ConsumerRecord<String, String> record) {
        return String.format("主题[%s]的第[%d]分区的偏移量[%s]的信息[%s:%s(%s)]", record.topic(), record.partition(),
                record.offset(), record.key(), record.value(), record.timestamp());
    }
}
