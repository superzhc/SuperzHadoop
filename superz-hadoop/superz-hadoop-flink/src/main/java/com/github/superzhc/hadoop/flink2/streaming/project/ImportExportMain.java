package com.github.superzhc.hadoop.flink2.streaming.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.jackson.JsonUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author superz
 * @create 2022/11/29 14:01
 **/
public class ImportExportMain {
    private static final String brokers = "bigdata9:6667,bigdata10:6667,bigdata11:6667";

    public static void main(String[] args) throws Exception {
        String topic = "im_export_Work"/*"im_export_Raw"*/;

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers(brokers)
                .setTopics(topic)
//                .setGroupId("consumer-flink-user:superz:0")
                .setGroupId("consumer-flink-user:superz:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")))
                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST))
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        // 只有一个分区，并行度设置为1，设置多了没意义
        DataStream<String> ds = env.fromSource(source, WatermarkStrategy.noWatermarks(), "kafka source:" + topic).setParallelism(1);

        ds.filter(new FilterFunction<String>() {
            @Override
            public boolean filter(String value) throws Exception {
                String productId= JsonUtils.string(value,"productID");
                return "A151F014B00009".equals(productId);
            }
        }).print();

        //3.执行
        env.execute("import_export project");
    }
}
