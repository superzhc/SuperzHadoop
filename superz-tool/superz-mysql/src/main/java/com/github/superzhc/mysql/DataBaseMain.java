package com.github.superzhc.mysql;

import com.github.superzhc.common.jdbc.JdbcHelper;

/**
 * @author superz
 * @create 2022/1/10 14:23
 */
public class DataBaseMain {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            System.out.println(String.join("\n", jdbc.tables()));
        }
    }
}
