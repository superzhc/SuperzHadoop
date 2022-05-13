package com.github.superzhc.financial;

import com.github.superzhc.common.jdbc.JdbcHelper;

/**
 * @author superz
 * @create 2022/5/12 19:16
 **/
public class AccountDao {
    private JdbcHelper dao;

    public AccountDao(JdbcHelper dao) {
        this.dao = dao;
    }

    public String TushareToken(){
        return getConfigValue("tushare_token");
    }

    public <T> T getConfigValue(String key) {
        return dao.queryOne("select config_value from USER_CONFIG where config_key=?", key);
    }
}
