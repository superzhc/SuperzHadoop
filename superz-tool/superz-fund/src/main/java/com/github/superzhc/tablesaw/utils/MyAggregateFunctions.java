package com.github.superzhc.tablesaw.utils;

import com.github.superzhc.fund.data.index.DanJuanIndex;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.Table;

/**
 * @author superz
 * @create 2022/4/25 15:21
 **/
public class MyAggregateFunctions {
    public static DoubleColumn position(DoubleColumn column) {
        int size = column.size();
        DoubleColumn newColumn = DoubleColumn.create(column.name() + "[Position]", size);
        for (int i = 1; i < size; i++) {
            DoubleColumn subColumn = column.first(i);
            double last = column.getDouble(i);
            Double p = position(subColumn, last);
            newColumn.set(i, p);
        }
        return newColumn;
    }

    /**
     * 值在列中所处的百分位
     *
     * @param column
     * @param value
     * @return
     */
    public static Double position(NumericColumn<?> column, double value) {
        double[] values = removeMissing(column);

        if (null == values || values.length == 0) {
            return null;
        }

        int total = values.length;
        int lessCount = 0;

        for (int i = 0; i < total; i++) {
            if (value > values[i]) {
                lessCount++;
            }
        }

        return lessCount / (1.0 * total);
    }

    private static double[] removeMissing(NumericColumn<?> column) {
        NumericColumn<?> numericColumn = (NumericColumn<?>) column.removeMissing();
        return numericColumn.asDoubleArray();
    }

    public static void main(String[] args) {
        // String code = "160119";
        String indexCode = "000905.SH";
//        Table table = DanJuanFunds.peHistory3Y(indexCode);
//        DoubleColumn positionColumn = position(table.doubleColumn("pe"));

        Table table = DanJuanIndex.pbHistory10Y(indexCode);
        DoubleColumn positionColumn = position(table.doubleColumn("pb"));

        table.addColumns(positionColumn);
        System.out.println(table.printAll());
        System.out.println(table.structure().printAll());
    }
}