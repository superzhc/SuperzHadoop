package com.github.superzhc.data.warehouse.ddl;

import com.github.superzhc.common.base.Preconditions;
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

    private static final String TABLE_SCHEMA_ENGINE = "engine";
    private static final String TABLE_SCHEMA_ENGINE_NAME = "engine_name";
    private static final String TABLE_SCHEMA_ENGINE_PARAMS = "params";

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
        set(TABLE_SCHEMA_ENGINE_NAME, name, TABLE_SCHEMA_ENGINE);
        return this;
    }

    public ClickHouseDDLDialect engineParams(String key, Object value) {
        set(key, value, TABLE_SCHEMA_ENGINE, TABLE_SCHEMA_ENGINE_PARAMS);
        return this;
    }

    @Override
    public String ddlTemplate() {
        String sql = "CREATE TABLE ${table_name}(" +
                "${fields} " +
                "${primary_key}" +
                ")ENGINE = ${engine} " +
                "${table_comment}";
        return sql;
    }

    @Override
    protected String convertTableOption(String option, Object value) {
        if (TABLE_SCHEMA_ENGINE.equalsIgnoreCase(option)) {
            return convertEngine(value);
        } else if (String.format("%s.%s", TABLE_SCHEMA_ENGINE_NAME, TABLE_SCHEMA_ENGINE_PARAMS).equalsIgnoreCase(option)) {
            return convertEngineParams(value);
        }

        return super.convertTableOption(option, value);
    }

    private String convertEngine(Object value) {
        Preconditions.checkNotNull(value, "ClickHouse must set engine");
        Map<String, Object> engine = convertValue(value);
        String engineName = engine.get(TABLE_SCHEMA_ENGINE_NAME).toString();
        String engineParams = convertEngineParams(engine.get(TABLE_SCHEMA_ENGINE_PARAMS));
        return engineName + engineParams;
    }

    private String convertEngineParams(Object value) {
        Map<String, Object> params = convertValue(value);

        if (null != params && params.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                sb.append(", ").append(param.getKey()).append("=").append(param.getValue());
            }

            return String.format("(%s)", sb.substring(1));
        }

        return "";
    }

    @Override
    protected Map<String, String> getFieldTypeMapping() {
        return typeMapping;
    }
}
