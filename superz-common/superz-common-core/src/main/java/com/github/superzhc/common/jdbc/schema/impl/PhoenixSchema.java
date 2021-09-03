package com.github.superzhc.common.jdbc.schema.impl;

import com.github.superzhc.common.jdbc.schema.AbstractDBSchema;
import com.github.superzhc.common.jdbc.schema.Column;

import java.util.List;

/**
 * 2020年11月23日 superz add
 */
public class PhoenixSchema extends AbstractDBSchema
{
    public PhoenixSchema(String url) {
        super(url);
    }

    @Override protected String tableSchemaSQL() {
        return "select * from SYSTEM.CATALOG";
    }

    @Override public List<Column> columns(String tablename) {
        return null;
    }
}
