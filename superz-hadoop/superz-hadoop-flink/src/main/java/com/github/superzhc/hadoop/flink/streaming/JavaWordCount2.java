package com.github.superzhc.hadoop.flink.streaming;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author superz
 * @create 2023/2/10 16:50
 **/
public class JavaWordCount2 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        String path = "E:\\data\\2022-04-22_fund_estimation.csv";
        DataStream<String> ds = env.readTextFile(path);
        ds.print();

        env.execute();
    }
}
