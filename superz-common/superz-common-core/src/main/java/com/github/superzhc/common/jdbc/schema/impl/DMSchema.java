package com.github.superzhc.common.jdbc.schema.impl;

import java.util.List;

import com.github.superzhc.common.jdbc.schema.AbstractDBSchema;
import com.github.superzhc.common.jdbc.schema.Column;
import com.github.superzhc.common.jdbc.schema.Table;

/**
 * 2020年11月23日 superz add
 */
public class DMSchema extends AbstractDBSchema
{
    public DMSchema(String url) {
        super(url);
    }

    @Override protected String tableSchemaSQL() {
        return "select * from dba_tables";
    }

    @Override
    public List<Column> columns(String tablename) {
        return null;
    }
}
