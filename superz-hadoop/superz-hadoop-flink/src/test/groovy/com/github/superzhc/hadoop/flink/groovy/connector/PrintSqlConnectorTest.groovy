package com.github.superzhc.hadoop.flink.groovy.connector

class PrintSqlConnectorTest {
    void ddl() {
        String sql = """
CREATE TABLE print_table (
    f0 INT,
    f1 INT,
    f2 STRING,
    f3 DOUBLE
) WITH (
    'connector' = 'print'
)
"""
    }
}
