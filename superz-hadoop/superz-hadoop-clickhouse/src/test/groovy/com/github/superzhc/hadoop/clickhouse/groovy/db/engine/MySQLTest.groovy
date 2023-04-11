package com.github.superzhc.hadoop.clickhouse.groovy.db.engine

import groovy.sql.Sql
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * MySQL引擎用于将远程的MySQL服务器中的表映射到ClickHouse中，并允许您对表进行INSERT和SELECT查询，以方便您在ClickHouse与MySQL之间进行数据交换
 *
 * MySQL数据库引擎会将对其的查询转换为MySQL语法并发送到MySQL服务器中，因此您可以执行诸如SHOW TABLES或SHOW CREATE TABLE之类的操作。
 *
 * 不支持如下操作：
 * 1. RENAME
 * 2. CREATE TABLE
 * 3. ALTER
 *
 * @author superz
 * @create 2023/4/11 9:39
 * */
class MySQLTest {
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
        String sql = """
CREATE DATABASE IF NOT EXISTS mysql_dw ENGINE = MySQL('mysql5.7:3306', 'news_dw', 'root', '123456')
"""

        jdbc.execute(sql)
    }

    @Test
    void tables() {
        String sql = "SHOW TABLES FROM mysql_dw"

        jdbc.eachRow(sql) {
            println(it[0])
        }
    }

    @Test
    void table0() {
        String sql = "SHOW CREATE TABLE mysql_dw.my_configs"

        jdbc.eachRow(sql) {
            println(it)
        }
    }

    @Test
    void table() {
        String sql = "DESCRIBE mysql_dw.my_configs"

        jdbc.eachRow(sql) {
            println(it)
        }
    }

    @Test
    void dql() {
        String sql = """ SELECT * FROM mysql_dw.my_configs """

        jdbc.eachRow(sql) {
            println(it)
        }
    }

    @Test
    void dml() {
        String sql = """
INSERT INTO mysql_dw.my_configs
VALUES(2,'clickhouse-insert','clickhouse_insert','xxxx',null,null,null)
"""

        def result = jdbc.executeInsert(sql)
        println(result)
    }
}
