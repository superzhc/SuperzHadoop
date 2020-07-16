package com.github.superzhc.demo.jdbc;

import com.github.superzhc.db.ResultSetUtils;

import java.sql.*;

/**
 * 当计划要多次使用SQL语句时使用。PreparedStatement接口在运行时接受输入参数
 * 2020年07月16日 superz add
 */
public class PreparedStatementDemo
{
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = DbCommonExample.example().getConnection();

//            String sql = "update test1 set name=? where rowguid=?";
//            pstmt = connection.prepareStatement(sql);
//            pstmt.setString(1, "superz");
//            pstmt.setInt(2, 4);
//
//            int num = pstmt.executeUpdate();
//            System.out.println(num);

            pstmt = connection.prepareStatement("select * from test1");
            ResultSet rs = pstmt.executeQuery();

            ResultSetUtils.print(rs);

            DBCloseUtils.close(rs);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            DBCloseUtils.close(pstmt);
            DBCloseUtils.close(connection);
        }
    }
}
