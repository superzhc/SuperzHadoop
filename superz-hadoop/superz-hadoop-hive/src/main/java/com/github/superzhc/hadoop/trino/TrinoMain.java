package com.github.superzhc.hadoop.trino;

import com.github.superzhc.common.jdbc.JdbcHelper;

/**
 * @author superz
 * @create 2022/12/13 14:41
 **/
public class TrinoMain {
    public static void main(String[] args) {

        /**
         * Trino 支持以下 JDBC 的 URL 格式：
         * jdbc:trino://host:port
         * jdbc:trino://host:port/catalog
         * jdbc:trino://host:port/catalog/schema
         */
        // String url = "jdbc:trino://log-platform01:8099/elasticsearch/default";
        String url = "jdbc:trino://hanyun-2:7099/hudi/default";

        try (JdbcHelper jdbc = new JdbcHelper(url, "root", null)) {
            jdbc.show("select count(*) from superz_java_client_20221213150742");
//            jdbc.show("show tables");
        }
    }
}
