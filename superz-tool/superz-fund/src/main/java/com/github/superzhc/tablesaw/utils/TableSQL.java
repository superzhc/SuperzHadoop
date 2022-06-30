package com.github.superzhc.tablesaw.utils;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.fund.data.fund.FundData;
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
 * @create 2022/6/29 18:27
 **/
public class TableSQL {

    private JdbcHelper jdbc;

    public TableSQL(JdbcHelper jdbc) {
        this.jdbc = jdbc;
    }

    public void save(Table table) {
        // 判断 id 列是否存在，默认不存在
        boolean hasIdColumn = false;

        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = table.columnCount(); i < len; i++) {
            Column column = table.column(i);
            String columnName = column.name();
            if ("id".equalsIgnoreCase(columnName)) {
                hasIdColumn = true;
            }

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
//                if (l < 255) {
//                    SQLType = "VARCHAR(255)";
//                } else {
                SQLType = "VARCHAR(" + new Double(maxLength * 1.5).longValue() + ")";
//                }
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

            sb.append("`").append(columnName).append("`").append(" ").append(SQLType).append(",");
        }

        String tableName = (null == table.name() || table.name().trim().length() == 0 ? "t_" + UUID.randomUUID().toString().replace('-', '_') : table.name());
        String ddlSQL = "CREATE TABLE IF NOT EXISTS " +
                tableName +
                "(" +
                (hasIdColumn ? "" : "id INT AUTO_INCREMENT,") +
                sb +
                "PRIMARY KEY (id)" +
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

        jdbc.batchUpdate(tableName, table.columnNames(), data, 1000);
    }

    public static void main(String[] args) {
        String symbol = "160119";

        Table table = FundData.history(symbol);

        System.out.println(table.print(200));
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());

//        Table t2=FundData.realTime(symbol);
//        System.out.println(t2.printAll());
//        System.out.println(t2.structure().printAll());


    }
}
