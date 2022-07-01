package com.github.superzhc.common.tablesaw.utils;

import com.github.superzhc.common.jdbc.JdbcHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/7/1 11:36
 **/
public class TableReaderUtils {
    private static final Logger log = LoggerFactory.getLogger(TableReaderUtils.class);

    public static Table dbByTableName(String url, String username, String password, String tableName) {
        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            return dbByTableName(jdbc, tableName);
        }
    }

    public static Table dbByTableName(JdbcHelper jdbc, String tableName) {
        String sql = String.format("SELECT * FROM %s", tableName);
        return db(jdbc, sql);
    }

    public static Table db(String url, String username, String password, String sql, Object... params) {
        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            return db(jdbc, sql, params);
        }
    }

    public static Table db(JdbcHelper jdbc, String sql, Object... params) {
        Table table = jdbc.dqlExecute(sql, params, new Function<ResultSet, Table>() {
            @Override
            public Table apply(ResultSet resultSet) {
                try {
                    return Table.read().db(resultSet);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return table;
    }
}
