package com.github.superzhc.hadoop.flink.groovy.connector

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment
import org.junit.Before
import org.junit.Test

class DataGenSqlConnectorTest {
    StreamExecutionEnvironment env;
    TableEnvironment tEnv;

    @Before
    public void setUp() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        tEnv = StreamTableEnvironment.create(env);
    }

    @Test
    void ddl_bounded() {
        String sql = """
CREATE TABLE datagen_bounded (
    f_sequence INT,
    f_random INT,
    f_random_str STRING,
    ts AS localtimestamp,
    WATERMARK FOR ts AS ts
) WITH (
    'connector' = 'datagen',
    'rows-per-second'='5',
    -- 字段为序列生成器，因此此表为有界表
    'fields.f_sequence.kind'='sequence',
    'fields.f_sequence.start'='1',
    'fields.f_sequence.end'='1000',
    'fields.f_random.min'='1',
    'fields.f_random.max'='1000',
    'fields.f_random_str.length'='10'
)
"""
        tEnv.executeSql(sql)

        tEnv.executeSql("SELECT * FROM datagen_bounded").print()
    }

    @Test
    void ddl_unbounded() {
        String sql = """
CREATE TABLE datagen_unbounded (
    f_random INT,
    f_random_str STRING,
    ts AS localtimestamp,
    WATERMARK FOR ts AS ts
) WITH (
    'connector' = 'datagen',
    'rows-per-second'='5',
    'fields.f_random.min'='1',
    'fields.f_random.max'='1000',
    'fields.f_random_str.length'='10'
)
"""

        tEnv.executeSql(sql)
        tEnv.executeSql("SELECT * FROM datagen_unbounded").print()
    }
}
