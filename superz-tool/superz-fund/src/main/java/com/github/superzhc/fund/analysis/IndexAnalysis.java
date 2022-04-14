package com.github.superzhc.fund.analysis;

import com.github.superzhc.fund.akshare.CSIndex;
import tech.tablesaw.aggregate.NumericAggregateFunction;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.Table;

import static tech.tablesaw.aggregate.AggregateFunctions.*;

/**
 * @author superz
 * @create 2022/4/8 10:36
 **/
public class IndexAnalysis {
    public static double pe(double marketValue, double netProfit) {
        return marketValue / netProfit;
    }

    public static double pb(double marketValue, double netWorth) {
        return marketValue / netWorth;
    }

    public static double peg(double pe, double netProfitGrowth) {
        return pe / netProfitGrowth;
    }

    public static void main(String[] args) {
        Table table = CSIndex.indexHistory("000905");
        System.out.println(table.print());
        Table t2 = table.summarize("收盘", "涨跌幅",
                count,//不为空的个数
                max,
                min,
                mean,//平均
                range,//范围
                stdDev,//标准差
                median,//中位数
                variance,//方差
                geometricMean,//几何平均数
                percentile90,// 百分位，下同，只是百分位的比例不同，注意，该数值是从小到大进行排序的
                // 15%的百分位
                new NumericAggregateFunction("Percentile15") {
                    @Override
                    public Double summarize(NumericColumn<?> column) {
                        return percentile(column, 15.0);
                    }
                },
                // 25%的百分位
                new NumericAggregateFunction("Percentile25") {
                    @Override
                    public Double summarize(NumericColumn<?> column) {
                        return percentile(column, 25.0);
                    }
                },
                populationVariance,//总体方差
                product,//乘积
                sumOfLogs,//对数求和
                sumOfSquares//平方和
        ).apply();
        System.out.println(t2.printAll());
    }
}
