package com.github.superzhc.common.tablesaw.utils;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.common.utils.CamelCaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import tech.tablesaw.columns.booleans.BooleanColumnType;
import tech.tablesaw.columns.dates.DateColumnType;
import tech.tablesaw.columns.datetimes.DateTimeColumnType;
import tech.tablesaw.columns.instant.InstantColumnType;
import tech.tablesaw.columns.numbers.*;
import tech.tablesaw.columns.strings.StringColumnType;
import tech.tablesaw.columns.strings.TextColumnType;
import tech.tablesaw.columns.times.TimeColumnType;

import java.util.UUID;

/**
 * @author superz
 * @create 2022/7/1 10:31
 **/
public class TableWriterUtils {
    private static final Logger log = LoggerFactory.getLogger(TableWriterUtils.class);

    public static void db(String url, Table table) {
        db(url, table, null);
    }

    public static void db(String url, Table table, String tableName) {
        try (JdbcHelper jdbc = new JdbcHelper(url)) {
            db(jdbc, table, tableName);
        }
    }

    public static void db(String url, String username, String password, Table table) {
        db(url, username, password, table, null);
    }

    public static void db(String url, String username, String password, Table table, String tableName) {
        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            db(jdbc, table, tableName);
        }
    }

    public static void db(JdbcHelper jdbc, Table table) {
        db(jdbc, table, null);
    }

    public static void db(JdbcHelper jdbc, Table table, String tableName) {
        // 判断 id 列是否存在，默认不存在
        boolean hasIdColumn = false;

        String[] columnNames = new String[table.columnCount()];

        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = table.columnCount(); i < len; i++) {
            Column column = table.column(i);
            String columnName = column.name();

            if ("id".equalsIgnoreCase(columnName)) {
                hasIdColumn = true;
            }

            // 驼峰命名转换成下划线
            columnName = CamelCaseUtils.camelCase2underscore(columnName);

            // 做一层转义
            // MySQL:`columnName`;SQLServer:[columnName];
            columnName = String.format("`%s`", columnName);
            columnNames[i] = columnName;

            ColumnType columnType = column.type();
            String SQLType = "";
            if (columnType instanceof ShortColumnType) {
                SQLType = "SMALLINT";
            } else if (columnType instanceof IntColumnType) {
                SQLType = "INT";
            } else if (columnType instanceof LongColumnType) {
                SQLType = "BIGINT";
            } else if (columnType instanceof FloatColumnType) {
                SQLType = "FLOAT";
            } else if (columnType instanceof BooleanColumnType) {
                SQLType = "TINYINT";
            } else if (columnType instanceof StringColumnType) {
                Double maxLength = table.stringColumn(i).length().max();
                Long l = maxLength.longValue();
                if (l > 65535) {
                    SQLType = "TEXT";
                } else {
                    SQLType = "VARCHAR(" + new Double(maxLength * 1.5).longValue() + ")";
                }
            } else if (columnType instanceof DoubleColumnType) {
                SQLType = "DOUBLE";
            } else if (columnType instanceof DateColumnType) {
                SQLType = "DATE";
            } else if (columnType instanceof TimeColumnType) {
                SQLType = "TIME";
            } else if (columnType instanceof DateTimeColumnType) {
                SQLType = "DATETIME";
            } else if (columnType instanceof InstantColumnType) {
                SQLType = "TIMESTAMP";
            } else if (columnType instanceof TextColumnType) {
                SQLType = "TEXT";
            } else {
                SQLType = "VARCHAR(255)";
            }

            sb.append(columnName).append(" ").append(SQLType).append(",");
        }

        if (null == tableName || tableName.trim().length() == 0) {
            tableName = (null == table.name() || table.name().trim().length() == 0 ? "t_" + UUID.randomUUID().toString().replace('-', '_') : table.name());
        }

        String ddlSQL = "CREATE TABLE IF NOT EXISTS " +
                tableName +
                "(" +
                (hasIdColumn ? "" : "id INT AUTO_INCREMENT,") +
                sb.substring(0, sb.length() - 1) +
                (hasIdColumn ? "" : ",PRIMARY KEY (id)") +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8";
        jdbc.ddlExecute(ddlSQL);

        Object[][] data = new Object[table.rowCount()][];
        for (int i = 0, size = table.rowCount(); i < size; i++) {
            Row row = table.row(i);

            Object[] dataRow = new Object[table.columnCount()];
            for (int j = 0, csize = table.columnCount(); j < csize; j++) {
                dataRow[j] = row.getObject(j);
            }
            data[i] = dataRow;
        }

        jdbc.batchUpdate(tableName, columnNames, data, 1000);
    }

}
