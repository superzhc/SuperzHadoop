package com.github.superzhc.hadoop.flink.table.stream;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author superz
 * @create 2021/4/6 13:35
 */
public class StreamTableFirstDemo {
    public static void main(String[] args) {
        StreamExecutionEnvironment sEnv=StreamExecutionEnvironment.getExecutionEnvironment();

        StreamTableEnvironment sTableEnv= StreamTableEnvironment.create(sEnv);
    }
}
