package com.github.superzhc.data.warehouse.ddl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.superzhc.common.base.Preconditions;
import com.github.superzhc.common.jackson.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author superz
 * @create 2023/4/15 17:33
 **/
public abstract class WarehouseDDLDialect {
    private static final Logger LOG = LoggerFactory.getLogger(WarehouseDDLDialect.class);

    protected static final String TABLE_TYPE_JAVA_BYTE = Byte.class.getName();
    protected static final String TABLE_TYPE_JAVA_BOOL = Boolean.class.getName();
    protected static final String TABLE_TYPE_JAVA_SHORT = Short.class.getName();
    protected static final String TABLE_TYPE_JAVA_INT = Integer.class.getName();
    protected static final String TABLE_TYPE_JAVA_LONG = Long.class.getName();
    protected static final String TABLE_TYPE_JAVA_FLOAT = Float.class.getName();
    protected static final String TABLE_TYPE_JAVA_DOUBLE = Double.class.getName();
    protected static final String TABLE_TYPE_JAVA_STRING = String.class.getName();
    protected static final String TABLE_TYPE_JAVA_DECIMAL = BigDecimal.class.getName();
    protected static final String TABLE_TYPE_JAVA_DATE = Date.class.getName();
    protected static final String TABLE_TYPE_JAVA_TIMESTAMP = Timestamp.class.getName();
    protected static final String TABLE_TYPE_JAVA_LOCAL_DATE = LocalDate.class.getName();
    protected static final String TABLE_TYPE_JAVA_LOCAL_TIME = LocalTime.class.getName();
    protected static final String TABLE_TYPE_JAVA_LOCAL_DATE_TIME = LocalDateTime.class.getName();


    protected static final String TABLE_SCHEMA_CATALOG_NAME = "catalog_name";
    protected static final String TABLE_SCHEMA_DATABASE_NAME = "database_name";
    protected static final String TABLE_SCHEMA_TABLE_NAME = "table_name";

    /*Field & Field Options*/
    protected static final String TABLE_SCHEMA_FIELDS = "fields";
    protected static final String TABLE_SCHEMA_FIELD_NAME = "field_name";
    protected static final String TABLE_SCHEMA_FIELD_TYPE = "field_type";
    protected static final String TABLE_SCHEMA_FIELD_PRIMARY_KEY = "field_primary_key";
    protected static final String TABLE_SCHEMA_FIELD_IS_NOT_NULL = "field_is_not_null";
    protected static final String TABLE_SCHEMA_FIELD_DEFAULT_VALUE = "field_default_value";
    protected static final String TABLE_SCHEMA_FIELD_COMMENT = "field_comment";

    /*Table Options*/
    protected static final String TABLE_SCHEMA_PARTITION = "partition";
    protected static final String TABLE_SCHEMA_PRIMARY_KEY = "primary_key";
    protected static final String TABLE_SCHEMA_TABLE_COMMENT = "table_comment";

    //    protected static final Pattern patternTableOption = Pattern.compile(
//            String.format(
//                    "(%s)",
//                    String.join("|",
//                            // DDL_TABLE_SCHEMA_NAME,
//                            TABLE_SCHEMA_TABLE_COMMENT,
//                            TABLE_SCHEMA_PARTITION,
//                            TABLE_SCHEMA_PRIMARY_KEY
//                    )
//            )
//    );
    protected static Pattern patternFieldOption = Pattern.compile(String.format("%s\\[\\d+\\]\\.([\\s\\S]+)", TABLE_SCHEMA_FIELDS));
    private static Pattern patternFieldType = Pattern.compile("(\\S+?)\\((\\d+?)(,(\\d+?)){0,1}\\)");

    protected String dialectName;
    protected ObjectNode dialectJson;

    public WarehouseDDLDialect(String dialectName) {
        this.dialectName = dialectName;
        this.dialectJson = JsonUtils.mapper().createObjectNode();
    }

    public final WarehouseDDLDialect set(String key, Object value, String... children) {
        LOG.debug("[{}]-[CONFIG] <{}>={}", dialectName, (null == children || children.length == 0) ? key : (String.join(".", children) + "." + key), value);
        JsonUtils.put(dialectJson, key, value, children);
        return this;
    }

    public final WarehouseDDLDialect setArray(String arrayName, Object value, String... children) {
        LOG.debug("[{}]-[CONFIG] <{}[]>={}", dialectName, (null == children || children.length == 0) ? arrayName : (String.join(".", children) + "." + arrayName), value);
        JsonUtils.putArray(dialectJson, arrayName, value, children);
        return this;
    }

    public abstract String ddlTemplate();

