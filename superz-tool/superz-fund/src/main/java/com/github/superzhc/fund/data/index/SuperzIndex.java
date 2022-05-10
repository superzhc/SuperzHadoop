package com.github.superzhc.fund.data.index;

import tech.tablesaw.api.Table;

/**
 * @author superz
 * @create 2022/5/10 9:14
 **/
public class SuperzIndex {
    public static Table indices() {
        return CSIndex.indices();
    }

    public static Table index(String symbol) {
        return CSIndex.index(symbol);
    }

    public static Table mainStocks(String symbol) {
        return CSIndex.mainStocks(symbol);
    }

    public static Table stocks(String symbol) {
        return CSIndex.stocks(symbol);
    }

    public static Table industry(String symbol) {
        return CSIndex.industry(symbol);
    }

    public static Table history(String symbol) {
        return SinaIndex.history(symbol);
    }
}
