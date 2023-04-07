package com.github.superzhc.hadoop.flink.groovy.connector

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment
import org.junit.Before

class JDBCSqlConnectorTest {
    StreamExecutionEnvironment env;
    TableEnvironment tEnv;

    @Before
    void setUp() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        tEnv = StreamTableEnvironment.create(env);
    }

    void ddl() {
        String sql ="""
CREATE TABLE MyUserTable (
    id BIGINT,
    name STRING,
    age INT,
    status BOOLEAN,
    PRIMARY KEY (id) NOT ENFORCED
) WITH (
    'connector' = 'jdbc',
    'url' = 'jdbc:mysql://localhost:3306/mydatabase',
    'table-name' = 'users'
)
"""
    }
}
