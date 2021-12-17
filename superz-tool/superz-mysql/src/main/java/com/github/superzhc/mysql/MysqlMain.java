package com.github.superzhc.mysql;

import com.github.superzhc.common.jdbc.JdbcHelper;

import java.sql.Connection;

/**
 * @author superz
 * @create 2021/12/17 11:32
 */
public class MysqlMain {
    public void connectionTemplate(String url, String username, String password) {
        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            Connection connection = jdbc.getConnection();
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            Long count = jdbc.aggregate("select count(*) from penguin");
            System.out.println(count);
        }
    }
}
