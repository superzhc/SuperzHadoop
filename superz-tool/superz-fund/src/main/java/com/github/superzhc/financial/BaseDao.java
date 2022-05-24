package com.github.superzhc.financial;

import com.github.superzhc.common.jdbc.JdbcHelper;

/**
 * @author superz
 * @create 2022/5/24 16:28
 **/
public abstract class BaseDao {
    protected JdbcHelper dao;

    public BaseDao(JdbcHelper dao) {
        this.dao = dao;
    }
}
