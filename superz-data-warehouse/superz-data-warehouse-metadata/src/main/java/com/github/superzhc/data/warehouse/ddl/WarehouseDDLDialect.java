package com.github.superzhc.data.warehouse.ddl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.superzhc.common.base.Preconditions;
import com.github.superzhc.common.jackson.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    protected static final String TABLE_TYPE_JAVA_BOOL = Boolean.class.getName();
    protected static final String TABLE_TYPE_JAVA_SHORT = Short.class.getName();
    protected static final String TABLE_TYPE_JAVA_INT = Integer.class.getName();
    protected static final String TABLE_TYPE_JAVA_LONG = Long.class.getName();
    protected static final String TABLE_TYPE_JAVA_FLOAT = Float.class.getName();
    protected static final String TABLE_TYPE_JAVA_DOUBLE = Double.class.getName();
    protected static final String TABLE_TYPE_JAVA_STRING = String.class.getName();


    protected static final String DDL_TABLE_SCHEMA_NAME = "table_name";
    protected static final String DDL_TABLE_SCHEMA_FIELDS = "fields";
    protected static final String DDL_TABLE_SCHEMA_FIELD_NAME = "field_name";
    protected static final String DDL_TABLE_SCHEMA_FIELD_TYPE = "field_type";
    protected static final String DDL_TABLE_SCHEMA_FIELD_IS_NOT_NULL = "is_not_null";
    protected static final String DDL_TABLE_SCHEMA_FIELD_COMMENT = "field_comment";
    /*方言特定的，使用通用json配置么？？？*/
    // protected static final String DDL_TABLE_SCHEMA_WITH = "with";
    // protected static final String DDL_TABLE_SCHEMA_ENGINE = "engine";
    // protected static final String DDL_TABLE_SCHEMA_ENGINE_NAME = "engine_name";
    protected static final String DDL_TABLE_SCHEMA_PARTITION = "partition";
    protected static final String DDL_TABLE_SCHEMA_PRIMARY_KEY = "primary_key";
    protected static final String DDL_TABLE_SCHEMA_COMMENT = "table_comment";

    protected static Pattern patternFieldOption = Pattern.compile(String.format("%s\\[\\d+\\]\\.([\\s\\S]+)", DDL_TABLE_SCHEMA_FIELDS));
    // protected static Pattern patternWithOption = Pattern.compile(String.format("%s\\.([\\s\\S]+)", DDL_TABLE_SCHEMA_WITH));
    // protected static Pattern patternEngineOption = Pattern.compile(String.format("%s\\.([\\s\\S]+)", DDL_TABLE_SCHEMA_ENGINE));

    protected String dialectName;
    protected ObjectNode dialectJson;

    public WarehouseDDLDialect(String dialectName) {
        this.dialectName = dialectName;
        this.dialectJson = JsonUtils.mapper().createObjectNode();
    }

    public final WarehouseDDLDialect set(String key, Object value, String... children) {
        JsonUtils.put(dialectJson, key, value, children);
        return this;
    }

    public abstract String ddlTemplate();

    public final String convertParam(String param, Object value) {
        // 扩展方言自身的参数配置，优先级高于配置文件
        Object[] jsonPaths = JsonUtils.convertPaths(param);
        Object dialectJsonValue = JsonUtils.objectValue(dialectJson, jsonPaths);
        value = null == dialectJsonValue ? value : dialectJsonValue;

        String dialectValue = null;
        if (DDL_TABLE_SCHEMA_NAME.equalsIgnoreCase(param)) {
            dialectValue = convertTableName(value);
        } else if (DDL_TABLE_SCHEMA_FIELDS.equalsIgnoreCase(param)) {
            dialectValue = convertFields(value);
        } else if (param.startsWith(DDL_TABLE_SCHEMA_FIELDS)) {
            Matcher m = patternFieldOption.matcher(param);
            if (m.find()) {
                String option = m.group(1);
                dialectValue = convertFieldOption0(option, value);
            } else {
                dialectValue = convertField(value);
            }
        } else if (DDL_TABLE_SCHEMA_PARTITION.equalsIgnoreCase(param)) {
            dialectValue = convertPartition(value);
        }
//        else if (DDL_TABLE_SCHEMA_WITH.equalsIgnoreCase(param)) {
//            dialectValue = convertWith(value);
//        } else if (param.startsWith(DDL_TABLE_SCHEMA_WITH)) {
//            Matcher m = patternWithOption.matcher(param);
//            String s = m.group(1);
//            dialectValue = convertWithOption0(s, value);
//        } else if (DDL_TABLE_SCHEMA_ENGINE.equalsIgnoreCase(param)) {
//            dialectValue = convertEngine(value);
//        } else if (param.startsWith(DDL_TABLE_SCHEMA_ENGINE)) {
//            Matcher m = patternEngineOption.matcher(param);
//            String s = m.group(1);
//            dialectValue = convertEngineOption0(s, value);
//        }
        else {
            dialectValue = convertTableOption0(param, value);
        }

        LOG.debug("[{}] <{}>={}", dialectName, param, dialectValue);
        return dialectValue;
    }

    // region======================================表名、表选项==========================================================
    protected String convertTableName(Object value) {
        Preconditions.checkNotNull(value);
        return convertValue(value);
    }

    private String convertTableOption0(String option, Object value0) {
        String value;
        if (DDL_TABLE_SCHEMA_COMMENT.equalsIgnoreCase(option)) {
            value = convertTableComment(value0);
        } else if (DDL_TABLE_SCHEMA_PRIMARY_KEY.equals(option)) {
            value = convertPrimaryKey(value0);
        } else {
            value = convertTableOption(option, value0);
        }
        return value;
    }

    protected String convertTableComment(Object value) {
        return format("COMMENT '%s'", value);
    }

    protected String convertPrimaryKey(Object value) {
        if (null == value) {
            return "";
        }

        List<String> keys = convertValue(value);
        if (null == keys || keys.size() == 0) {
            return "";
        }

        return format("PRIMARY KEY(%s)", keys.stream().collect(Collectors.joining(",")));
    }

    protected String convertTableOption(String option, Object value) {
        return null == value ? "" : value.toString();
    }

    // endregion===================================表名、表选项==========================================================

    // region=====================================字段==================================================================
    protected String convertFields(Object value) {
        List<Map<String, Object>> fields = convertValue(value);

        Preconditions.checkArgument(null != fields && fields.size() > 0, "Field at least one!");

        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> field : fields) {
            sb.append(",").append(convertField(field));
        }
        return sb.substring(1);
    }

    protected String convertField(Object value) {
        Preconditions.checkNotNull(value);
        Map<String, Object> field = convertValue(value);

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> item : field.entrySet()) {
            String option = item.getKey();
            String optionValue = convertFieldOption0(option, item.getValue());

            LOG.debug("[{}]-[FIELD] <{}>={}", dialectName, option, optionValue);

            if (null == optionValue || optionValue.trim().length() == 0) {
                continue;
            }

            sb.append(" ").append(optionValue);
        }
        return sb.substring(1);
    }

    private String convertFieldOption0(String option, Object value) {
        String fieldOption;
        if (DDL_TABLE_SCHEMA_FIELD_NAME.equalsIgnoreCase(option)) {
            fieldOption = convertFieldName(value);
        } else if (DDL_TABLE_SCHEMA_FIELD_TYPE.equalsIgnoreCase(option)) {
            fieldOption = convertFieldSimpleJavaType(value);
        } else if (DDL_TABLE_SCHEMA_FIELD_IS_NOT_NULL.equalsIgnoreCase(option)) {
            fieldOption = convertFieldIsNotNull(value);
        } else if (DDL_TABLE_SCHEMA_FIELD_COMMENT.equalsIgnoreCase(option)) {
            fieldOption = convertFieldComment(value);
        } else {
            fieldOption = convertFieldOption(option, value);
        }
        return fieldOption;
    }

    protected String convertFieldName(Object value) {
        return convertValue(value);
    }

    private String convertFieldSimpleJavaType(Object value) {
        String customType = convertValue(value);
        String clazz;
        switch (customType) {
            case "bool":
                clazz = TABLE_TYPE_JAVA_BOOL;
                break;
            case "short":
                clazz = TABLE_TYPE_JAVA_SHORT;
                break;
            case "int":
                clazz = TABLE_TYPE_JAVA_INT;
                break;
            case "long":
                clazz = TABLE_TYPE_JAVA_LONG;
                break;
            case "float":
                clazz = TABLE_TYPE_JAVA_FLOAT;
                break;
            case "double":
                clazz = TABLE_TYPE_JAVA_DOUBLE;
                break;
            case "string":
            default:
                clazz = TABLE_TYPE_JAVA_STRING;
        }
        return convertFieldType(clazz);
    }

    protected String convertFieldType(String javaClazz) {
        if (getFieldTypeMapping().containsKey(javaClazz)) {
            return getFieldTypeMapping().get(javaClazz);
        } else {
            return getFieldTypeMapping().get(TABLE_TYPE_JAVA_STRING);
        }
    }

    protected abstract Map<String, String> getFieldTypeMapping();

    protected String convertFieldIsNotNull(Object value) {
        Boolean b = convertValue(value);
        if (null == b || !b) {
            return "NULL";
        } else {
            return "NOT NULL";
        }
    }

    protected String convertFieldOption(String option, Object value) {
        return null == value ? "" : value.toString();
    }

    protected String convertFieldComment(Object value) {
        return format("COMMENT '%s'", value);
    }

    // endregion==================================字段==================================================================

    // region=====================================分区==================================================================

    protected String convertPartition(Object value) {
        throw new UnsupportedOperationException();
    }

    // endregion==================================分区==================================================================

    // region=====================================With子句[废弃]========================================================

