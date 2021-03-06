package com.github.superzhc.db.schema.impl;

import java.util.List;

import com.github.superzhc.db.schema.AbstractDBSchema;
import com.github.superzhc.db.schema.Column;

/**
 * 2020年11月23日 superz add
 */
public class SQLServerSchema extends AbstractDBSchema
{
    public SQLServerSchema(String url) {
        super(url);
    }

    @Override protected String tableSchemaSQL() {
        return "select name from sys.tables";
    }

    @Override public List<Column> columns(String tablename) {
        return null;
    }
}