    public final String convertParam(String param, Object value) {
        // 扩展方言自身的参数配置，优先级高于配置文件
        Object[] jsonPaths = JsonUtils.convertPaths(param);
        Object dialectJsonValue = JsonUtils.objectValue(dialectJson, jsonPaths);
        value = null == dialectJsonValue ? value : dialectJsonValue;

        String dialectValue = null;
        if (TABLE_SCHEMA_DATABASE_NAME.equalsIgnoreCase(param)) {
            dialectValue = convertDatabaseName(value);
        } else if (TABLE_SCHEMA_TABLE_NAME.equalsIgnoreCase(param)) {
            dialectValue = convertTableName(value);
        } else if (TABLE_SCHEMA_FIELDS.equalsIgnoreCase(param)) {
            dialectValue = convertFields(value);
        } else if (param.startsWith(TABLE_SCHEMA_FIELDS)) {
            Matcher m = patternFieldOption.matcher(param);
            if (m.find()) {
                String option = m.group(1);
                dialectValue = convertFieldOption0(option, value);
            } else {
                dialectValue = convertField(value);
            }
        } else {
//            // 意义不是很大的匹配
//            Matcher m = patternTableOption.matcher(param);
//            if (m.find()) {
            dialectValue = convertTableOption0(param, value);
//            } else {
//                dialectValue = convertTableOption(param, value);
//            }
        }

        LOG.debug("[{}] <{}>={}", dialectName, param, dialectValue);
        return dialectValue;
    }

    // region======================================表名、表选项==========================================================
    protected String convertDatabaseName(Object value) {
        // Preconditions.checkNotEmpty(value);
        // return convertValue(value);
        return convertValueWithNullToDefault(value, "default");
    }

    protected String convertTableName(Object value) {
        Preconditions.checkNotEmpty(value);
        return convertValue(value);
    }

    private String convertTableOption0(String option, Object value0) {
        String value;
        if (TABLE_SCHEMA_TABLE_COMMENT.equalsIgnoreCase(option)) {
            value = convertTableComment(value0);
        } else if (TABLE_SCHEMA_PRIMARY_KEY.equals(option)) {
            value = convertPrimaryKey(value0);
        } else if (TABLE_SCHEMA_PARTITION.equalsIgnoreCase(option)) {
            value = convertPartition(value0);
        } else {
            value = convertTableOption(option, value0);
        }
        return value;
    }

    protected String convertTableComment(Object value) {
        return formatValueWithNullToBlank("COMMENT '%s'", value);
    }

    protected String convertPrimaryKey(Object value) {
        if (null == value) {
            return "";
        }

        String s;
        if (value instanceof String) {
            s = (String) value;
        } else if (value.getClass().isArray()) {
            s = String.join(",", (String[]) value);
        } else if (value instanceof List) {
            List<String> keys = convertValue(value);
            s = keys.stream().collect(Collectors.joining(","));
        } else {
            throw new UnsupportedOperationException("不支持主键类型为：" + value.getClass());
        }

        return formatValueWithNullToBlank("PRIMARY KEY(%s)", s);
    }

    protected String convertPartition(Object value) {
        if (null == value) {
            return "";
        }

        String s;
        if (value instanceof String) {
            s = (String) value;
        } else if (value.getClass().isArray()) {
            s = String.join(",", (String[]) value);
        } else if (value instanceof List) {
            List<String> keys = convertValue(value);
            s = keys.stream().collect(Collectors.joining(","));
        } else {
            throw new UnsupportedOperationException("不支持分区类型为：" + value.getClass());
        }

        return formatValueWithNullToBlank("PARTITION BY (%s)", s);
    }

    protected String convertTableOption(String option, Object value) {
        return convertValue(value);
    }

    // endregion===================================表名、表选项==========================================================

