package com.github.superzhc.financial;

import com.github.superzhc.common.jdbc.JdbcHelper;

/**
 * @author superz
 * @create 2022/5/12 19:16
 **/
public class ConfigDao extends BaseDao {
    /*用户配置表*/
    public static final String TABLE_USER_CONFIG = "CREATE TABLE USER_CONFIG(CONFIG_KEY TEXT PRIMARY KEY NOT NULL,CONFIG_VALUE TEXT,NOTE TEXT)";

    public ConfigDao(JdbcHelper dao) {
        super(dao);
    }

    public String TushareToken() {
        return getConfigValue("tushare_token");
    }

    public String LiXingerAccount() {
        return getConfigValue("lixinger_account");
    }

    public String LiXingerPassword() {
        return getConfigValue("lixinger_password");
    }

    public <T> T getConfigValue(String key) {
        return dao.queryOne("select config_value from USER_CONFIG where config_key=?", key);
    }

    public int insert(String key, String value) {
        String sql = "INSERT INTO USER_CONFIG(CONFIG_KEY,CONFIG_VALUE) VALUES(?,?)";
        int result = dao.dmlExecute(sql, key, value);
        return result;
    }

    public int insert(String key, String value, String note) {
        String sql = "INSERT INTO USER_CONFIG(CONFIG_KEY,CONFIG_VALUE,NOTE) VALUES(?,?,?)";
        int result = dao.dmlExecute(sql, key, value, note);
        return result;
    }
}
