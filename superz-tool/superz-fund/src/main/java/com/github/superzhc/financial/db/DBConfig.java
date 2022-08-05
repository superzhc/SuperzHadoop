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
    public static final String TABLE_FUND_RECORDS="create table fund_records(id INTEGER primary key autoincrement,fund_code TEXT not null,fund_name TEXT,index_code TEXT,index_name TEXT,buy_date CHAR(10),net_worth DOUBLE,share DOUBLE,worth DOUBLE,invest DOUBLE)";
    public static final String TABLE_INDEX_ESTIMATE="create table index_estimate(id integer primary key autoincrement,code varchar(10),name varchar(255),valuation double,valuation_type varchar(5),valuation_percentage double,valuation_lowest double,valuation_highest double,underestimal_threshold double,overestimate_threshold double,dividend_yield double,roe double,valuation_date date,note text)";

    public static void main(String[] args) {
        System.out.println(new IndexBasicDao().indexCodes());
    }
}
