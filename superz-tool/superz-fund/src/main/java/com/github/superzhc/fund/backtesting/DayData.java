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
 * @create 2022/4/25 14:58
 **/
public class DayData {
    private static final Logger log = LoggerFactory.getLogger(DayData.class);

    public Table create1Y() {
        return create(365);
    }

    public Table create(Integer days) {
        LocalDate end = LocalDate.now();
        log.debug("Last Day:{}", end);
        LocalDate start = end.minusDays(days);
        log.debug("Start Day:{}", start);

        DateColumn dates = DateColumn.create("date");
        while (start.isBefore(end)) {
            dates.append(start);
            start = start.plusDays(1);
        }

        Table table = Table.create("buy dates", dates);
        return table;
    }

    public static void main(String[] args) {
        String code = "160119";
        Table table = new DayData().create1Y();
        TableUtils.addConstantColumn(table, "code", code);
        TableUtils.addConstantColumn(table, "amount", 50.0);

        PurchaseAnalysis analysis = new PurchaseAnalysis(table);
        System.out.println(analysis.getRecords().print());
        Table purchaseSummarize = analysis.summarize();
        System.out.println(purchaseSummarize.print());
    }
}
