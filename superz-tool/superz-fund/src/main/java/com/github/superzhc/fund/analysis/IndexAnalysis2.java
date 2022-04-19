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
public class IndexAnalysis2 {
    public static double ep(double netProfit, double marketValue) {
        return netProfit / marketValue;
    }
}
