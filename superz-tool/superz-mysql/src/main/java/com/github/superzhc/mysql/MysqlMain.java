package com.github.superzhc.mysql;

import com.github.superzhc.common.jdbc.JdbcHelper;

import java.sql.Connection;

/**
 * 使用maven打包成包含依赖的jar包，执行如下语句：
 * java -cp superz-mysql-0.2.0-jar-with-dependencies.jar com.github.superzhc.mysql.MysqlMain "jdbc:mysql://localhost:3306/scm?useSSL=false&useUnicode=true&characterEncoding=utf-8" "root" "root" "select 1"
 *
 * @author superz
 * @create 2021/12/17 11:32
 */
public class MysqlMain {
    /*
    public void connectionTemplate(String url, String username, String password) {
        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            Connection connection = jdbc.getConnection();
        }
    }
     */

    public static void main(String[] args) {
        String url;//"jdbc:mysql://localhost:3306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username; //"root";
        String password; //"123456";
        String sql;
        if (args.length == 4) {
            url = args[0];
            username = args[1];
            password = args[2];
            sql = args[3];
        } else if (args.length == 2) {
            url = args[0];
            username = null;
            password = null;
            sql = args[1];
        } else {
            throw new RuntimeException("请正确配置数据库连接");
        }

        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            jdbc.show(sql);
        }
    }
}
