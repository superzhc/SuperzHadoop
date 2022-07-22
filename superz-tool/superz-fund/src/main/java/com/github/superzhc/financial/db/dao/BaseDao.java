package com.github.superzhc.financial.db.dao;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.financial.db.DBConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author superz
 * @create 2022/7/22 14:09
 **/
public abstract class BaseDao {
    protected static final Logger log = LoggerFactory.getLogger(BaseDao.class);

    protected JdbcHelper jdbc;

    public BaseDao() {
        this.jdbc = new JdbcHelper(DBConfig.defaultJdbcUrl());
    }

    public BaseDao(JdbcHelper jdbc) {
        this.jdbc = jdbc;
    }

    public JdbcHelper getJdbc() {
        return jdbc;
    }
}
