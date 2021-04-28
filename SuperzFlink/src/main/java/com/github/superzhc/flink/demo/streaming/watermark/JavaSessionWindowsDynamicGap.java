package com.github.superzhc.flink.demo.streaming.watermark;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SessionWindowTimeGapExtractor;

/**
 * 2020年11月25日 superz add
 */
public class JavaSessionWindowsDynamicGap
{
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Tuple3<String, Long, Integer>> dss = env.fromElements(new Tuple3<>("a", 1L, 1),
                new Tuple3<>("b", 2L, 3));
        dss.keyBy(0)//
                .window(EventTimeSessionWindows.withDynamicGap(
                        // 实例化SessionWindowTimeGapExtractor接口
                        new SessionWindowTimeGapExtractor<Tuple3<String, Long, Integer>>()
                        {
                            @Override
                            public long extract(Tuple3<String, Long, Integer> element) {
                                // 动态指定返回Session Gap
                                return 0;
                            }
                        }))
        ;

    }
}
