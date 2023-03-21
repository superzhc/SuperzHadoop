package com.github.superzhc.hadoop.flink.connector;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.KafkaSourceBuilder;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.junit.Before;

/**
 * @author superz
 * @create 2023/3/22 0:13
 */
public class KafkaConnectorTest {
    private static String brokers = "127.0.0.1:19092";

    StreamExecutionEnvironment env;

    @Before
    public void setUp() throws Exception {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
    }

    public void source() throws Exception {
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers(brokers)
                .setTopics("rsshub_shopping")
                .setGroupId("stream_test_group")
                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST))
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .setProperty("partition.discovery.interval.ms", "10000") // 每 10 秒检查一次新分区，默认不开启
                .build();

        DataStream<String> ds = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");
        ds.print();

        env.execute();
    }

    private <T> KafkaSourceBuilder<T> setStartingOffsets(KafkaSourceBuilder<T> builder) {
        return builder
                // 从消费组提交的位点开始消费，不指定位点重置策略
                .setStartingOffsets(OffsetsInitializer.committedOffsets())
                // 从消费组提交的位点开始消费，如果提交位点不存在，使用最早位点
                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST))
                // 从时间戳大于等于指定时间戳（毫秒）的数据开始消费
                .setStartingOffsets(OffsetsInitializer.timestamp(1657256176000L))
                // 从最早位点开始消费，默认使用
                .setStartingOffsets(OffsetsInitializer.earliest())
                // 从最末尾位点开始消费
                .setStartingOffsets(OffsetsInitializer.latest());
    }

    public void sink() throws Exception {
        DataStream<String> ds = null;

        KafkaSink<String> sink = KafkaSink.<String>builder()
                .setBootstrapServers(brokers)
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic("flink_test_sink")
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build()
                )
                .setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();

        ds.sinkTo(sink);

        env.execute();
    }
}
