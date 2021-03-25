package com.github.superzhc.db;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {
    private static String driverClass = "com.mysql.cj.jdbc.Driver";
    private static String dbUrl =
            "jdbc:mysql://192.168.1.145:3306/superz_hadoop?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
    private static String userName = "root";
    private static String passWord = "123456";

    public static void main(String[] args) throws SQLException {
        DbUtils.loadDriver(driverClass);
        Connection conn= DriverManager.getConnection(dbUrl,userName,passWord);
        QueryRunner queryRunner=new QueryRunner();
        List<Map<String,Object>> lst=queryRunner.query(conn,"",new MapListHandler());

    }
}
