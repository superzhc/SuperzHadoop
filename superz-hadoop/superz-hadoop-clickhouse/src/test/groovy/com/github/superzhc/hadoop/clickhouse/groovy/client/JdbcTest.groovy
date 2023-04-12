package com.github.superzhc.hadoop.clickhouse.groovy.client


import groovy.sql.Sql
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * @author superz
 * @create 2023/4/10 17:25
 * */
class JdbcTest {
    String driver = "com.clickhouse.jdbc.ClickHouseDriver"
    String url = "jdbc:clickhouse://127.0.0.1:8123"
    // jdbc连接未设置用户名，默认使用default用户名
    String username = "default"
    // 默认密码为空
    String password = ""

    Sql jdbc = null

    @Before
    void setUp() throws Exception {
//        ClickHouseDataSource dataSource = new ClickHouseDataSource(url, new Properties())
//        def conn = dataSource.getConnection()
        jdbc = Sql.newInstance(url, username, password, driver)
    }

    @After
    void tearDown() throws Exception {
        jdbc?.close()
    }

    @Test
    void version() {
        jdbc.eachRow("SELECT version()") { println(it) }
    }

    @Test
    void currentDatabase() {
        jdbc.eachRow("select currentDatabase()") { println(it) }
    }

    @Test
    void databases() {
        jdbc.eachRow("SHOW DATABASES") { println(it) }
    }

    @Test
    void tables() {
        String sql = """
SELECT database
     , if(engine LIKE '%%View', 'v', 'r') AS relkind
     , name                              AS relname
FROM system.tables
-- WHERE name NOT LIKE '.inner%%'
"""
        jdbc.eachRow(sql) { println(it) }
    }

    @Test
    void test() {
        String sql = """
SELECT database
     , table AS table_name
     , name
     , type
     , comment
FROM system.columns
WHERE database = 'INFORMATION_SCHEMA'
ORDER BY database, table, position
"""

        jdbc.eachRow(sql) { println(it) }
    }
}
