package com.github.superzhc.hadoop.flink.streaming.connector;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Connector的作用就相当于一个连接器，连接 Flink 计算引擎跟外界存储系统。
 *
 * Flink里有以下几种方式，当然也不限于这几种方式可以跟外界进行数据交换：
 * 1. Flink里面预定义了一些source和sink
 * 2. FLink内部也提供了一些Boundled connectors
 * 3. 可以使用第三方apache Bahir项目中提供的连接器
 * 4. 通过异步IO方式
 * @author superz
 * @create 2021/10/27 9:01
 */
public class ConnectorMain {
    public static void main(String[] args) {
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> ds=env.fromElements("x");
        //ds.writeToSocket()
    }
}
