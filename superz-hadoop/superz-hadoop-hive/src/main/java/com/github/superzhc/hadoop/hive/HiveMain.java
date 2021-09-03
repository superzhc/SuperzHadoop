package com.github.superzhc.hadoop.hive;

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
    private static String CONNECTION_URL = "jdbc:hive2://ep-002.hadoop:10000/";

    static {
        try {
            Class.forName(JDBC_DRIVER);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            connection = DriverManager.getConnection(CONNECTION_URL);
            ps = connection.prepareStatement("select * from superz_test");
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1) + "-------" + rs.getString(2));
            }
            if (null != ps)
                ps.close();
            if (null != rs)
                rs.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (null != connection)
                    connection.close();
            }
            catch (Exception e1) {
            }
        }
    }
}
