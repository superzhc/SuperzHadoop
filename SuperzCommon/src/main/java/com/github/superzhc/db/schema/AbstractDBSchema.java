package com.github.superzhc.db.schema;

import com.github.superzhc.db.JdbcHelper;
import com.github.superzhc.collection.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 2020年11月23日 superz add
 */
public abstract class AbstractDBSchema implements DBSchema
{
    protected String url;
    protected JdbcHelper helper;

    public AbstractDBSchema(String url) {
        this.url = url;
        initJdbcHelper();
    }

    public void initJdbcHelper() {
        helper = new JdbcHelper(url);
    }

    @Override
    public List<Table> tables(String dbname) {
        String sql = tableSchemaSQL();
        /**
         * 数据表的元数据不是统一的，有两种方案：
         *  1. 直接在查询元数据的SQL中进行转换
         *  2. 用户自行实现数据的映射
         * 本代码中采用第2种方案，并且也兼容第一种方案
         */
        // List<Table> tables = helper.queryBeans(sql, Table.class);
        List<Map<String, Object>> lst = helper.query(sql);
        List<Table> tables = mapping(lst);
        return tables;
    }

    protected abstract String tableSchemaSQL();

    protected List<Table> mapping(List<Map<String, Object>> origins) {
        List<Table> lst = new ArrayList<>(origins.size());
        for (Map<String, Object> origin : origins) {
            lst.add(MapUtils.mapToBean(origin, Table.class));
        }
        return lst;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
