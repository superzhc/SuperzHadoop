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

    public static Table index(String symbol) {
        Table table = EastMoneyIndex.index(symbol);
        return table;
    }

    public static Table history(String symbol) {
        Table table = EastMoneyIndex.dailyHistory(symbol);
        return table;
    }

    public static Table realTime(String symbol) {
        Table table = XueQiuIndex.realTime(symbol);
        return table;
    }

    public static Table industry(String symbol) {
        Table table = CSIndex.industry(symbol);
        return table;
    }

    public static Table stocks(String symbol) {
        Table table = CSIndex.stocksWeight(symbol);
        return table;
    }

    public static void main(String[] args) {
        String symbol = "000905.SH";

        Table table = Table.create();

        table = trackIndex(symbol);

        System.out.println(table.print(100));
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
    }
}
