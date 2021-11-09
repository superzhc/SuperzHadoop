package com.github.superzhc.hadoop.flink;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 反压机制
 * <p>
 * 反压是实时计算应用开发中，特别是流式计算中，十分常见的问题。反压意味着数据管道中某个节点成为瓶颈，处理速率跟不上上游发送数据的速率，而需要对上游进行限速。由于实时计算应用通常使用消息队列来进行生产端和消费端的解耦，消费端数据源是 pull-based 的，所以反压通常是从某个节点传导至数据源并降低数据源（比如 Kafka consumer）的摄入速率。
 *
 * @author superz
 * @create 2021/11/8 15:14
 */
public class BackPressure {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
        // env.setParallelism(2);
        Map<String, String> fakerConfigs = new HashMap<>();
        fakerConfigs.put("fields.name.expression", FakerUtils.Expression.name());
        fakerConfigs.put("fields.age.expression", FakerUtils.Expression.age(1, 80));
        fakerConfigs.put("fields.sex.expression", FakerUtils.Expression.options("F", "M"));
        fakerConfigs.put("fields.date.expression", FakerUtils.Expression.pastDate(5));
        DataStream<String> ds = env.addSource(new JavaFakerSource(fakerConfigs, 1000L));

        final ObjectMapper mapper = new ObjectMapper();
        ds.map(new MapFunction<String, Tuple4<String, Integer, String, LocalDateTime>>() {
                    @Override
                    public Tuple4<String, Integer, String, LocalDateTime> map(String value) throws Exception {
                        JsonNode node = mapper.readTree(value);
                        return Tuple4.of(
                                node.get("name").asText(),
                                node.get("age").asInt(),
                                node.get("sex").asText(),
                                FakerUtils.toLocalDateTime(node.get("date").asText())
                        );
                    }
                }).keyBy(new KeySelector<Tuple4<String, Integer, String, LocalDateTime>, String>() {
                    @Override
                    public String getKey(Tuple4<String, Integer, String, LocalDateTime> t4) throws Exception {
                        return t4.f2;
                    }
                })/*.map(new MapFunction<Tuple4<String, Integer, String, LocalDateTime>, String>() {
            @Override
            public String map(Tuple4<String, Integer, String, LocalDateTime> value) throws Exception {
                if ("F".equals(value.f2)) {
                    //Thread.sleep(500);
                } else {
                    Thread.sleep(1000 * 3);
                }
                return mapper.writeValueAsString(value);
            }
        })*/
                .reduce(new ReduceFunction<Tuple4<String, Integer, String, LocalDateTime>>() {
                    @Override
                    public Tuple4<String, Integer, String, LocalDateTime> reduce(Tuple4<String, Integer, String, LocalDateTime> value1, Tuple4<String, Integer, String, LocalDateTime> value2) throws Exception {
                        return Tuple4.of(/*value1.f0+","+value2.f0*/"faker", value1.f1 + value2.f1, value1.f2, LocalDateTime.now());
                    }
                }).setParallelism(2)
                .map(new MapFunction<Tuple4<String, Integer, String, LocalDateTime>, Object>() {
                    @Override
                    public Object map(Tuple4<String, Integer, String, LocalDateTime> value) throws Exception {
                        if ("F".equals(value.f2)) {
                            Thread.sleep(1000 * 3);
                        }
                        return value;
                    }
                }).setParallelism(2)
                .print().setParallelism(2)
        ;

        env.execute("javafaker source");
    }
}
