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
    /**
     * 市盈率
     * <p>
     * 市盈率的定义是：公司市值/公司盈利（即 PE=P/E，其中 P 代表公司市值，E 代表公司盈利）
     * <p>
     * 市盈率细分为 静态市盈率、滚动市盈率和动态市盈率。
     * - 静态市盈率是取用公司上一个年度的净利润
     * - 滚动市盈率是取用最近 4 个季度财报的净利润
     * - 动态市盈率是取用预估的公司下一个年度的净利润
     * <p>
     * 市盈率适用的范围：流通性好、盈利稳定的品种
     *
     * @param marketValue
     * @param netProfit
     *
     * @return
     */
    public static double pe(double marketValue, double netProfit) {
        return marketValue / netProfit;
    }

    public static double ep(double netProfit, double marketValue) {
        return netProfit / marketValue;
    }

    /**
     * 市净率
     * <p>
     * 市净率指的是每股股价与每股净资产的比率。
     * <p>
     * 市净率的定义是：PB=P/B（其中 P 代表公司市值，B 代表公司净资产）
     *
     * @param marketValue
     * @param netWorth
     *
     * @return
     */
    public static double pb(double marketValue, double netWorth) {
        return marketValue / netWorth;
    }

    /**
     * 企业运作资产的效率：ROE
     */
    public static void roe() {
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
