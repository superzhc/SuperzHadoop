package com.github.superzhc.db.transform;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author superz
 * @create 2021/8/4 19:16
 */
public class Txt2SQL {
    public static void main(String[] args) throws Exception {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/superz?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "123456";
        DbUtils.loadDriver(driver);
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("connectionTimeout", "1000"); // 连接超时：1秒
        config.addDataSourceProperty("idleTimeout", "60000"); // 空闲超时：60秒
        config.addDataSourceProperty("maximumPoolSize", "10"); // 最大连接数：10
        DataSource ds = new HikariDataSource(config);
        QueryRunner runner = new QueryRunner(ds);
        String sql = "INSERT INTO JD(username,account,password,email,note,mobile,telephone) values(?,?,?,?,?,?,?)";

        String path = "C:\\Users\\superz\\Downloads\\Telegram Desktop\\jd com.txt";
        // FileReader fr = new FileReader(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-16"));

        String line = null;
        int total = 0;
        int success = 0;
        int fail = 0;
        while ((line = reader.readLine()) != null) {
            String[] ss = line.split("---");
            String[] params = new String[7];
            int cursor = 0;
            for (String s : ss) {
                if (cursor == 7) {
                    params[6] = (null == params[6] ? s : params[6] + "@" + s);
                } else {
                    params[cursor++] = "\\N".equals(s) ? null : s;
                }
            }
            int i = runner.update(sql, params);

            total++;
            if (i > 0) {
                success++;
            } else {
                fail++;
            }
            if (total % 100 == 0) {
                System.out.println();
            }
            System.out.print(".");
        }
        System.out.println("总数据：" + total);
        System.out.println("成功：" + success);
        System.out.println("失败：" + fail);
    }
}
