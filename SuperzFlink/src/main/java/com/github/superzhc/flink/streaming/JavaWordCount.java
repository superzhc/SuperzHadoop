package com.github.superzhc.flink.streaming;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * 2020年11月19日 superz add
 */
public class JavaWordCount
{
    public static void main(String[] args) throws Exception {
        // 获取运行环境
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();

        // 连接Socket获取输入的数据
        DataStreamSource<String> text=env.socketTextStream("localhost",9090);

        DataStream<Tuple2<String,Long>> wordCount=text.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>()
        {
            @Override public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
                String[] words=value.split("\\s");
                for(String word:words){
                    out.collect(new Tuple2<>(word,1L));
                }
            }
        }).keyBy(0)//
        .timeWindow(Time.seconds(2),Time.seconds(1))//指定时间窗口大小为2s，指定时间间隔为1s
        .sum(1)//在这里使用sum或者reduce都可以
        /*.reduce(new ReduceFunction<Tuple2<String, Long>>()
        {
            @Override public Tuple2<String, Long> reduce(Tuple2<String, Long> value1, Tuple2<String, Long> value2)
                    throws Exception {
                return new Tuple2<>(value1.f0, value1.f1+value2.f1);
            }
        })*/
        ;

        // 把数据打印到控制台并设置并行度
        wordCount.print().setParallelism(1);

        // 执行代码
        env.execute("WordCount");

    }
}
