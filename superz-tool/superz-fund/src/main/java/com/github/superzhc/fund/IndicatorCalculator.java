package com.github.superzhc.fund;

import tech.tablesaw.aggregate.AggregateFunctions;
import tech.tablesaw.api.DoubleColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * 参考 https://github.com/Tsingchi-Chao/IndicatorCalculation/blob/main/Indicator.py
 *
 * @author superz
 * @create 2022/4/2 13:39
 **/
public class IndicatorCalculator {

    /**
     * The annual standard deviation.
     *
     * @param datas
     * @return
     */
    public static double annualChemicalFluctuationRate(DoubleColumn datas) {
        double dailyStd = datas.standardDeviation();
        double annualStd = dailyStd * Math.sqrt(250);
        return annualStd;
    }

    public static double annualIncomeRate(DoubleColumn datas) {
        double annualReturn = Math.pow(datas.add(1).geometricMean(), 250) - 1;
        return annualReturn;
    }

    public static double cumulativeIncomeRate(DoubleColumn datas) {
//        Double first = AggregateFunctions.first.summarize(datas) /*datas.get(0)*/;
//        Double last = AggregateFunctions.last.summarize(datas)/*datas.get(datas.size() - 1)*/;
//        return (last - first) / first;
        return AggregateFunctions.pctChange.summarize(datas);
    }

    /**
     * Sharpe ratio
     *
     * @param datas
     * @return
     */
    public static double sharpeRatio(DoubleColumn datas) {
        double annualReturn = annualIncomeRate(datas);
        double annualStd = annualChemicalFluctuationRate(datas);
        double sharpeRatio = annualReturn / annualStd;
        return sharpeRatio;
    }

    /**
     * Max drawdown of the financial series
     *
     * @param datas
     * @return
     */
    public static double maxDrawdown(DoubleColumn datas) {
        double rollMax = datas.cumSum().max();
        double maxDrawdown = -1 * datas.divide(rollMax).add(-1).min();
        return maxDrawdown;
    }

    public static double calmarRatio(DoubleColumn datas) {
        return annualIncomeRate(datas) / maxDrawdown(datas);
    }

    public static double annualDownsideStd(DoubleColumn datas) {
        DoubleColumn negativeDatas = datas.where(datas.isLessThan(0));
        int num = negativeDatas.size();
        double dailyDownsideStd = Math.sqrt(negativeDatas.power(2).sum() / num);
        double annualDownsideStd = dailyDownsideStd * Math.sqrt(250);
        return annualDownsideStd;
    }

    public static double sortinoRatio(DoubleColumn datas) {
        return annualIncomeRate(datas) / annualDownsideStd(datas);
    }

    public static double skewness(DoubleColumn datas) {
        return datas.skewness();
    }

    public static double kurtosis(DoubleColumn datas) {
        return datas.kurtosis();
    }

//    public static void averageTop5MaxDrawdown(DoubleColumn datas) {
//        List<Double> drawdownList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            double rollMax = datas.cumSum().max();
//            double drawdown = -1 * datas.divide(rollMax).add(-1).min();
//            if (drawdown <= 0) {
//                break;
//            }
//            drawdownList.add(drawdown);
//
//            //datas.divide(rollMax).add(-1).indexOf(drawdown);
//        }
//    }

    public static void main(String[] args) {
        DoubleColumn datas = DoubleColumn.create("data", new Integer[]{0, -3, 2, null, 2});
//        System.out.println(maxDrawdown(datas));
//        DoubleColumn dd = datas.cumSum();
//        System.out.println(dd.print());
//        int num=datas.where(datas.isLessThan(0)).size();
//        System.out.println(num);
    }
}
