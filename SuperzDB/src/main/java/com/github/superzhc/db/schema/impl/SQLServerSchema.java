package com.github.superzhc.db.schema.impl;

import com.github.superzhc.db.schema.Schema;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2021/8/4 16:25
 */
public class SQLServerSchema extends Schema implements Closeable {
    private static final String driver = "";
    private String url;
    private String username;
    private String password;
    private String database;
    private String tablePattern = null;
    private Connection conn = null;

    public SQLServerSchema(String url, String username, String password, String database) {
        this(url, username, password, database, null);
    }

    public SQLServerSchema(String url, String username, String password, String database, String tablePattern) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.database = database;
        this.tablePattern = tablePattern;
    }

    private Connection getConn() {
        if (null == conn) {
            try {
                DbUtils.loadDriver(driver);
                conn = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                new RuntimeException(ex);
            }
        }
        return conn;
    }

    @Override
    protected List<String> tables() {
        String sql = "SELECT name FROM sysobjects WHERE type = 'U' AND sysstat = '83'";

        try {
            QueryRunner query = new QueryRunner();
            List<String> tableNames = query.query(getConn(), sql, new ColumnListHandler<String>("table_name"));
            return tableNames;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected String describe(String table) {
//        String sql = "SELECT table_comment FROM information_schema.TABLES WHERE table_schema =? AND table_name =?";
//        try {
//            QueryRunner query = new QueryRunner();
//            Object[] obj = query.query(conn, sql, new ArrayHandler(), database, table);
//            return null == obj ? null : String.valueOf(obj[0]);
//        } catch (Exception ex) {
//            throw new RuntimeException(ex);
//        }
        return null;
    }

    @Override
    protected LinkedHashMap<String, String> columnHeaders() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("COLUMN_NAME", "列名");
        map.put("COLUMN_TYPE", "字段类型");
        map.put("IS_NULLABLE", "是否为空");
        map.put("COLUMN_KEY", "键");
        map.put("COLUMN_DEFAULT", "默认值");
        map.put("COLUMN_COMMENT", "备注");
        return map;
    }

    @Override
    protected List<Map<String, Object>> columns(String table) {
//        String sql = "SELECT COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_KEY,COLUMN_DEFAULT,COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema = ? AND table_name = ?";
//        try {
//            QueryRunner query = new QueryRunner();
//            List<Map<String, Object>> lst = query.query(conn, sql, new MapListHandler(), database, table);
//            return lst;
//        } catch (Exception ex) {
//            throw new RuntimeException(ex);
//        }
        return null;
    }

    @SneakyThrows
    @Override
    public void close() throws IOException {
        if (null != conn) {
            DbUtils.close(conn);
        }
    }
}
