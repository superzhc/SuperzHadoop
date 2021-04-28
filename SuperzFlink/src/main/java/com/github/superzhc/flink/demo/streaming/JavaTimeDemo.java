package com.github.superzhc.flink.demo.streaming;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 2020年11月24日 superz add
 */
public class JavaTimeDemo
{
    public static void main(String[] args) {
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();

        // 时间概念设定
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        // env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
        // env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
    }
}
