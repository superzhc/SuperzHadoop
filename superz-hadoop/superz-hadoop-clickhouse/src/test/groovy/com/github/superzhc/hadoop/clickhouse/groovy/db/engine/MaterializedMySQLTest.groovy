package com.github.superzhc.hadoop.clickhouse.groovy.db.engine


import groovy.sql.Sql
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <b>这是一个实验性的特性，不应该在生产中使用。</b>
 *
 * 创建ClickHouse数据库，包含MySQL中所有的表，以及这些表中的所有数据。
 * ClickHouse服务器作为MySQL副本工作。它读取binlog并执行DDL和DML查询。
 *
 * 这个功能是实验性的。
 * @author superz
 * @create 2023/4/11 9:17
 * */
class MaterializedMySQLTest {
    String driver = "com.clickhouse.jdbc.ClickHouseDriver"
    String url = "jdbc:clickhouse://127.0.0.1:8123"

    Sql jdbc = null

    @Before
    void setUp() throws Exception {
        jdbc = Sql.newInstance(url, new Properties(), driver)
    }

    @After
    void tearDown() throws Exception {
        jdbc?.close()
    }

    @Test
    void databases() {
        String sql = """SHOW DATABASES"""

        jdbc.eachRow(sql) {
            row -> println(row[0])
        }
    }

    // 异常，待修复
//    @Test
    void createDatabase() throws Exception {
        String sql = """
CREATE DATABASE IF NOT EXISTS mysql_dw 
ENGINE = MaterializeMySQL('mysql5.7:3306', 'news_dw', 'root', '123456') 
SETTINGS
        allows_query_when_mysql_lost=true,
        max_wait_time_when_mysql_unavailable=10000
"""

        jdbc.execute(sql)
    }
}
