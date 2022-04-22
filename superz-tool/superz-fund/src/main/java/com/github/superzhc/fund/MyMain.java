package com.github.superzhc.fund;

import com.github.superzhc.fund.akshare.EastMoney;
import tech.tablesaw.api.Table;

/**
 * @author superz
 * @create 2022/4/22 13:58
 **/
public class MyMain {
    public static void main(String[] args) {
        String symbol="000942";

        Table table=Table.create();

        table= EastMoney.fundIndustryComponent(symbol);//EastMoney.fundRealNet(symbol,"501009");

        System.out.println(table.printAll());
        System.out.println(table.structure().printAll());
    }
}
