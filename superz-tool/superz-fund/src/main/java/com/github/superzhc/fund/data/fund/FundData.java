package com.github.superzhc.fund.data.fund;

import com.github.superzhc.financial.data.fund.EastMoneyFund;
import tech.tablesaw.api.Table;

/**
 * @author superz
 * @create 2022/5/19 23:37
 */
public class FundData {
    public static Table fund(String symbol) {
        Table table = EastMoneyFund.fundNewTable(symbol);
        return table;
    }

    public static Table history(String symbol) {
        Table table = EastMoneyFund.fundNetHistory(symbol);
        return table;
    }

    public static Table realTime(String symbol) {
        Table table = EastMoneyFund.fundRealNet(symbol);
        return table;
    }

//    public static Table fundStocks(String symbol){
//        Table table=EastMoneyFund.fundStocks(symbol);
//        return table;
//    }

//    public static Table industry(String symbol) {
//        Table table = EastMoneyFund.fundIndustryComponent(symbol);
//        return table;
//    }

    public static Table managers(String symbol) {
        Table table = EastMoneyFund.fundManager(symbol);
        return table;
    }

    public static Table scale(String symbol) {
        Table table = EastMoneyFund.fundScale(symbol);
        return table;
    }

    public static Table fenhong(String symbol) {
        Table table = EastMoneyFund.fundFenHong(symbol);
        return table;
    }

    public static Table rank(String symbol) {
        Table table = EastMoneyFund.fundPeriodRank(symbol);
        return table;
    }

    public static Table summarize(String symbol) {
        Table table = EastMoneyFund.fundSummarize2(symbol);
        return table;
    }
}
