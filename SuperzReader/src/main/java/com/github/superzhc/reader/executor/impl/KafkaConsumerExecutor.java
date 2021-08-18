package com.github.superzhc.reader.executor.impl;

import com.github.superzhc.reader.datasource.impl.KafkaConsumerDatasource;
import com.github.superzhc.reader.param.impl.KafkaConsumerParam;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;

/**
 * @author superz
 * @create 2021/8/17 18:13
 */
public class KafkaConsumerExecutor {
    private KafkaConsumerDatasource dataSource;
    private KafkaConsumerParam param;

    public KafkaConsumerExecutor(KafkaConsumerDatasource dataSource, KafkaConsumerParam param) {
        this.dataSource = dataSource;
        this.param = param;
    }

    public void execute() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(dataSource.convert());
        consumer.subscribe(Arrays.asList(param.getTopics()));
        try {
            while (true) {
                // 100 是超时时间（ms），在该时间内 poll 会等待服务器返回数据
                ConsumerRecords<String, String> records = consumer.poll(100);

                // poll 返回一个记录列表。
                // 每条记录都包含了记录所属主题的信息、记录所在分区的信息、记录在分区里的偏移量，以及记录的键值对。
                for (ConsumerRecord<String, String> record : records) {
                    /*log.debug("topic=%s, partition=%s, offset=%d, customer=%s, country=%s",
                            record.topic(), record.partition(), record.offset(),
                            record.key(), record.value());*/

//                    int updatedCount = 1;
//                    if (custCountryMap.countainsValue(record.value())) {
//                        updatedCount = custCountryMap.get(record.value() ) + 1;
//                        custCountryMap.put(record.value(), updatedCount);
//
//                        JSONObject json = new JSONObject(custCountryMap);
//                        System.out.println(json.toString());
//                    }
                }
            }
        } finally {
            // 关闭消费者,网络连接和 socket 也会随之关闭，并立即触发一次再均衡
            consumer.close();
        }
    }
}
