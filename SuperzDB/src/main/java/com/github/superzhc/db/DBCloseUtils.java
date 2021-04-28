package com.github.superzhc.db;

import java.sql.*;

/**
 * 2020年07月16日 superz add
 */
public class DBCloseUtils
{
    public static void close(Connection connection) {
        try {
            if (null != connection)
                connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Statement stmt) {
        try {
            if (null != stmt)
                stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(PreparedStatement pstmt) {
        try {
            if (null != pstmt)
                pstmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet rs) {
        try {
            if (null != rs)
                rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
