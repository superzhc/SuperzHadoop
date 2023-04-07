package com.github.superzhc.hadoop.trino

import groovy.sql.Sql
import org.junit.After
import org.junit.Before
import org.junit.Test

class TrinoSQLTest {
    final static String DRIVER = "io.trino.jdbc.TrinoDriver"
    final static String URL = "jdbc:trino://10.90.15.221:7099"
    final static String USERNAME = "root"
    final static String PASSWORD = null

    Sql sql = null

    @Before
    void setUp() throws Exception {
        sql = Sql.newInstance(URL, USERNAME, PASSWORD, DRIVER)
    }

    @After
    void tearDown() throws Exception {
        if (null != sql) {
            sql.close()
        }
    }

    @Test
    void catalogs() {
        sql.eachRow("SHOW CATALOGS") { row ->
            println(row[0])
        }
    }

    @Test
    void databases() {
        sql.eachRow("SHOW SCHEMAS IN iceberg") {
            row -> println(row[0])
        }
    }

    @Test
    void tables() {
        sql.eachRow("SHOW TABLES IN iceberg.xgitbigdata") {
            row -> println(row[0])
        }
    }
}
