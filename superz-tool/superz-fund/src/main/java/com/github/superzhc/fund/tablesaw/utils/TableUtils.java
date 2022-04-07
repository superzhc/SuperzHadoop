package com.github.superzhc.fund.tablesaw.utils;

import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.TableBuildingUtils;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/4/7 9:07
 **/
public class TableUtils {
    public static class FundColumnType implements Function<String, Optional<ColumnType>> {

        @Override
        public Optional<ColumnType> apply(String s) {
            ColumnType ct = null;
            switch (s) {
                case "code":
                case "fund_code":
                case "fundCode":
                case "fund.code":
                case "代码":
                    ct = ColumnType.STRING;
                    break;
            }
            return Optional.ofNullable(ct);
        }
    }

    public static Table build(List<String> columnNames, List<String[]> dataRows) {
        return TableBuildingUtils.build(columnNames, dataRows, ReadOptionsUtils.columnTypeByFunction(new FundColumnType()));
    }

    public static Table timestamp2Date(Table table, String columnName) {
        DateColumn dc = table.longColumn(columnName).asDateTimes(ZoneOffset.ofHours(+8)).date().setName(columnName);
        return table.replaceColumn(columnName, dc);
    }

    public static Table rename(Table table, String columnName, String newColumnName) {
        table.column(columnName).setName(newColumnName);
        return table;
    }
}
