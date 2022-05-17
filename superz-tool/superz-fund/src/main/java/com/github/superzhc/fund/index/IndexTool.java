package com.github.superzhc.fund.index;

import org.decampo.xirr.Transaction;
import org.decampo.xirr.Xirr;
import tech.tablesaw.api.*;

import java.time.LocalDate;

/**
 * @author superz
 * @create 2022/4/21 14:54
 **/
public class IndexTool {
    // 均摊
    public static Double shareEqually(double netWorth, double share, double increase, double currentNetWorth) {
        double x = (increase * netWorth * share) / (1 - netWorth * (1 + increase) / currentNetWorth);
        return x;
    }

    public static void shengouCal(Double sg, Double sgf) {
        // 净金额
        Double jsg = sg / (1 + sgf * 0.01);
    }

    // 年化收益率
    public static double syl(Table table, String dateColumn, String numberColumn) {
        return syl(table.dateColumn(dateColumn), table.numberColumn(numberColumn));
    }

    public static double syl(DateColumn dates, NumericColumn<?> numbers) {
        if (dates.size() != numbers.size()) {
            throw new IllegalArgumentException("DateColumn 与 NumbericColumn 数据量不同");
        }

        int size = dates.size();
        Transaction[] tx = new Transaction[size];
        for (int i = 0; i < size; i++) {
            tx[i] = new Transaction(numbers.getDouble(i), dates.get(i));
        }
        Xirr instance = new Xirr(tx);
        double rate = instance.xirr();
        return rate;
    }

    public static void main(String[] args) {
        DateColumn dateColumn = DateColumn.create("date",
                LocalDate.parse("2021-08-06"),
                LocalDate.parse("2022-02-08"),
                LocalDate.parse("2022-02-10"),
                LocalDate.parse("2022-03-04"),
                LocalDate.parse("2022-04-22") // 当前时间
        );
        DoubleColumn intColumn = DoubleColumn.create("m",
                -5000,
                -1000,
                -500,
                -500,
                5645.53 // 当前持有金额
        );

        double rate = syl(dateColumn, intColumn);
        System.out.println(rate * 100);
    }
}
