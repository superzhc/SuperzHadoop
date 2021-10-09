package com.github.superzhc.hadoop.flink.streaming;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaNoParallelSource;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 自定义数据源使用
 * 2020年11月19日 superz add
 */
public class JavaCustomSourceDemo
{
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
        // 自定义数据源使用
        DataStreamSource<Long> dss;
        dss = env.addSource(new JavaNoParallelSource());
        // dss = env.addSource(new JavaParallelSource());

        SingleOutputStreamOperator<Tuple1<Long>> val= dss.map(new MapFunction<Long, Tuple1<Long>>()
        {
            @Override public Tuple1<Long> map(Long value) throws Exception {
                return new Tuple1<>(value);
            }
        });

        dss.print().setParallelism(1);
        env.execute("custom source demo");
    }
}
