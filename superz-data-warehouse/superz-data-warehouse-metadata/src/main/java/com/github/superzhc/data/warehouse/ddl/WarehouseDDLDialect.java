package com.github.superzhc.data.warehouse.ddl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2023/4/15 17:33
 **/
public abstract class WarehouseDDLDialect {
    private static final Logger LOG = LoggerFactory.getLogger(WarehouseDDLDialect.class);

    private static final String DDL_TABLE_SCHEMA_NAME = "table_name";
    private static final String DDL_TABLE_SCHEMA_FIELDS = "fields";
    private static final String DDL_TABLE_SCHEMA_FIELD_NAME = "field_name";
    private static final String DDL_TABLE_SCHEMA_FIELD_TYPE = "field_type";
    private static final String DDL_TABLE_SCHEMA_WITH = "with";
    private static final String DDL_TABLE_SCHEMA_ENGINE = "engine";
    // private static final String DDL_TABLE_SCHEMA_ENGINE_NAME = "engine_name";
    private static final String DDL_TABLE_SCHEMA_PARTITION = "partition";
    private static final String DDL_TABLE_SCHEMA_COMMENT = "comment";

    private static Pattern patternFieldOption = Pattern.compile(String.format("%s\\[\\d+\\]\\.([\\s\\S]+)", DDL_TABLE_SCHEMA_FIELDS));
    private static Pattern patternWithOption = Pattern.compile(String.format("%s\\.([\\s\\S]+)", DDL_TABLE_SCHEMA_WITH));
    private static Pattern patternEngineOption = Pattern.compile(String.format("%s\\.([\\s\\S]+)", DDL_TABLE_SCHEMA_ENGINE));


    private String dialectName;

    public WarehouseDDLDialect(String dialectName) {
        this.dialectName = dialectName;
    }

    public abstract String ddlTemplate();

    public final String convertParam(String param, Object value) {
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
        } else if (DDL_TABLE_SCHEMA_WITH.equalsIgnoreCase(param)) {
            dialectValue = convertWith(value);
        } else if (param.startsWith(DDL_TABLE_SCHEMA_WITH)) {
            Matcher m = patternWithOption.matcher(param);
            String s = m.group(1);
            dialectValue = convertWithOption(s, value);
        } else if (DDL_TABLE_SCHEMA_ENGINE.equalsIgnoreCase(param)) {
            dialectValue = convertEngine(value);
        } else if (param.startsWith(DDL_TABLE_SCHEMA_ENGINE)) {
            Matcher m = patternEngineOption.matcher(param);
            String s = m.group(1);
            dialectValue = convertEngineOption(s, value);
        } else if (DDL_TABLE_SCHEMA_COMMENT.equalsIgnoreCase(param)) {
            dialectValue = convertTableComment(value);
        } else {
            dialectValue = convertTableOptions(param, value);
        }
        LOG.debug("[{}] <{}>={}", dialectName, param, dialectValue);
        return dialectValue;
    }

    protected String convertTableName(Object value) {
        return convertValue(value);
    }

    protected abstract String convertTableOptions(String option, Object value);

    protected String convertFields(Object value) {
        List<Map<String, Object>> fields = (List<Map<String, Object>>) value;

        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> field : fields) {
            sb.append(",").append(convertField(field));
        }
        return sb.substring(1);
    }

    protected String convertField(Object value) {
        Map<String, Object> field = (Map<String, Object>) value;

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> item : field.entrySet()) {
            String option = convertFieldOption0(item.getKey(), value);
        }
        return sb.substring(1);
    }

    protected String convertFieldOption0(String option, Object value) {
        if (DDL_TABLE_SCHEMA_FIELD_NAME.equalsIgnoreCase(option)) {
            return convertFieldName(value);
        } else if (DDL_TABLE_SCHEMA_FIELD_TYPE.equalsIgnoreCase(option)) {
            return convertFieldType(value);
        } else {
            return convertFieldOption(option, value);
        }
    }

    protected String convertFieldName(Object value) {
        return convertValue(value);
    }

    protected String convertFieldType(Object value) {
        String customType = convertValue(value);
        Class clazz;
        switch (customType) {
            case "bool":
                clazz = Boolean.class;
                break;
            case "short":
                clazz = Short.class;
                break;
            case "int":
                clazz = Integer.class;
                break;
            case "long":
                clazz = Long.class;
                break;
            case "float":
                clazz = Float.class;
                break;
            case "double":
                clazz = Double.class;
                break;
            case "string":
            default:
                clazz = String.class;
        }
        return java2db(clazz);
    }

    protected abstract String java2db(Class clazz);

    protected abstract String convertFieldOption(String option, Object value);

    protected String convertPartition(Object value) {
        throw new UnsupportedOperationException();
    }

    protected String convertWith(Object value) {
        // Map<String, Object> with = (Map<String, Object>) value;
        //
        // StringBuilder sb = new StringBuilder();
        // for (Map.Entry<String, Object> param : with.entrySet()) {
        //     sb.append(",").append(convertWithOption(param.getKey(), param.getValue()));
        // }
        // return sb.substring(1);
        throw new UnsupportedOperationException();
    }

    protected String convertWithOption(String param, Object value) {
        throw new UnsupportedOperationException();
    }

    protected String convertEngine(Object value) {
        throw new UnsupportedOperationException();
    }

    protected String convertEngineOption(String param, Object value) {
        throw new UnsupportedOperationException();
    }

    protected String convertTableComment(Object value) {
        return convertValue(value);
    }

    private String convertValue(Object value) {
        return String.valueOf(value);
    }
}
