package com.github.superzhc.groovy

import groovy.sql.Sql
import org.junit.Test

class SqlTest {
    String url = "jdbc:mysql://127.0.0.1:3306/news_dw"
    String username = "root"
    String password = "123456"

    @Test
    void tables() {
        def sql = Sql.newInstance(url, username, password)
        sql.eachRow("SHOW TABLES") {
            row -> println(row[0])
        }
        sql.close()
    }
}