    // region=====================================字段==================================================================
    protected String convertFields(Object value) {
        Preconditions.checkNotEmpty(value, "Field At Least One");
        List<Map<String, Object>> fields = convertValue(value);

        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> field : fields) {
            sb.append(",").append(convertField(field));
        }
        return sb.substring(1);
    }

    protected String convertField(Object value) {
        Preconditions.checkNotEmpty(value);
        Map<String, Object> field = convertValue(value);

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> item : field.entrySet()) {
            String option = item.getKey();
            String optionValue = convertFieldOption0(option, item.getValue());

            LOG.debug("[{}]-[FIELD] <{}>={}", dialectName, option, optionValue);

            sb.append(" ").append(optionValue);
        }
        return sb.substring(1);
    }

    private String convertFieldOption0(String option, Object value) {
        try {
            String fieldOption;
            if (TABLE_SCHEMA_FIELD_NAME.equalsIgnoreCase(option)) {
                fieldOption = convertFieldName(value);
            } else if (TABLE_SCHEMA_FIELD_TYPE.equalsIgnoreCase(option)) {
                fieldOption = convertFieldType0(value);
            } else if (TABLE_SCHEMA_FIELD_PRIMARY_KEY.equalsIgnoreCase(option)) {
                fieldOption = convertFieldPrimaryKey(value);
            } else if (TABLE_SCHEMA_FIELD_IS_NOT_NULL.equalsIgnoreCase(option)) {
                fieldOption = convertFieldIsNotNull(value);
            } else if (TABLE_SCHEMA_FIELD_COMMENT.equalsIgnoreCase(option)) {
                fieldOption = convertFieldComment(value);
            } else if (TABLE_SCHEMA_FIELD_DEFAULT_VALUE.equalsIgnoreCase(option)) {
                fieldOption = convertFieldDefaultValue(value);
            } else {
                fieldOption = convertFieldOption(option, value);
            }
            return fieldOption;
        } catch (UnsupportedOperationException e) {
            LOG.debug("[{}]-[FIELD] 不支持参数 <{}>", dialectName, option);
            return "";
        }
    }

    protected String convertFieldName(Object value) {
        Preconditions.checkNotEmpty(value);
        return convertValue(value);
    }

    private String convertFieldType0(Object value) {
        Preconditions.checkNotEmpty(value);
        String customType = convertValue(value);

        String type;
        Integer length = null;
        Integer precision = null;

        Matcher typeMatcher = patternFieldType.matcher(customType);
        if (typeMatcher.find()) {
            type = typeMatcher.group(1);
            length = Integer.valueOf(typeMatcher.group(2));
            if (typeMatcher.groupCount() == 4)
                precision = Integer.valueOf(typeMatcher.group(4));
        } else {
            type = customType;
        }

        String javaType;
        switch (type.toLowerCase()) {
            case "byte":
                javaType = TABLE_TYPE_JAVA_BYTE;
                break;
            case "bool":
            case "boolean":
                javaType = TABLE_TYPE_JAVA_BOOL;
                break;
            case "short":
                javaType = TABLE_TYPE_JAVA_SHORT;
                break;
            case "int":
            case "integer":
                javaType = TABLE_TYPE_JAVA_INT;
                break;
            case "bigint":
            case "long":
                javaType = TABLE_TYPE_JAVA_LONG;
                break;
            case "float":
                javaType = TABLE_TYPE_JAVA_FLOAT;
                break;
            case "double":
                javaType = TABLE_TYPE_JAVA_DOUBLE;
                break;
            case "string":
                javaType = TABLE_TYPE_JAVA_STRING;
                break;
            case "date":
                javaType = TABLE_TYPE_JAVA_DATE;
                break;
            default:
                javaType = customType;
                break;
        }
        return convertFieldType(javaType);
    }

    protected String convertFieldType(String javaClazz) {
        if (getFieldTypeMapping().containsKey(javaClazz)) {
            return getFieldTypeMapping().get(javaClazz);
        } else {
            return getFieldTypeMapping().get(TABLE_TYPE_JAVA_STRING);
        }
    }

    protected abstract Map<String, String> getFieldTypeMapping();

    protected String convertFieldPrimaryKey(Object value) {
        Boolean b = convertValue(value);
        if (null != b && b) {
            return "PRIMARY KEY";
        }
        return "";
    }

    protected String convertFieldIsNotNull(Object value) {
        Boolean b = convertValue(value);
        if (null == b || !b) {
            return "NULL";
        } else {
            return "NOT NULL";
        }
    }

    protected String convertFieldOption(String option, Object value) {
        return convertValueWithNullToBlank(value);
    }

    protected String convertFieldDefaultValue(Object value) {
        // bug：函数会被识别为字符串，待修复
        return formatValueWithNullToBlank("DEFAULT %s", /*convertSQLValue(value)*/value);
    }

    protected String convertFieldComment(Object value) {
        return formatValueWithNullToBlank("COMMENT '%s'", value);
    }

    // endregion==================================字段==================================================================

    // region=====================================工具==================================================================

    protected final String formatValueWithNullToBlank(String template, Object value) {
        if (null == value) {
            return "";
        }

        return formatValue(template, value);
    }

    protected final String formatValue(String template, Object value) {
        return String.format(template, value);
    }

    protected final String convertSQLValue(Object value) {
        if (null == value) {
            return "null";
        }

        if (value instanceof Integer
                || value instanceof Long
                || value instanceof Float
                || value instanceof Double
                || value instanceof BigDecimal) {
            return value.toString();
        } else {
            return String.format("'%s'", value);
        }

    }

    protected final String convertValueWithNullToBlank(Object value) {
        return convertValueWithNullToDefault(value, "");
    }

    protected final String convertValueWithNullToDefault(Object value, String defaultValue) {
        return null == value ? defaultValue : convertValue(value);
    }

    protected final <T> T convertValue(Object value) {
        return (T) value;
    }

    // endregion==================================工具==================================================================

}
