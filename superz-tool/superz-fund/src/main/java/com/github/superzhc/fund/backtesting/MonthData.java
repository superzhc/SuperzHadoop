package com.github.superzhc.fund.backtesting;

import com.github.superzhc.fund.strategy.PurchaseAnalysis;
import com.github.superzhc.fund.tablesaw.utils.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.Table;

import java.time.LocalDate;

/**
 * @author superz
 * @create 2022/4/25 15:04
 **/
public class MonthData {
    private static final Logger log = LoggerFactory.getLogger(MonthData.class);

    private Integer dayOfMonth;

    public MonthData(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    private LocalDate date() {
        LocalDate now = LocalDate.now();
        if (dayOfMonth < now.getDayOfMonth()) {
            return LocalDate.of(now.getYear(), now.getMonthValue() + 1, dayOfMonth);
        } else {
            return LocalDate.of(now.getYear(), now.getMonthValue(), dayOfMonth);
        }
    }

    public Table create(Integer number) {
        LocalDate end = date();
        log.debug("Last Day:{}", end);
        LocalDate start = end.minusMonths(number);
        log.debug("Start Day:{}", start);

        DateColumn dates = DateColumn.create("date");
        while (start.isBefore(end)) {
            dates.append(start);
            start = start.plusMonths(1);
        }

        Table table = Table.create("buy dates", dates);
        return table;
    }

    public static void main(String[] args) {
        String code = "160119";
        for (int i = 1; i <= 28; i++) {
            Table table = new MonthData(i).create(12);
            TableUtils.addConstantColumn(table, "code", code);
            TableUtils.addConstantColumn(table, "amount", 1000.0);

            PurchaseAnalysis analysis = new PurchaseAnalysis(table);
            System.out.println(analysis.getRecords().print());
            Table purchaseSummarize = analysis.summarize();
            System.out.println(purchaseSummarize.print());
        }
    }
}
