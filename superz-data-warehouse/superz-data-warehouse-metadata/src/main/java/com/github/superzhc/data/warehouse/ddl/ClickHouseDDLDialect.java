package com.github.superzhc.data.warehouse.ddl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/4/17 14:27
 **/
public class ClickHouseDDLDialect extends WarehouseDDLDialect {
    private static final Logger LOG = LoggerFactory.getLogger(WarehouseDDLDialect.class);

    private static final String DDL_TABLE_SCHEMA_ENGINE = "engine";
    private static final String DDL_TABLE_SCHEMA_ENGINE_NAME = "name";

    private static final String DDL_TABLE_SCHEMA_ENGINE_PARAMS = "params";

    private static Map<String, String> typeMapping = new HashMap<>();

    static {
        typeMapping.put(TABLE_TYPE_JAVA_SHORT, "Int8");
        typeMapping.put(TABLE_TYPE_JAVA_INT, "Int32");
        typeMapping.put(TABLE_TYPE_JAVA_LONG, "Int64");
        typeMapping.put(TABLE_TYPE_JAVA_FLOAT, "Float32");
        typeMapping.put(TABLE_TYPE_JAVA_DOUBLE, "Float64");
        typeMapping.put(TABLE_TYPE_JAVA_STRING, "String");
    }

    public ClickHouseDDLDialect() {
        super("ClickHouse");
    }

    public ClickHouseDDLDialect engine(String name) {
        set(DDL_TABLE_SCHEMA_ENGINE_NAME, name, DDL_TABLE_SCHEMA_ENGINE);
        return this;
    }

    public ClickHouseDDLDialect engineParams(String key, Object value) {
        set(key, value, DDL_TABLE_SCHEMA_ENGINE, DDL_TABLE_SCHEMA_ENGINE_PARAMS);
        return this;
    }

    @Override
    public String ddlTemplate() {
        String sql = "CREATE TABLE ${table_name}(" +
                "   ${fields} " +
                "   ${primary_key}" +
                ")ENGINE = ${engine.name}(${engine.params})" +
                "   ${table_comment}";
        return sql;
    }

    @Override
    protected Map<String, String> getFieldTypeMapping() {
        return typeMapping;
    }
}
