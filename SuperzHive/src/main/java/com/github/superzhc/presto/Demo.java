package com.github.superzhc.presto;

import com.facebook.presto.jdbc.PrestoConnection;
import com.facebook.presto.jdbc.PrestoStatement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimeZone;

/**
 * 2020年07月27日 superz add
 */
public class Demo
{
    public static void main(String[] args) {
        // 设置时区，这里必须要设置
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        try {
            Class.forName("com.facebook.presto.jdbc.PrestoDriver");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        PrestoConnection connection = null;
        try {
            connection = (PrestoConnection) DriverManager
                    .getConnection("jdbc:presto://ep-001.hadoop:8285/mysql/superz", "guest", null);

            PrestoStatement stmt = (PrestoStatement) connection.createStatement();

            String query = "select * from test1";
            ResultSet rs = stmt.executeQuery(query);

            int cn = rs.getMetaData().getColumnCount();
            int[] types = new int[cn];
            for (int i = 1; i <= cn; i++) {
                types[i - 1] = rs.getMetaData().getColumnType(i);
            }



            while (rs.next()) {
                for (int i = 0; i < cn; i++) {
                    System.out.printf(" %s", rs.getObject(i + 1));
                }
                System.out.println("");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
