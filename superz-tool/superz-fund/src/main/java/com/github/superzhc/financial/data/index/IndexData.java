package com.github.superzhc.financial.data.index;

import com.github.superzhc.tablesaw.functions.DoubleFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;

import static com.github.superzhc.financial.utils.FundConstant.*;

/**
 * @author superz
 * @create 2022/5/19 23:37
 */
public class IndexData {
    private static final Logger log = LoggerFactory.getLogger(IndexData.class);

    public static Table trackIndex(String symbol) {
        Table table, t1 = null;
        try {
            t1 = CSIndex.trackIndex(symbol);
            log.debug("source [CSINDEX] size : {}", t1.rowCount());
        } catch (Exception e) {
            log.error("fetch [csindex] fail !", e);
        }
        Table t2 = EastMoneyIndex.tranceIndex(symbol);
        log.debug("source [EM] size : {}", t2.rowCount());
        if (null == t1) {
            table = t2;
        } else {
            table = t1.joinOn(FUND_CODE).fullOuter(true, t2);
        }
        String[] columnNames = new String[]{
                FUND_TRACKING_ERROR, FUND_FEE
                , FUND_YIELD_LAST_WEEK
                , FUND_YIELD_LAST_MONTH, FUND_YIELD_LAST_THREE_MONTH, FUND_YIELD_LAST_SIX_MONTH
                , FUND_YIELD_THIS_YEAR, FUND_YIELD_LAST_YEAR, FUND_YIELD_LAST_THREE_YEAR
                , FUND_YIELD_ALL
        };
        for (String columnName : columnNames) {
            table.replaceColumn(columnName, DoubleFunctions.percentage(table.stringColumn(columnName)).setName(columnName));
        }
        table = table.sortAscendingOn(columnNames);
        return table;
    }

    public static Table indices(String str) {
        Table table = CSIndex.indices(str);
        return table;
    }

    public static Table index(String symbol) {
        Table table = CSIndex.index(symbol);
        return table;
    }

    public static Table history(String symbol) {
        Table table = EastMoneyIndex.dailyHistory(symbol);
        return table;
    }

    public static Table realTime(String symbol) {
        Table table = XueQiuIndex.realTime(symbol);
        return table;
    }

    public static Table industry(String symbol) {
        Table table = CSIndex.industry(symbol);
        return table;
    }

    public static Table stocks(String symbol) {
        Table table = CSIndex.stocksWeight(symbol);
        return table;
    }

    public static Table pe(String symbol) {
        Table table = JiuCaiShuoIndex.pe(symbol);
        return table;
    }

    public static Table pb(String symbol) {
        Table table = JiuCaiShuoIndex.pb(symbol);
        return table;
    }

    public static void main(String[] args) {
        String symbol = "000905.SH";

        Table table = Table.create();

        table = trackIndex(symbol);

        System.out.println(table.print(100));
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
    }
}
