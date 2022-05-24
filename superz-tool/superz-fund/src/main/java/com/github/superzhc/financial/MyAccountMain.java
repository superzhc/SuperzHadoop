package com.github.superzhc.financial;

import com.github.superzhc.common.jdbc.JdbcHelper;
import tech.tablesaw.api.Table;

/**
 * @author superz
 * @create 2022/5/20 13:41
 **/
public class MyAccountMain {
    public static final String JDBC_URL = "jdbc:sqlite:E:\\superz_financial\\db\\account.db";

    public static void main(String[] args) {
        try (JdbcHelper jdbc = new JdbcHelper(JDBC_URL)) {
            Table table = Table.create();

            FundRecordsDao fundRecordsDao = new FundRecordsDao(jdbc);
//            table=fundRecordsDao.records();
            table=fundRecordsDao.funds();

            IndexDao indexDao = new IndexDao(jdbc);
//            table = indexDao.indices();

            System.out.println(table.print());
            System.out.println(table.shape());
        }
    }
}
