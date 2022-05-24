package com.github.superzhc.financial;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

/**
 * @author superz
 * @create 2022/5/24 16:28
 **/
public class FundRecordsDao extends BaseDao {
    public static final String TABLE_USER_FUND_RECORDS = "CREATE TABLE USER_FUND_RECORDS(ID INTEGER PRIMARY KEY,FUND_CODE TEXT NOT NULL,INDEX_NAME TEXT,RIQI CHAR(10),NET_WORTH DOUBLE,SHARE DOUBLE,WORTH DOUBLE,INVEST DOUBLE)";

    public FundRecordsDao(JdbcHelper dao) {
        super(dao);
    }

    public Table records() {
        String sql = "SELECT * FROM USER_FUND_RECORDS";
        Table table = TableUtils.db(dao, sql);
        return table;
    }

    public Table funds() {
        String sql = "SELECT FUND_CODE,INDEX_NAME,SUM(SHARE) AS TOTAL_SHARE,SUM(INVEST) AS TOTAL_INVEST,SUM(INVEST)/SUM(SHARE) AS AVG_COST from USER_FUND_RECORDS GROUP BY FUND_CODE,INDEX_NAME";
        Table table = TableUtils.db(dao, sql);
        return table;
    }
}
