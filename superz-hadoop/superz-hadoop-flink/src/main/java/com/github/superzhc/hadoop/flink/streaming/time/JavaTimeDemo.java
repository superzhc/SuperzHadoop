package com.github.superzhc.hadoop.flink.streaming.time;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 在 Flink1.12 版本被标注为废弃，不推荐使用这种时间戳概念设定了
 * 2020年11月24日 superz add
 */
@Deprecated
public class JavaTimeDemo {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 时间概念设定
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        // env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
        // env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
    }
}
