package com.github.superzhc.common.jdbc.schema;

import java.util.List;

public interface DBSchema
{
    List<Table> tables(String dbname);

    List<Column> columns(String tablename);
}
