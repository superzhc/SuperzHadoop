package com.github.superzhc.fund.backtesting;

import com.github.superzhc.fund.strategy.PurchaseAnalysis;
import com.github.superzhc.fund.tablesaw.utils.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import java.time.LocalDate;

/**
 * @author superz
 * @create 2022/4/25 13:39
 **/
public class WeekData {
    private static final Logger log = LoggerFactory.getLogger(WeekData.class);

    private Integer dayOfWeek;

    public WeekData(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    private LocalDate date() {
        LocalDate now = LocalDate.now();
        if (now.getDayOfWeek().getValue() == dayOfWeek) {
            return now;
        } else {
            int x = now.getDayOfWeek().getValue() - dayOfWeek;
            if (x < 0) {
                return now.minusDays(x);
            } else {
                return now.plusDays(7 - x);
            }
        }
    }

    public Table create1Y() {
        return create(52);
    }

    public Table create(Integer number) {
        LocalDate end = date();
        log.debug("Last Day:{}", end);
        LocalDate start = end.minusDays(7 * number);
        log.debug("Start Day:{}", start);

        DateColumn dates = DateColumn.create("date");
        while (start.isBefore(end)) {
            dates.append(start);
            start = start.plusWeeks(1);
        }

        Table table = Table.create("buy dates", dates);
        return table;
    }

    public static void main(String[] args) {
        String code = "160119";
        Table table = new WeekData(1).create(52);
        TableUtils.addConstantColumn(table, "code", code);
        TableUtils.addConstantColumn(table, "amount", 200.0);

        PurchaseAnalysis analysis = new PurchaseAnalysis(table);
        System.out.println(analysis.getRecords().print());
        Table purchaseSummarize = analysis.summarize();
        System.out.println(purchaseSummarize.print());
        Table t=purchaseSummarize.where(purchaseSummarize.stringColumn("code").isEqualTo(code));

        Row row=t.row(0);

        double increase=(0.85*row.getDouble("total_input")-row.getDouble("total_value"))/0.15;
        System.out.println(increase);
    }
}
