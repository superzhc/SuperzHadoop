package com.github.superzhc.gis.postgis;

import org.postgresql.util.PGobject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author superz
 * @create 2021/9/1 17:42
 */
public class PostgreSQLDemo {
    public static void main(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection(PostgreSQLConfig.URL, PostgreSQLConfig.USERNAME, PostgreSQLConfig.PASSWORD);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from gis_demo");
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String name = rs.getString("name");
            /*Object geom = rs.getObject("geom");*/
            PGobject geom = (PGobject) rs.getObject("geom");
            System.out.println(id + "," + name + "," + geom.getType() + "," + geom.getValue());
        }
        rs.close();
        stmt.close();
        conn.close();
    }
}
