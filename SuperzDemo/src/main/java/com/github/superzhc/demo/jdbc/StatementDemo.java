package com.github.superzhc.demo.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 用于对数据库进行通用访问，在运行时使用静态SQL语句时很有用。Statement不能接受参数
 * 2020年07月16日 superz add
 */
public class StatementDemo
{
    public static void main(String[] args) {
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = DbCommonExample.example().getConnection();

            // 创建Statement对象
            stmt = connection.createStatement();

            String sql = "select * from test1";
            /**
             * 在创建Statement对象后，可以使用它来执行一个SQL语句，它有三个执行方法可以执行。
             */
            // 如果可以检索到ResultSet对象，则返回一个布尔值true；否则返回false。使用次方法执行SQL-DDL语句或需要使用真正的动态SQL，可适用于执行创建数据库，创建表的SQL语句等等
//            boolean b = stmt.execute(sql);
//            System.out.println(b);

            // 返回受SQL语句执行影响的行数。使用此方法执行预期会影响多行的SQL语句，例如：insert，update和delete语句
//            int i = stmt.executeUpdate("insert into superz.test1 values (111,'222')");
//            System.out.println(i);

            // 返回一个ResultSet对象
            ResultSet rs = stmt.executeQuery(sql);
            DBCloseUtils.close(rs);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            DBCloseUtils.close(stmt);
            DBCloseUtils.close(connection);
        }
    }
}
