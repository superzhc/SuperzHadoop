package com.github.superzhc.hadoop.flink.streaming.time.watermark;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * 2020年11月24日 superz add
 */
public class JavaAssignAscendingTimestampsDemo
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

        // 使用系统默认Ascending分配时间信息和Watermark
        SingleOutputStreamOperator<Tuple3<String, Long, Integer>> withTimestampAndWatermarks = // dss.assignAscendingTimestamps();
                // 是否和上面的scala函数是否一致？？？
                dss.assignTimestampsAndWatermarks(new AscendingTimestampExtractor<Tuple3<String, Long, Integer>>()
                {
                    @Override
                    public long extractAscendingTimestamp(Tuple3<String, Long, Integer> element) {
                        return element.f2;
                    }
                });

        // 对数据集进行窗口运算
        SingleOutputStreamOperator<Tuple3<String, Long, Integer>> result = withTimestampAndWatermarks.keyBy(0)
                .timeWindow(Time.seconds(10)).sum(2);
    }
}
