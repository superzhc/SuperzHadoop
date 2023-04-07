package com.github.superzhc.db.mysql.groovy

import groovy.sql.Sql
import org.junit.Before
import org.junit.Test

class SqlTest {
    String url = "jdbc:mysql://127.0.0.1:3306/news_dw?useSSL=false"
    String username = "root"
    String password = "123456"

    Sql sql;

    @Before
    void setUp() throws Exception {
        sql = Sql.newInstance(url, username, password)
    }

    @Test
    void tables() {
        sql.eachRow("SHOW TABLES") {
            row -> println(row[0])
        }
    }

    @Test
    void any_knew_hot_news() {
        sql.eachRow("SELECT * FROM any_knew_hot_news") {
            row -> println(row)
        }
    }
}
