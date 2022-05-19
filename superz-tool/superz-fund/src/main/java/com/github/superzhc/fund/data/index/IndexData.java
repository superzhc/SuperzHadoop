package com.github.superzhc.fund.data.index;

import com.github.superzhc.tablesaw.functions.DoubleFunctions;
import tech.tablesaw.api.Table;

/**
 * @author superz
 * @create 2022/5/19 23:37
 */
public class IndexData {
    public static Table trackIndex(String symbol) {
        Table table = EastMoneyIndex.tranceIndex(symbol);
        String[] columnNames = new String[]{"跟踪误差", "手续费"};
        for (String columnName : columnNames) {
            table.replaceColumn(columnName, DoubleFunctions.percentage(table.stringColumn(columnName)).setName(columnName));
        }
        table = table.sortAscendingOn(columnNames);
        return table;
    }

    public static void main(String[] args) {
        Table table = Table.create();

        table = trackIndex("000905.SH");

        System.out.println(table.print(100));
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
    }
}
