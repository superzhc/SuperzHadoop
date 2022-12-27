package com.github.superzhc.hadoop.presto;

import com.github.superzhc.common.jdbc.JdbcHelper;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;

/**
 * @author superz
 * @create 2022/4/22 14:34
 **/
public class PrestoMain {
    public static void main(String[] args) {
        /**
         * Presto 支持以下 JDBC 的 URL 格式：
         * jdbc:presto://host:port
         * jdbc:presto://host:port/catalog
         * jdbc:presto://host:port/catalog/schema
         */
        String url = "jdbc:presto://log-platform01:8099/hive/default";

//        Connection conn = null;
//        Statement stmt = null;
//        try {
//            /* 异常一：Connection property 'user' is required */
//            /* 即 user 不能为 null，直接默认配置成 root */
//            conn = DriverManager.getConnection(url, "root", null);
//            stmt = conn.createStatement();
//
//            ResultSet rs = stmt.executeQuery("select create_time,title,tag from any_knew_hot_news");
//            while (rs.next()) {
//                long time = rs.getLong(1);
//                String method = rs.getString(2);
//                String path = rs.getString(3);
//                System.out.println(String.format("time=%s, method=%s, path=%s", time, method, path));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (null != stmt)
//                    stmt.close();
//                if (null != conn)
//                    conn.close();
//            } catch (Exception e1) {
//            }
//        }

        try (JdbcHelper jdbc = new JdbcHelper(url, "root", null)) {
            jdbc.show("select * from hive.default.any_knew_hot_news");

            String sql = "select * from elasticsearch.default.\"service_instance_sla-20220423\" where time_bucket>202204231414 order by time_bucket asc limit 5";
            jdbc.show(sql);
        }
    }
}
