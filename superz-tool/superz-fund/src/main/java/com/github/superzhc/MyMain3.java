package com.github.superzhc;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.JsonUtils;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.financial.IndexDao;
import com.github.superzhc.fund.akshare.JiuCaiShuo;
import com.github.superzhc.fund.data.fund.DoctorXiongFund;
import com.github.superzhc.fund.data.fund.EastMoneyFund;
import com.github.superzhc.fund.data.fund.FundData;
import com.github.superzhc.fund.data.index.*;
import com.github.superzhc.fund.strategy.DemoStrategy;
import com.github.superzhc.fund.strategy.Strategy;
import com.github.superzhc.indicator.InvestCalculator;
import com.github.superzhc.market.data.EastMoneyMarket;
import com.github.superzhc.tablesaw.utils.MyAggregateFunctions;
import tech.tablesaw.api.*;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.TimeSeriesPlot;
import tech.tablesaw.plotly.components.Axis;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.traces.ScatterTrace;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.github.superzhc.common.HttpConstant.UA;
import static tech.tablesaw.aggregate.AggregateFunctions.count;

/**
 * @author superz
 * @create 2022/5/13 9:03
 **/
public class MyMain3 {
    public static void main(String[] args) {
        String symbol = "000905.SH";
        String fundSymbol = "160119";
        Table table = Table.create();

//        table = IndexData.realTime(symbol);

//        table = FundData.realTime(fundSymbol);

//        table = FundData.fund(fundSymbol);
//
//        String str = table.row(0).getString("INDEX_CODE");

        table = FundData.history(fundSymbol);
        table = table.where(table.dateColumn("date").isAfter(LocalDate.now().minusMonths(6))).select("date","net_worth");

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        double holdCost = 1.8321883910858774;
        double holdShare = 4748.42;
//        table.addColumns(table.doubleColumn("net_worth").map(d->{
//            if(d>holdCost){
//                return null;
//            }
//
////            if()
//        }));


        for (int i = 0, size = table.rowCount(); i < size; i++) {
            Row row = table.row(i);
            LocalDate curDate = row.getDate("date");
            double curCost = row.getDouble("net_worth");
            if (curCost >= holdCost) {
                continue;
            }

            if ((curCost - holdCost) / holdCost < -0.1) {
                double invest = InvestCalculator.investByChange(holdCost, holdShare, curCost, -0.1);
                System.out.printf("%s的净值为%.4f，持仓成本下降1%%需要买入%.4f\n", curDate.format(df), curCost, invest);
            }
            double cost = InvestCalculator.costByInvest(holdCost, holdShare, curCost, 1000);
            System.out.printf("%s的净值为%.4f，买入1000会让持仓成本降为%.4f\n", curDate.format(df), curCost, cost);
        }

        System.out.println(table.print(100));
        System.out.println(table.shape());
        System.out.println(table.structure().print());
    }
}
