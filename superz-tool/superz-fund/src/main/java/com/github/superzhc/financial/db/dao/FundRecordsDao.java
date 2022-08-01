package com.github.superzhc.financial.db.dao;

import com.github.superzhc.common.jdbc.JdbcHelper;

/**
 * @author superz
 * @create 2022/8/1 15:16
 **/
public class FundRecordsDao extends BaseDao {
    public FundRecordsDao() {
        super();
    }

    public FundRecordsDao(JdbcHelper jdbc) {
        super(jdbc);
    }

    public void add(String fundCode, String fundName, String indexCode, String indexName, String buyDate, Double invest) {
        add(fundCode, fundName, indexCode, indexName, buyDate, null, null, null, invest);
    }

    public void add(String fundCode, String fundName, String indexCode, String indexName, String buyDate, Double netWorth, Double share, Double worth, Double invest) {
        String sql = "insert into fund_records (fund_code, fund_name, index_code, index_name, buy_date, net_worth, share, worth, invest) values (?,?,?,?,?,?,?,?,?)";
        int result = jdbc.dmlExecute(sql, fundCode, fundName, indexCode, indexName, buyDate, netWorth, share, worth, invest);
    }
}
