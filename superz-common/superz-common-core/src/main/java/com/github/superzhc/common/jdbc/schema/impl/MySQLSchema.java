package com.github.superzhc.common.jdbc.schema.impl;

import com.github.superzhc.common.jdbc.schema.AbstractDBSchema;
import com.github.superzhc.common.jdbc.schema.Column;

import java.util.List;

/**
 * 2020年11月23日 superz add
 */
public class MySQLSchema extends AbstractDBSchema
{
    public MySQLSchema(String url) {
        super(url);
    }

    @Override
    protected String tableSchemaSQL() {
        // 通过对url进行处理，获取对应的数据库
        int position = url.indexOf("?");
        url = position == -1 ? url : url.substring(0, position);
        String tablebase = url.substring(url.lastIndexOf("/"));

        String sql = "select * from information_schema.tables where table_schema='" + tablebase + "'";
        return sql;
    }

    @Override
    public List<Column> columns(String tablename) {
        return null;
    }
}
