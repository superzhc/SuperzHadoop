package com.github.superzhc.demo.java;

import com.github.superzhc.util.PropertiesUtils;

import java.sql.*;

/**
 * 2020年07月08日 superz add
 */
public class JDBCDemo
{
    static{
        PropertiesUtils.read("./jdbc.properties");
    }

    public static void main(String[] args) throws Exception {
        //1.加载驱动程序
        Class.forName("com.mysql.jdbc.Driver");
        //2.获得数据库的连接
        Connection conn = DriverManager.getConnection(PropertiesUtils.getOrDefault("url","jdbc:mysql://127.0.0.1:3306/superz?user=root&password=123456&useSSL=false"));
        //3.通过数据库的连接操作数据库，实现增删改查
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select rowguid,d1,updatetime from test1 where d1>=678294000000");//选择import java.sql.ResultSet;

        while(rs.next()){//如果对象中有数据，就会循环打印出来
//            Object obj=rs.getObject("m");
//            System.out.println(obj==null?null:obj.getClass());
//            Object obj1=rs.getObject("rowguid");
//            System.out.println(obj1.getClass());
//            Timestamp ts=rs.getTimestamp("m");
//            System.out.println(null==ts?null:ts.getTime());
//            System.out.println(rs.getMetaData().getColumnTypeName(2));
            Date d1=rs.getDate("d1");
            System.out.println(null==d1?null:d1.getTime());
        }
    }
}
