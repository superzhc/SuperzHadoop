package com.github.superzhc.hadoop.flink.metric;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.Gauge;
import org.apache.flink.metrics.SimpleCounter;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * 指标瞬时值
 * <p>
 * Gauge 是最简单的 Metrics ，它反映一个指标的瞬时值。比如要看现在 TaskManager 的 JVM heap 内存用了多少，就可以每次实时的暴露一个 Gauge，Gauge 当前的值就是 heap 使用的量。
 *
 * @author superz
 * @create 2022/3/11 11:35
 **/
public class FlinkMetricGauge {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Map<String, String> fakerConfigs = new HashMap<>();
        fakerConfigs.put(JavaFakerSource.field("name"), FakerUtils.Expression.NAME);
        fakerConfigs.put(JavaFakerSource.field("age"), FakerUtils.Expression.age(1, 80));
        fakerConfigs.put(JavaFakerSource.field("sex"), FakerUtils.Expression.SEX);
        DataStream<String> ds = env.addSource(new JavaFakerSource(fakerConfigs));

        ds = ds.map(new RichMapFunction<String, String>() {
            private transient int valueToExpose = 0;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                getRuntimeContext()
                        .getMetricGroup()
                        .gauge("SuperzGauge", new Gauge<Integer>() {
                            @Override
                            public Integer getValue() {
                                return valueToExpose;
                            }
                        });
            }

            @Override
            public String map(String s) throws Exception {
                valueToExpose++;
                return s;
            }
        });

        ds.print();

        env.execute("gauge metric");
    }
}
