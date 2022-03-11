package com.github.superzhc.hadoop.flink.metric;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Gauge;
import org.apache.flink.metrics.Meter;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * Meter 平均值
 * 用来记录一个指标在某个时间段内的平均值。
 *
 * @author superz
 * @create 2022/3/11 13:54
 **/
public class FlinkMetricMeter {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Map<String, String> fakerConfigs = new HashMap<>();
        fakerConfigs.put(JavaFakerSource.field("name"), FakerUtils.Expression.NAME);
        fakerConfigs.put(JavaFakerSource.field("age"), FakerUtils.Expression.age(1, 80));
        fakerConfigs.put(JavaFakerSource.field("sex"), FakerUtils.Expression.SEX);
        DataStream<String> ds = env.addSource(new JavaFakerSource(fakerConfigs));

        ds = ds.map(new RichMapFunction<String, String>() {
            private transient Meter meter;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                getRuntimeContext()
                        .getMetricGroup()
                        .meter("SuperzMeter", new Meter() {
                            @Override
                            public void markEvent() {

                            }

                            @Override
                            public void markEvent(long l) {

                            }

                            @Override
                            public double getRate() {
                                return 0;
                            }

                            @Override
                            public long getCount() {
                                return 0;
                            }
                        });
            }

            @Override
            public String map(String s) throws Exception {
                this.meter.markEvent();
                return s;
            }
        });

        ds.print();

        env.execute("meter metric");
    }
}
