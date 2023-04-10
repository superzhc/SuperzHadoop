package com.github.superzhc.hadoop.clickhouse.groovy.jdbc

import com.clickhouse.jdbc.ClickHouseDataSource

/**
 * @author superz
 * @create 2023/4/10 17:25
 * */
class JdbcTest {
    String driver = "com.clickhouse.jdbc.ClickHouseDriver"
    String url = "jdbc:clickhose://127.0.0.1:8123"

    void test() {
        ClickHouseDataSource dataSource = new ClickHouseDataSource(url, new Properties())
        def conn = dataSource.getConnection()
    }
}
