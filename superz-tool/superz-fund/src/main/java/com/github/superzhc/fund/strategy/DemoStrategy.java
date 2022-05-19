package com.github.superzhc.fund.strategy;

import com.github.superzhc.common.DateUtils;
import com.github.superzhc.fund.akshare.JiuCaiShuo;
import com.github.superzhc.fund.index.IndexTool;
import com.github.superzhc.tablesaw.utils.MyAggregateFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;

import java.time.LocalDate;

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
        Table records=Table.create();

        Table table = JiuCaiShuo.pe("000905.SH");


    }
}
