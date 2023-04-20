package com.github.superzhc.data.warehouse.ddl;

import com.github.superzhc.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/4/19 9:04
 **/
public class FlinkSQLDDLDialect extends WarehouseDDLDialect {
    private static final Logger LOG = LoggerFactory.getLogger(FlinkSQLDDLDialect.class);

    private static final String TABLE_SCHEMA_METADATA = "metadata";
    private static final String TABLE_SCHEMA_METADATA_NAME = "field_name";
    private static final String TABLE_SCHEMA_METADATA_TYPE = "field_type";
    private static final String TABLE_SCHEMA_METADATA_FROM = "metadata_key";
    private static final String TABLE_SCHEMA_METADATA_VIRTUAL = "virtual";
    private static final String TABLE_SCHEMA_WITH = "with";

    private static Map<String, String> typeMapping = new HashMap<>();

    static {
        typeMapping.put(TABLE_TYPE_JAVA_LONG, "BIGINT");
        typeMapping.put(TABLE_TYPE_JAVA_STRING, "STRING");
    }

    public FlinkSQLDDLDialect() {
        super("Flink");
    }

    public FlinkSQLDDLDialect metadata(String name, String type) {
        return metadata(name, type, null, null);
    }

    public FlinkSQLDDLDialect metadata(String name, String type, String metadataKey) {
        return metadata(name, type, metadataKey, null);
    }

    public FlinkSQLDDLDialect metadata(String name, String type, String metadataKey, Boolean virtual) {
        Map<String, Object> map = new HashMap<>();
        map.put(TABLE_SCHEMA_METADATA_NAME, name);
        map.put(TABLE_SCHEMA_METADATA_TYPE, type);
        if (null != metadataKey) {
            map.put(TABLE_SCHEMA_METADATA_FROM, metadataKey);
        }
        if (null != virtual) {
            map.put(TABLE_SCHEMA_METADATA_VIRTUAL, virtual);
        }
        setArray(TABLE_SCHEMA_METADATA, map);
        return this;
    }

    public FlinkSQLDDLDialect connector(String value) {
        return with("connector", value);
    }

    public FlinkSQLDDLDialect with(String key, Object value) {
        set(key, value, TABLE_SCHEMA_WITH);
        return this;
    }

    @Override
    public String ddlTemplate() {
        String sql = "CREATE TABLE IF NOT EXISTS ${catalog_name}.${database_name}.${table_name}(" +
                " ${fields} " +
                " ${metadata} " +
                " ${primary_key}" +
                ")" +
                "${table_comment} " +
                "${partition} " +
                "WITH(" +
                "${with}" +
                ")";
        return sql;
    }

    @Override
    protected String convertTableOption(String option, Object value) {
        if (TABLE_SCHEMA_METADATA.equalsIgnoreCase(option)) {
            return convertMetaData(value);
        } else if (TABLE_SCHEMA_WITH.equalsIgnoreCase(option)) {
            Preconditions.checkNotEmpty(value);
            StringBuilder sb = new StringBuilder();
            Map<String, Object> params = convertValue(value);
            for (Map.Entry<String, Object> param : params.entrySet()) {
                sb.append(",").append(String.format("'%s'='%s'", param.getKey(), param.getValue()));
            }
            return sb.substring(1);
        }
        return super.convertTableOption(option, value);
    }

    @Override
    protected String convertPrimaryKey(Object value) {
        String s = super.convertPrimaryKey(value);
        return formatPrimaryKey(s);
    }

    @Override
    protected String convertFieldPrimaryKey(Object value) {
        String s = super.convertFieldPrimaryKey(value);
        return formatPrimaryKey(s);
    }

    private String formatPrimaryKey(String s) {
        if (null == s || s.trim().isEmpty()) {
            return "";
        }

        return s + " NOT ENFORCED";
    }

    @Override
    protected String convertFieldIsNotNull(Object value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Flink 列不支持默认值
     *
     * @param value
     * @return
     */
    @Override
    protected String convertFieldDefaultValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Map<String, String> getFieldTypeMapping() {
        return typeMapping;
    }

    private String convertMetaData(Object value) {
        List<Map<String, Object>> maps = convertValue(value);

        if (null != maps && maps.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Map<String, Object> map : maps) {
                sb.append(",")
                        .append(map.get(TABLE_SCHEMA_METADATA_NAME)).append(" ")
                        .append(map.get(TABLE_SCHEMA_METADATA_TYPE)).append(" ")
                        .append("METADATA")
                ;

                if (map.containsKey(TABLE_SCHEMA_METADATA_FROM)) {
                    sb.append(" FROM ").append(map.get(TABLE_SCHEMA_METADATA_FROM));
                }

                Boolean virtual = convertValue(map.get(TABLE_SCHEMA_METADATA_VIRTUAL));
                if (null != virtual && virtual) {
                    sb.append(" ").append("VIRTUAL");
                }
            }
            return sb.toString();
        }

        return "";
    }

    private String convertComputedColumn(Object value) {
        // TODO
        // column_name AS computed_column_expression [COMMENT column_comment]
        return "";
    }

    private String convertWaterMark(Object value) {
        // TODO
        // WATERMARK FOR rowtime_column_name AS watermark_strategy_expression
        String waterMarkDefinition = String.format("WATERMARK FOR %s AS %s");
        return "";
    }
}