//    protected String convertWith(Object value) {
//        // Map<String, Object> with = (Map<String, Object>) value;
//        //
//        // StringBuilder sb = new StringBuilder();
//        // for (Map.Entry<String, Object> param : with.entrySet()) {
//        //     sb.append(",").append(convertWithOption(param.getKey(), param.getValue()));
//        // }
//        // return sb.substring(1);
//        throw new UnsupportedOperationException();
//    }
//
//    private String convertWithOption0(String option, Object value0) {
//        String value;
//        value = convertWithOption(option, value0);
//        return value;
//    }
//
//    protected String convertWithOption(String param, Object value) {
//        throw new UnsupportedOperationException();
//    }

    // endregion==================================With子句[废弃]========================================================

    // region=====================================Engine[废弃]==========================================================

//    protected String convertEngine(Object value) {
//        throw new UnsupportedOperationException();
//    }
//
//    private String convertEngineOption0(String option, Object value0) {
//        String value;
//        value = convertEngineOption(option, value0);
//        return value;
//    }
//
//    protected String convertEngineOption(String param, Object value) {
//        throw new UnsupportedOperationException();
//    }

    // endregion==================================Engine[废弃]==========================================================

    protected String format(String template, Object value) {
        if (null == value) {
            return "";
        }

        return String.format(template, value);
    }

    protected <T> T convertValue(Object value) {
        return (T) value;
    }
}
