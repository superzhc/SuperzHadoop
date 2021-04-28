package com.github.superzhc.flink.demo.streaming;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * @author superz
 */
public class JavaStreamingWindowWordCount {
    public static void main(String[] args) throws Exception {
        //1. 获取一个执行环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
//        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //2. 加载/创建初始化数据
        DataStream<String> text = env.socketTextStream("vm1", 9999,"\n");

        //3. 指定操作数据的Transaction算子
        DataStream<Tuple2<String, Long>> windowWordCounts = text
                .flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
                    @Override
                    public void flatMap(String s, Collector<Tuple2<String, Long>> collector) throws Exception {
                        for (String word : s.split("\\s")) {
                            collector.collect(new Tuple2<>(word, 1L));
                        }
                    }
                })//
                .keyBy(0)//
                .timeWindow(Time.seconds(3), Time.seconds(1))//
                //.window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .reduce(new ReduceFunction<Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> reduce(Tuple2<String, Long> t2, Tuple2<String, Long> t1) throws Exception {
                        return new Tuple2<>(t1.f0, t1.f1 + t2.f1);
                    }
                });

        //4. 指定计算好的数据的存放位置
        windowWordCounts.print();

        //5. 调用execute()出发执行程序
        env.execute("Socket Window WordCount");
    }
}
