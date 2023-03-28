package com.github.superzhc.hadoop.hive;

import com.github.superzhc.common.jdbc.JdbcHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 2020年05月14日 superz add
 */
public class HiveMain
{
    private static String JDBC_DRIVER = "org.apache.hive.jdbc.HiveDriver";
    private static String CONNECTION_URL = "jdbc:hive2://log-platform03:10000/default";

    private static String CONNECTION_URL_XG="jdbc:hive2://10.90.18.212:10000/";

    public static void main(String[] args) {
        try(JdbcHelper jdbc=new JdbcHelper(JDBC_DRIVER,CONNECTION_URL,"root",null)){
//             jdbc.show("show create table superz_java_client_20221226001");
//            jdbc.show("select * from superz_java_client_20221213142129");
//            jdbc.ddlExecute("alter table superz_java_client_20221213150742 add columns(n1 string)");
            //jdbc.ddlExecute("create database IF NOT EXISTS superz_jdbc_hive_20221214");
//            jdbc.show("show databases");
            jdbc.show("show tables");
        }
    }
}
