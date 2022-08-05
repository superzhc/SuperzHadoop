package com.github.superzhc.financial.db.dao;

import com.github.superzhc.common.jdbc.JdbcHelper;

import java.util.List;

/**
 * @author superz
 * @create 2022/7/22 14:14
 **/
public class IndexBasicDao extends BaseDao {
    public IndexBasicDao() {
        super();
    }

    public IndexBasicDao(JdbcHelper jdbc) {
        super(jdbc);
    }

    @Override
    protected String tableName() {
        return "index_basic";
    }

    public List<String> indexCodes() {
        String sql = "SELECT id FROM index_basic";
        List<String> codes = jdbc.queryOneColumn(sql);
        return codes;
    }
}
