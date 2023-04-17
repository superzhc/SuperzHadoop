package com.github.superzhc.data.warehouse.ddl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/4/17 3:34
 */
public class MySQLDDLDialect extends WarehouseDDLDialect {
    private static final Logger LOG = LoggerFactory.getLogger(MySQLDDLDialect.class);

    private static final String DDL_TABLE_SCHEMA_FIELD_AUTO_INCREMENT = "auto_increment";
    private static final String DDL_TABLE_SCHEMA_FIELD_DEFAULT_VALUE = "default";
    private static final String DDL_tABLE_SCHEMA_CHARACTER = "character";

    private static Map<String, String> typeMapping = new HashMap<>();

    static {
        typeMapping.put(TABLE_TYPE_JAVA_INT, "int");
        typeMapping.put(TABLE_TYPE_JAVA_LONG, "bigint");
        typeMapping.put(TABLE_TYPE_JAVA_FLOAT, "float");
        typeMapping.put(TABLE_TYPE_JAVA_STRING, "VARCHAR(255)");
    }

    public MySQLDDLDialect() {
        super("MySQL");
        init();
    }

    private void init() {
        engine("InnoDB");
    }

    public MySQLDDLDialect engine(String engine) {
        set("engine", engine);
        return this;
    }

    @Override
    public String ddlTemplate() {
        String sqlTemplate = "CREATE TABLE ${table_name}(" +
                "   ${fields}" +
                "   ${primary_key}" +
                ")ENGINE=${engine}" +
                "   ${character}" +
                "   ${table_comment}";
        return sqlTemplate;
    }

    @Override
    protected String convertTableOption(String option, Object value) {
        if (DDL_tABLE_SCHEMA_CHARACTER.equalsIgnoreCase(option)) {
            return format("DEFAULT CHARACTER SET=%s", value);
        }
        return super.convertTableOption(option, value);
    }

    @Override
    protected Map<String, String> getFieldTypeMapping() {
        return typeMapping;
    }

    @Override
    protected String convertFieldOption(String option, Object value0) {
        String value = super.convertFieldOption(option, value0);
        if (DDL_TABLE_SCHEMA_FIELD_AUTO_INCREMENT.equalsIgnoreCase(option)) {
            if (null == value0 || value0 instanceof Boolean && !((Boolean) value0)) {
                value = "";
            } else {
                value = "AUTO_INCREMENT";
            }
        } else if (DDL_TABLE_SCHEMA_FIELD_DEFAULT_VALUE.equalsIgnoreCase(option)) {
            if (value0 instanceof Number || value0 instanceof Boolean) {
                value = format("DEFAULT %s", value0);
            } else {
                value = format("DEFAULT '%s'", value0);
            }
        }
        return value;
    }
}
