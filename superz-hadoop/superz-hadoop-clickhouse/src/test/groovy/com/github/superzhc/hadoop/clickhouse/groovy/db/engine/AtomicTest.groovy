package com.github.superzhc.hadoop.clickhouse.groovy.db.engine

import groovy.sql.Sql
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * 它支持非阻塞的DROP TABLE和RENAME TABLE查询和原子的EXCHANGE TABLES t1 AND t2查询。默认情况下使用Atomic数据库引擎。
 *
 * @author superz
 * @create 2023/4/11 9:55
 * */
class AtomicTest {
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

    @Test
    void createDatabase() {
        String sql = "CREATE DATABASE my_dw ENGINE = Atomic"

        def result = jdbc.execute(sql)
        println(result)
    }

    @Test
    void dropDatabase(){
        String sql="DROP DATABASE my_dw"

        def result = jdbc.execute(sql)
        println(result)
    }
}
