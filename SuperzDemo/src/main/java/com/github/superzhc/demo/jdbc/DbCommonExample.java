package com.github.superzhc.demo.jdbc;

import com.github.superzhc.db.Driver;
import com.github.superzhc.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 2020年07月16日 superz add
 */
public class DbCommonExample
{
    private static final Logger logger = LoggerFactory.getLogger(DbCommonExample.class);

    private Connection connection;

    public DbCommonExample(String url) {
        try {
            Driver driver = Driver.match(url);
            Class.forName(driver.fullClassName());
            connection = DriverManager.getConnection(url);
        }
        catch (Exception e) {
        }
    }

    public static DbCommonExample example() {
        PropertiesUtils.read("./jdbc.properties");
        String url = PropertiesUtils.get("url");
        logger.debug("数据库连接信息：{}", url);
        return new DbCommonExample(url);
    }

    public Connection getConnection() {
        return connection;
    }
}
