package com.github.superzhc.data.warehouse.ddl;

import com.github.superzhc.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2023/4/18 10:41
 **/
public class SparkSQLDDLDialect extends WarehouseDDLDialect {
    private static final Logger LOG = LoggerFactory.getLogger(SparkSQLDDLDialect.class);

    private static final String TABLE_SCHEMA_DATA_SOURCE = "data_source";
    private static final String TABLE_SCHEMA_OPTIONS = "options";
    private static final String TABLE_SCHEMA_LOCATION = "location";
    private static final String TABLE_SCHEMA_TBL_PROPERTIES = "tbl_properties";

    private static Pattern patternOptions = Pattern.compile(TABLE_SCHEMA_OPTIONS + "\\.(\\S+?)");
    private static Pattern patternTblProperties = Pattern.compile(TABLE_SCHEMA_TBL_PROPERTIES + "\\.(\\S+?)");

    private static Map<String, String> typeMapping = new HashMap<>();

    static {
        typeMapping.put(TABLE_TYPE_JAVA_BYTE, "BYTE");
        typeMapping.put(TABLE_TYPE_JAVA_SHORT, "SHORT");
        typeMapping.put(TABLE_TYPE_JAVA_INT, "INT");
        typeMapping.put(TABLE_TYPE_JAVA_LONG, "LONG");
        typeMapping.put(TABLE_TYPE_JAVA_FLOAT, "FLOAT");
        typeMapping.put(TABLE_TYPE_JAVA_DOUBLE, "DOUBLE");
        typeMapping.put(TABLE_TYPE_JAVA_STRING, "STRING");
    }

    public SparkSQLDDLDialect() {
        super("Spark SQL");
    }

    public SparkSQLDDLDialect dataSource(String value) {
        set(TABLE_SCHEMA_DATA_SOURCE, value);
        return this;
    }

    public SparkSQLDDLDialect options(String key, Object value) {
        set(key, value, TABLE_SCHEMA_OPTIONS);
        return this;
    }

    public SparkSQLDDLDialect location(String value) {
        set(TABLE_SCHEMA_LOCATION, value);
        return this;
    }

    public SparkSQLDDLDialect tblProperties(String key, Object value) {
        set(key, value, TABLE_SCHEMA_TBL_PROPERTIES);
        return this;
    }

    @Override
    public String ddlTemplate() {
        String sql = "CREATE TABLE ${table_name}(" +
                "${fields}" +
                ")USING ${data_source} " +
                "${options} " +
                "${partition} " +
                "${location} " +
                "${table_comment} " +
                "${tbl_properties}";
        return sql;
    }

    @Override
    protected String convertTableOption(String option, Object value) {
        if (TABLE_SCHEMA_DATA_SOURCE.equalsIgnoreCase(option)) {
            Preconditions.checkNotNull(value, "Spark SQL USING Option not null");
            return value.toString();
        } else if (TABLE_SCHEMA_OPTIONS.equalsIgnoreCase(option)) {
            Map<String, Object> sparkOptions = convertValue(value);
            if (null != sparkOptions && sparkOptions.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, Object> sparkOption : sparkOptions.entrySet()) {
                    sb.append(",").append(convertOption(sparkOption.getKey(), sparkOption.getValue()));
                }
                return String.format("OPTIONS(%s)", sb.substring(1));
            }
        } else if (option.startsWith(TABLE_SCHEMA_OPTIONS)) {
            Matcher m = patternOptions.matcher(option);
            if (m.find()) {
                convertOption(m.group(1), value);
            }
        } else if (TABLE_SCHEMA_TBL_PROPERTIES.equalsIgnoreCase(option)) {
            Map<String, Object> sparkOptions = convertValue(value);
            if (null != sparkOptions && sparkOptions.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, Object> sparkOption : sparkOptions.entrySet()) {
                    sb.append(",").append(convertTBLProperty(sparkOption.getKey(), sparkOption.getValue()));
                }
                return String.format("TBLPROPERTIES(%s)", sb.substring(1));
            }
        } else if (option.startsWith(TABLE_SCHEMA_TBL_PROPERTIES)) {
            Matcher m = patternTblProperties.matcher(option);
            if (m.find()) {
                convertTBLProperty(m.group(1), value);
            }
        } else if (TABLE_SCHEMA_LOCATION.equalsIgnoreCase(option)) {
            return format("LOCATION %s", value);
        }
        return super.convertTableOption(option, value);
    }

    private String convertOption(String key, Object value) {
        return String.format("'%s'='%s'", key, value);
    }

    private String convertTBLProperty(String key, Object value) {
        return String.format("'%s'='%s'", key, value);
    }

    @Override
    protected Map<String, String> getFieldTypeMapping() {
        return typeMapping;
    }
}
