package com.github.superzhc.hadoop.clickhouse.groovy.table.engine

import groovy.sql.Sql
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * 具有最小功能的轻量级引擎。当您需要快速写入许多小表（最多约100万行）并在以后整体读取它们时，该类型的引擎是最有效的。
 *
 * @author superz
 * @create 2023/4/11 10:08
 * */
class LogTest {
    String driver = "com.clickhouse.jdbc.ClickHouseDriver"
    String url = "jdbc:clickhouse://10.90.18.76:18123"
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

    @Test
    void testStripeLog() {
        String table = "t_202304120951"
        String sql = """
CREATE TABLE IF NOT EXISTS ${db}.${table}
(
    timestamp DateTime,
    message_type String,
    message String
)
ENGINE = StripeLog
"""
        jdbc.execute(sql)

        sql = "INSERT INTO ${db}.${table} VALUES (now(),'REGULAR','The first regular message')"
        jdbc.executeInsert(sql)

        sql = "INSERT INTO ${db}.${table} VALUES (now(),'REGULAR','The second regular message'),(now(),'WARNING','The first warning message')"
        jdbc.executeInsert(sql)

        sql = "SELECT * FROM ${db}.${table}"
        jdbc.eachRow(sql) {
            println(it)
        }
    }
}
