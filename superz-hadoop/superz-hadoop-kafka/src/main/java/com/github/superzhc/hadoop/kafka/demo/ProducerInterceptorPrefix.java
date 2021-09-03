package com.github.superzhc.hadoop.kafka.demo;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * 生产者拦截器示例
 * 代码中使用方式：props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,ProducerInterceptorPrefix.class.getName());
 * 2020年07月24日 superz add
 */
public class ProducerInterceptorPrefix implements ProducerInterceptor<String, String>
{
    private volatile long sendTotal = 0;
    private volatile long sendSuccess = 0;

    /**
     * 给每条消息添加前缀
     * @param record
     * @return
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        String modifyValue = "★" + record.value();
        return new ProducerRecord<>(record.topic(), record.partition(), record.timestamp(), record.key(), modifyValue,
                record.headers());
    }

    /**
     * 计算发送消息的成功率
     * @param metadata
     * @param exception
     */
    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        sendTotal++;
        if (null == exception)
            sendSuccess++;
    }

    @Override
    public void close() {
        double successRatio = ((double) sendSuccess) / sendTotal;
        System.out.printf("发送成功率= %f\n", successRatio * 100);
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
