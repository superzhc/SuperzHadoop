package com.github.superzhc.hadoop.flink.table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author superz
 * @create 2021/11/5 1:36
 */
public class FlinkSqlKafkaMain {
    public static String KAFKA_TABLE_SOURCE_DDL = "" +
            "CREATE TABLE user_behavior (\n" +
            "    user_id BIGINT,\n" +
            "    item_id BIGINT,\n" +
            "    category_id BIGINT,\n" +
            "    behavior STRING,\n" +
            "    ts TIMESTAMP(3)\n" +
            ") WITH (\n" +
            "    'connector.type' = 'kafka',  -- 指定连接类型是kafka\n" +
            "    'connector.version' = '0.11',  -- 与我们之前Docker安装的kafka版本要一致\n" +
            "    'connector.topic' = 'mykafka', -- 之前创建的topic \n" +
            "    'connector.properties.group.id' = 'flink-test-0', -- 消费者组，相关概念可自行百度\n" +
            "    'connector.startup-mode' = 'earliest-offset',  --指定从最早消费\n" +
            "    'connector.properties.zookeeper.connect' = 'localhost:2181',  -- zk地址\n" +
            "    'connector.properties.bootstrap.servers' = 'localhost:9092',  -- broker地址\n" +
            "    'format.type' = 'json'  -- json格式，和topic中的消息格式保持一致\n" +
            ")";

    public static void main(String[] args) {
//        //构建StreamExecutionEnvironment
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
//        //构建EnvironmentSettings 并指定Blink Planner
//        EnvironmentSettings bsSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
//
//        //构建StreamTableEnvironment
//        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, bsSettings);
//
//        tEnv.executeSql(KAFKA_TABLE_SOURCE_DDL);
//
//        Table table=tEnv.sqlQuery("select * from user_behavior");
    }
}
