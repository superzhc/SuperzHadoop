package com.github.superzhc.flink.streaming.watermark;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * 2020年11月25日 superz add
 */
public class JavaBoundedOutOfOrdernessTimestampDemo
{
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 指定系统时间概念为Event Time
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        List<Tuple3<String, Long, Integer>> input = new ArrayList<>();
        input.add(new Tuple3("a", 1L, 1));
        input.add(new Tuple3("b", 1L, 1));
        input.add(new Tuple3("b", 3L, 1));

        DataStreamSource<Tuple3<String, Long, Integer>> dss = env.fromCollection(input);

        SingleOutputStreamOperator<Tuple3<String, Long, Integer>> withTimestampAndWatermarks = dss
                .assignTimestampsAndWatermarks(
                        new BoundedOutOfOrdernessTimestampExtractor<Tuple3<String, Long, Integer>>(Time.seconds(10))
                        {
                            @Override
                            public long extractTimestamp(Tuple3<String, Long, Integer> element) {
                                return element.f2;
                            }
                        });
    }
}
