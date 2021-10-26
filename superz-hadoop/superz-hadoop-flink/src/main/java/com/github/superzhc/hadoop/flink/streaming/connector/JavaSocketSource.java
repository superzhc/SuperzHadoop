package com.github.superzhc.hadoop.flink.streaming.connector;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author superz
 * @create 2021/10/22 17:35
 */
public class JavaSocketSource {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 连接Socket获取输入的数据
        DataStreamSource<String> text=env.socketTextStream("localhost",8421);
        text.print();

        env.execute("Socket Source");
    }
}
