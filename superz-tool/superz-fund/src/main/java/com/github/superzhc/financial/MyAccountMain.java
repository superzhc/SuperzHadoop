package com.github.superzhc.financial;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.indicator.InvestCalculator;

/**
 * @author superz
 * @create 2022/5/20 13:41
 **/
public class MyAccountMain {
    public static final String JDBC_URL = "jdbc:sqlite:E:\\superz_financial\\db\\account.db";

    public static void main(String[] args) {
        String symbol = "000905.SH";

        try (JdbcHelper jdbc = new JdbcHelper(JDBC_URL)) {
            // jdbc.show("select * from USER_FUND_RECORDS");

            double invest=InvestCalculator.investByCost(1.0, 1000, 0.95, 0.9);
            System.out.println(invest);
        }
    }
}
