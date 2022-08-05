package com.github.superzhc.financial.db.dao;

import com.github.superzhc.common.jdbc.JdbcHelper;

/**
 * @author superz
 * @create 2022/8/5 13:51
 **/
public class IndexEstimateDao extends BaseDao {
    public IndexEstimateDao() {
        super();
    }

    public IndexEstimateDao(JdbcHelper jdbc) {
        super(jdbc);
    }

    @Override
    protected String tableName() {
        return "index_estimate";
    }
}
