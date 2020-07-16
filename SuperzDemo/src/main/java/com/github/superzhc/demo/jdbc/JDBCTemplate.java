package com.github.superzhc.demo.jdbc;

import java.sql.*;

/**
 * 2020年07月16日 superz add
 */
public class JDBCTemplate
{
    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "";
    private static String username = "";
    private static String password = "";

    private static String fullUrl = "";// 带有用户名和密码

    public static void main(String[] args) {
        Connection connection = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        try {
            // 加载驱动程序
            Class.forName(driver);

            // 获取数据库连接
            connection = DriverManager.getConnection(fullUrl);

            String sql = "";

            // 创建Statement
            stmt = connection.createStatement();
            ResultSet rs1 = stmt.executeQuery(sql);

            // 创建PreparedStatement
            pstmt = connection.prepareStatement(sql);
            ResultSet rs2 = pstmt.executeQuery();

            // 遍历结果集
            while (rs1.next()) {
                // doing something
                System.out.println(rs1.getDate(1));
            }

            // 关闭资源
            rs1.close();
            rs2.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (stmt != null)
                    stmt.close();

                if (pstmt != null)
                    pstmt.close();

                if (null != connection)
                    connection.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
