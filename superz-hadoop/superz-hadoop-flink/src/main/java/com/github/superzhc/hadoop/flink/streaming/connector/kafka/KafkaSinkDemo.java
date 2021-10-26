package com.github.superzhc.hadoop.flink.streaming.connector.kafka;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2021/10/25 15:36
 */
public class KafkaSinkDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        Map<String, String> map = new HashMap<>();
        map.put("fields.name.expression", "#{Name.name}");
        map.put("fields.age.expression", "#{number.number_between '1','80'}");
        map.put("fields.sex.expression","#{regexify '(男|女){1}'}");
        map.put("fields.create_time.expression", "#{date.past '5','SECONDS'}");
        JavaFakerSource source=new JavaFakerSource(map);

        DataStream<String> ds=env.addSource(source);

        ds.print();

        ds.addSink(new FlinkKafkaProducer<String>("127.0.0.1:19092","flink-datastream-faker",new SimpleStringSchema()));
        env.execute("flink sink");
    }
}