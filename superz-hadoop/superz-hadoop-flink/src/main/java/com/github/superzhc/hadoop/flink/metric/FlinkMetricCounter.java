package com.github.superzhc.hadoop.flink.metric;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.SimpleCounter;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * Flink 监控指标：Counter（计数器）
 * 统计一个指标的总量
 *
 * @author superz
 * @create 2022/3/11 11:35
 **/
public class FlinkMetricCounter {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Map<String, String> fakerConfigs = new HashMap<>();
        fakerConfigs.put(JavaFakerSource.field("name"), FakerUtils.Expression.NAME);
        fakerConfigs.put(JavaFakerSource.field("age"), FakerUtils.Expression.age(1, 80));
        fakerConfigs.put(JavaFakerSource.field("sex"), FakerUtils.Expression.SEX);
        DataStream<String> ds = env.addSource(new JavaFakerSource(fakerConfigs));

        /* 使用方式：可以通过调用 counter 来创建和注册 MetricGroup */
        ds = ds.map(new RichMapFunction<String, String>() {
            private transient Counter counter;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                this.counter = getRuntimeContext().getMetricGroup().counter("CustomCounter", new SimpleCounter());
            }

            @Override
            public String map(String s) throws Exception {
                this.counter.inc();
                return s;
            }
        });

        ds.print();

        env.execute("counter metric");

    }
}
