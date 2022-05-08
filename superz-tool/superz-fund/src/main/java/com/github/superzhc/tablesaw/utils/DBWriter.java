package com.github.superzhc.tablesaw.utils;

import com.github.superzhc.common.jdbc.JdbcHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.*;
import tech.tablesaw.columns.Column;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * @author superz
 * @create 2022/4/13 14:57
 **/
public class DBWriter {
    private static final Logger log = LoggerFactory.getLogger(DBWriter.class);

    private String url;
    private String username = null;
    private String password = null;

    public DBWriter(String url) {
        this(url, null, null);
    }

    public DBWriter(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void db(Table table) {
        db(table, null);
    }

    public void db(Table table, String tableName) {
        try (JdbcHelper jdbc = new JdbcHelper(url, username, password)) {
            tableName = null == tableName
                    ? (
                    null == table.name()
                            ? LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "_" + UUID.randomUUID().toString()
                            : table.name())
                    : tableName;

            // 表不存在的情况，需先创建表再进行操作
            if (!jdbc.exist(tableName)) {
                StringBuilder ddlSb = new StringBuilder();
                ddlSb.append("CREATE TABLE ").append(tableName).append('(');

                List<Column<?>> columns = table.columns();
                for (int i = 0, len = columns.size(); i < len; i++) {
                    Column<?> column = columns.get(i);

                    /* 见：tech.tablesaw.io.jdbc.SqlResultSetReader */
                    String dbType;
                    if (column instanceof BooleanColumn) {
                        dbType = "BOOLEAN";
                    } else if (column instanceof ShortColumn) {
                        dbType = "SMALLINT";
                    } else if (column instanceof IntColumn) {
                        dbType = "INTEGER";
                    } else if (column instanceof LongColumn) {
                        dbType = "BIGINT";
                    } else if (column instanceof FloatColumn || column instanceof DoubleColumn
                    ) {
                        dbType = "DOUBLE";
                    }
//                    else if (column instanceof NumericColumn) {
//                        dbType = "DECIMAL(18,2)";
//                    }
                    else if (column instanceof DateColumn) {
                        dbType = "DATE";
                    } else if (column instanceof TimeColumn) {
                        dbType = "TIME";
                    } else if (column instanceof InstantColumn) {
                        dbType = "TIMESTAMP";
                    } else if (column instanceof StringColumn) {
                        // 以作为倍数，设置两倍的长度基本可以保证不会出问题
                        int length = (((StringColumn) column).columnWidth() / 64 + 1) * 64 * 2 - 1;
                        if (length > 65535) {
                            dbType = "TEXT";
                        } else {
                            dbType = "VARCHAR(" + length + ")";
                        }
                    } else if (column instanceof TextColumn) {
                        dbType = "TEXT";
                    } else {
                        dbType = "VARCHAR(255)";
                    }

                    if (i > 0) {
                        ddlSb.append(',');
                    }
                    ddlSb.append(column.name()).append(' ').append(dbType).append(' ').append("null");
                }

                ddlSb.append(')');
                jdbc.ddlExecute(ddlSb.toString());
            }

            List<String> columnNames = table.columnNames();

            Object[][] datas = new Object[table.rowCount()][];
            for (int j = 0, size = table.rowCount(); j < size; j++) {
                Row row = table.row(j);
                Object[] data = new Object[row.columnCount()];
                for (int k = 0, len = row.columnCount(); k < len; k++) {
                    //String columnName=columnNames.get(k);
                    data[k] = row.getObject(k);
                }
                datas[j] = data;
            }

            jdbc.batchUpdate(tableName, columnNames, datas, 1000);
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/news_dw?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";

        DBWriter writer=new DBWriter(url,username,password);
    }
}
