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
    private static String CONNECTION_URL = "jdbc:hive2://log-platform02:10000/";

    public static void main(String[] args) {
        try(JdbcHelper jdbc=new JdbcHelper(JDBC_DRIVER,CONNECTION_URL,"root",null)){
             // jdbc.show("show tables");
            jdbc.show("select * from any_knew_hot_news");
        }
    }
}
