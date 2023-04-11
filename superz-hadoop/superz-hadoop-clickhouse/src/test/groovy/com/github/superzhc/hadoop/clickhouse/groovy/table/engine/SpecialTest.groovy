package com.github.superzhc.hadoop.clickhouse.groovy.table.engine

import groovy.sql.Sql
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * @author superz
 * @create 2023/4/11 16:44
 * */
class SpecialTest {
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

    /**
     * 数据源是以 Clickhouse 支持的一种输入格式（TabSeparated，Native等）存储数据的文件
     */
    @Test
    void testFile() {
        String table = "t_202304111644"

        String sql = ""

        sql = "DROP TABLE IF EXISTS ${db}.${table}"
        jdbc.execute(sql)

        sql = "CREATE TABLE ${db}.${table} (name String, value UInt32) ENGINE=File(TabSeparated)"
        jdbc.execute(sql)

        sql = "INSERT INTO ${db}.${table} VALUES ('one', 1), ('two', 2), ('three', 3)"
        jdbc.executeInsert(sql)

        sql = "SELECT * FROM ${db}.${table}"
        jdbc.eachRow(sql) {
            println(it)
        }
    }
}
