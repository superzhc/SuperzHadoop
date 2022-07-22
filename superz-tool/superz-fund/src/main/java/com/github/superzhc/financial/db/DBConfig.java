package com.github.superzhc.financial.db;

import com.github.superzhc.common.utils.PathUtils;
import com.github.superzhc.financial.db.dao.IndexBasicDao;

/**
 * @author superz
 * @create 2022/7/22 10:12
 **/
public class DBConfig {
    public static final String DEFAULT_DB_STORE_PATH = "superz-tool/superz-fund/db/superz_fund.db";

    public static String defaultJdbcUrl() {
        return String.format("jdbc:sqlite:%s/%s", PathUtils.project(), DEFAULT_DB_STORE_PATH);
    }

    public static final String TABLE_INDEX_BASIC = "CREATE TABLE index_basic(id VARCHAR(10),code VARCHAR(6),market VARCHAR(4),name VARCHAR(512))";

    public static void main(String[] args) {
        System.out.println(new IndexBasicDao().indexCodes());
    }
}
