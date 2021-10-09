package com.github.superzhc.hadoop.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * Flink 开发步骤流程：
 * <p>
 * 1. 获得一个执行环境；
 * 2. 加载/创建初始化数据；
 * 3. 指定操作数据的 Transaction 算子；
 * 4. 指定计算好的数据的存放位置；
 * 5. 调用 `execute()` 触发执行程序。
 * <p>
 * 注意：Flink 程序是延迟计算的，只有最后调用 `execute()` 方法的时候才会真正触发执行程序。
 *
 * @author superz
 * @create 2021/10/8 18:10
 */
public class DevelopmentProcess {
    /*
     * Flink 编程模型：
     * Flink Job = Source + Transformation + Sink
     * */

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        /* Source */
        DataStreamSource<String> source = env.socketTextStream("localhost", 9090);

        /* Transformation */
        DataStream<Tuple2<String, Long>> wordCount = source.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
                        String[] words = value.split("\\s");
                        for (String word : words) {
                            out.collect(new Tuple2<>(word, 1L));
                        }
                    }
                }).keyBy(0)//
                .timeWindow(Time.seconds(2), Time.seconds(1))//指定时间窗口大小为2s，指定时间间隔为1s
                .sum(1);

        /* Sink */
        // 注意 `new PrintSinkFunction<>()` 其就是打印，简写方式 `wordCount.print()`
        wordCount.addSink(new PrintSinkFunction<>());

        env.execute("Development Process");
    }
}
