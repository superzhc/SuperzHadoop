package com.github.superzhc.hadoop.clickhouse.groovy.table.engine

import groovy.sql.Sql
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * 集成引擎
 *
 * @author superz
 * @create 2023/4/11 10:17
 * */
class IntegrationTest {
    String driver = "com.clickhouse.jdbc.ClickHouseDriver"
    String url = "jdbc:clickhouse://127.0.0.1:8123"
    String db = "my_dw"

    Sql jdbc = null

    @Before
    void setUp() throws Exception {
        jdbc = Sql.newInstance(url, new Properties(), driver)
    }

    @After
    void tearDown() throws Exception {
        jdbc?.close()
    }

    // 【待解决】
//    @Test
    void testS3() {
        String table = "t_202304111626"

        String sql = ""

        sql = "DROP TABLE ${db}.${table}"
        jdbc.execute(sql)

        sql = """
CREATE TABLE ${db}.$table (name String, value UInt32)
ENGINE = S3('s3a://superz/demo/ch/${table}.csv', 'admin', 'admin123456', 'CSV')
"""
        jdbc.execute(sql)

        sql = "INSERT INTO ${db}.${table} VALUES ('one', 1), ('two', 2), ('three', 3)"
        jdbc.executeInsert(sql)

        sql = "SELECT * FROM ${db}.${table} LIMIT 2"
        jdbc.eachRow(sql) {
            println(it)
        }
    }
}
