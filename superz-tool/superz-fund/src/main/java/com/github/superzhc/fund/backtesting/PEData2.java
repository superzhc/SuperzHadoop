package com.github.superzhc.fund.backtesting;

import com.github.superzhc.fund.akshare.ENiu;
import com.github.superzhc.fund.strategy.PurchaseAnalysis;
import com.github.superzhc.fund.tablesaw.utils.MyAggregateFunctions;
import com.github.superzhc.fund.tablesaw.utils.TableUtils;
import tech.tablesaw.aggregate.AggregateFunctions;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import java.time.LocalDate;

/**
 * @author superz
 * @create 2022/4/26 23:13
 */
public class PEData2 {
    public static void main(String[] args) {
        String indexCode = "000905.SH";

        Table table = ENiu.indexHistory10Y(indexCode);

        // 开始投资时间
        int start = table.where(table.dateColumn("date").isBefore(LocalDate.parse("2021-04-26"))).rowCount();

        Table records = Table.create(
                DateColumn.create("date"),
                DoubleColumn.create("amount")
        );

        int count = table.rowCount();
        for (int i = start; i < count; i++) {
            Table subTable = table.first(i);

            Row last = table.row(i);

            Double position = MyAggregateFunctions.position(subTable.doubleColumn("pe"), last.getDouble("pe"));

            if (position < 0.064) {
                records.dateColumn("date").append(last.getDate("date"));
                records.doubleColumn("amount").append(200.0);
            } else if (position > 0.10) {
                if (records.rowCount() > 0) {
                    double amount = AggregateFunctions.sum.summarize(records.doubleColumn("amount"));
                    if (amount > 400) {
                        records.dateColumn("date").append(last.getDate("date"));
                        records.doubleColumn("amount").append(-1 * amount / 2.0);
                    }
                }
            }
        }

        TableUtils.addConstantColumn(records, "code", "160119");

        PurchaseAnalysis analysis = new PurchaseAnalysis(records);
        System.out.println(analysis.getRecords().printAll());
        System.out.println(analysis.summarize().printAll());
    }
}
