package com.github.superzhc.fund.analysis;

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
}
