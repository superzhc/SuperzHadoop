package com.github.superzhc;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.fund.data.fund.EastMoneyFund;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/5/12 9:41
 **/
public class MyMain2 {
    public static final String JDBC_URL = "jdbc:sqlite:E:\\superz_financial\\db\\account.db";

    public static void main(String[] args) {
        try (JdbcHelper jdbc = new JdbcHelper(JDBC_URL)) {
            Table table = TableUtils.db(jdbc, "select * from USER_FUND_RECORDS");

//            System.out.println(table.print());
//            System.out.println(table.shape());
//            System.out.println(table.structure().printAll());

            Table realNetTable=EastMoneyFund.fundRealNet(table.stringColumn("FUND_CODE").unique().asObjectArray());

            Table t=table.select("FUND_CODE","SHARE","INVEST").joinOn("FUND_CODE").inner(realNetTable.select("code","estimate_net_worth"),"code");


            t.addColumns(t.doubleColumn("SHARE").multiply(t.stringColumn("estimate_net_worth").replaceAll("--","0.0").parseDouble()));

            System.out.println(t.print());
            System.out.println(t.shape());
//            System.out.println(realNetTable.structure().printAll());
        }
    }
}
