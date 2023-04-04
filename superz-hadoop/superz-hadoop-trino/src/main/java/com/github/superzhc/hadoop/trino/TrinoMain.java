package com.github.superzhc.hadoop.trino;

import com.github.superzhc.common.jdbc.JdbcHelper;

/**
 * @author superz
 * @create 2022/12/13 14:41
 **/
public class TrinoMain {
    public static void main(String[] args) {


        String url;
        url = "jdbc:trino://xxx:xx";

        try (JdbcHelper jdbc = new JdbcHelper(url, "root", null)) {

        }
    }
}
