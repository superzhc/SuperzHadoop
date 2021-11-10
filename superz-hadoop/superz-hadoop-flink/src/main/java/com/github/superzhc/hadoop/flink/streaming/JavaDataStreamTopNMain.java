package com.github.superzhc.hadoop.flink.streaming;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.HashMap;
import java.util.Map;

/**
 * Top N
 *
 * @author superz
 * @create 2021/11/9 19:09
 */
public class JavaDataStreamTopNMain {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Map<String, String> fakerConfigs = new HashMap<>();
        fakerConfigs.put("fields.name.expression", FakerUtils.Expression.name());
        fakerConfigs.put("fields.age.expression", FakerUtils.Expression.age(1, 80));
        fakerConfigs.put("fields.date.expression", FakerUtils.Expression.pastDate(5));
        DataStream<String> ds = env.addSource(new JavaFakerSource(fakerConfigs));

        final ObjectMapper mapper = new ObjectMapper();

        ds.keyBy(new KeySelector<String, String>() {
                    @Override
                    public String getKey(String value) throws Exception {
                        return mapper.readTree(value).get("sex").asText();
                    }
                })
                .window(SlidingProcessingTimeWindows.of(Time.minutes(3), Time.minutes(1)))
        ;

        env.execute("java datastream topN main");
    }
}
