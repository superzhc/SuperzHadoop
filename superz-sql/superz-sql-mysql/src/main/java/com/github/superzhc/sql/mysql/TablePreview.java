package com.github.superzhc.sql.mysql;

import com.github.superzhc.common.jdbc.JdbcHelper;

/**
 * @author superz
 * @create 2021/12/21 17:38
 */
public class TablePreview {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            jdbc.preview("fund_index_basic", 500);
        }
    }
}
