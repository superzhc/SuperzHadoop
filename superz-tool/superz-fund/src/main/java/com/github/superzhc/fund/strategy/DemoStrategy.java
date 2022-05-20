package com.github.superzhc.fund.strategy;

import com.github.superzhc.common.DateUtils;
import com.github.superzhc.fund.akshare.JiuCaiShuo;
import com.github.superzhc.tablesaw.functions.DoubleFunctions;
import com.github.superzhc.tablesaw.utils.MyAggregateFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.*;

import java.time.LocalDate;

import static tech.tablesaw.aggregate.AggregateFunctions.count;

/**
 * @author superz
 * @create 2022/5/18 20:45
 **/
public class DemoStrategy {
    private static final Logger log = LoggerFactory.getLogger(DemoStrategy.class);

    private Table valuation;

    public DemoStrategy(Table valuation) {
        this.valuation = valuation;
    }


    public Strategy.Operate suggest(LocalDate date) {
        if (DateUtils.isOffDay(date)) {
            return Strategy.Operate.Hold;
        }

        // 当天在交易时间内进行评估的时候，一般无法获取当天的估值，因此使用昨天的数值作为评估点
        LocalDate day = date.minusDays(1);
        // 几年前的时间节点
        LocalDate dayNYearsAgo = date.minusYears(6);

        // 昨天之前的所有估值数据
        Table table = valuation.where(valuation.dateColumn("date").isBetweenIncluding(dayNYearsAgo, day));
        System.out.println(table.print());

        // 昨天的估值
        double pe = valuation.where(valuation.dateColumn("date").isEqualTo(day)).first(1).doubleColumn("pe").get(0);

        // 昨天的估值在
        double position = MyAggregateFunctions.position(table.doubleColumn("pe"), pe);
        log.debug("date:{}={}%", day, position * 100);


        return Strategy.Operate.Hold;
    }

    public static void main(String[] args) {
        String symbol = "000905.SH";

        Table records = Table.create(DateColumn.create("date"), StringColumn.create("operate"), DoubleColumn.create("value"));
        Table table = JiuCaiShuo.pe(symbol);

        int yearPeriod = 5;
        String valueField = "pe";
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = LocalDate.now();
        while (start.isBefore(end)) {
            if (DateUtils.isNonTradingDay(start)) {
                log.debug("data:{}非交易日", start);
                start = start.plusDays(1);
                continue;
            }
            LocalDate valuationDate = start.minusDays(1);
            while (DateUtils.isNonTradingDay(valuationDate)) {
                valuationDate = valuationDate.minusDays(1);
            }
            LocalDate earliest = valuationDate.minusYears(yearPeriod);

            // yearPeriod 周期内的数据
            Table childTable = table.where(table.dateColumn("date").isBetweenExcluding(earliest, valuationDate));
            // 估值日期的数据
            Table valuationDateT = table.where(table.dateColumn("date").isEqualTo(valuationDate));
            if (valuationDateT.rowCount() == 0) {
                log.error("date:{}，估值日期【{}】数据缺省", start, valuationDate);
                start = start.plusDays(1);
                continue;
            }

            Row row = valuationDateT.row(0);
            double position = DoubleFunctions.position(childTable.doubleColumn(valueField), row.getDouble(valueField));
            double positionPercentage = position * 100;
            log.debug("date:{}，估值日期:{}={}%", start, valuationDate, positionPercentage);

            if (positionPercentage < 20) {
                records.dateColumn("date").append(start);
                records.stringColumn("operate").append("I");
                records.doubleColumn("value").append(position);
            } else if (20 <= positionPercentage && positionPercentage < 50) {
                // 无操作
            } else if (50 <= positionPercentage && positionPercentage < 70) {
                records.dateColumn("date").append(start);
                records.stringColumn("operate").append("R");
                records.doubleColumn("value").append(position);
            } else if (70 <= positionPercentage) {
                records.dateColumn("date").append(start);
                records.stringColumn("operate").append("C");
                records.doubleColumn("value").append(position);
            }

            start = start.plusDays(1);
        }

        System.out.println(records.print());
        System.out.println(records.shape());
        System.out.println(records.summarize("date", count).by("operate"));
    }


}
