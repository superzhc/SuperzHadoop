package com.github.superzhc.hadoop.flink.groovy.connector

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment
import org.junit.Before
import org.junit.Test

class KafkaSqlConnectorTest {
    StreamExecutionEnvironment env;
    TableEnvironment tEnv;

    @Before
    void setUp() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        tEnv = StreamTableEnvironment.create(env);
    }

    @Test
    void ddl() {
        String sql = """
CREATE TABLE rsshub(
    -- `partition` BIGINT METADATA VIRTUAL,
    title string,
    description string,
    guid string,
    link string,
    sourceType string,
    syncDate TIMESTAMP(3),
    rsshubKey string,
    pubDate TIMESTAMP(3),
    -- 不支持设置主键
    -- PRIMARY KEY(guid) NOT ENFORCED,
    -- 设置WaterMark
    WATERMARK FOR `pubDate` AS `pubDate` - INTERVAL '5' SECOND
) WITH (
    'connector' = 'kafka',
    'topic' = 'rsshub',
    'properties.bootstrap.servers' = 'localhost:19092',
    'properties.group.id' = 'testGroup',
    'scan.startup.mode' = 'earliest-offset',
    'format' = 'json',
    'json.ignore-parse-errors' = 'true'
)
"""
        tEnv.executeSql(sql)
    }
}
