package com.github.superzhc.common.jdbc.generator;

import com.github.superzhc.common.format.LogFormat;
import com.github.superzhc.common.format.PlaceholderResolver;
import com.github.superzhc.common.jdbc.Column;
import com.github.superzhc.common.jdbc.Table;
import com.github.superzhc.common.utils.CamelCaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author superz
 * @create 2022/7/7 17:24
 **/
public abstract class BaseGenerator {
    protected static final Logger log = LoggerFactory.getLogger(BaseGenerator.class);

    protected String table = null;
    protected String tableComment = null;
    // protected Map<String, String> columns = new LinkedHashMap<>();
    protected Map<String, Map<String, String>> columns = new LinkedHashMap<>();
    protected List<String> pkKeys = new ArrayList<>();
    // protected Map<String, String> columnComments = new LinkedHashMap<>();
    private Map<String, String> tableExtra = new HashMap<>();

    private Class<?> clazz = null;

    public BaseGenerator(Class<?> clazz) {
        this.clazz = clazz;
    }

    public final String generate() {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("未配置 " + Table.class.getName() + " 注解");
        }

        Table t = clazz.getAnnotation(Table.class);
        // 表名
        table = handleTableName(t.name(), transformClassName(clazz.getSimpleName()));

        // 表注释
        tableComment = handleTableComment(t.comment());

        /*属性处理*/
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 未通过Column注解配置的属性，缺失信息，无法使用，直接过掉
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }

            Column column = field.getAnnotation(Column.class);

            /* 处理列名 */
            String columnName = (null == column.name() || column.name().trim().length() == 0) ? transformFieldName(field.getName()) : column.name();
            // 验证列名
            verifyColumnName(columnName);
            // 初始化列名信息
            Map<String, String> columnInfo = new HashMap<>();
            columns.put(columnName, columnInfo);

            handleColumnName(columnName);


            /*处理列类型*/
            String columnType = (null == column.type() || column.type().trim().length() == 0) ? handleJavaClass2DBType0(field.getType()) : column.type();
            columnType = columnType.toUpperCase();
            String columnFullType = handleColumnType0(columnName, columnType, column.length(), column.precision(), column.scale());


            /*处理列约束*/
            handleColumnConstraints(columnName, columnFullType, column);


            /*处理列额外信息*/
            handleColumnExtra(columnName, columnFullType, column);
        }

        /*方法处理*/
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            // 未配置Column注解的方法，直接跳过
            if (!method.isAnnotationPresent(Column.class)) {
                continue;
            }

            Column column = method.getAnnotation(Column.class);

            /* 处理列名 */
            String columnName = (null == column.name() || column.name().trim().length() == 0) ? transformMethodName(method.getName()) : column.name();
            // 验证列名
            verifyColumnName(columnName);
            // 初始化列名信息
            Map<String, String> columnInfo = new HashMap<>();
            columns.put(columnName, columnInfo);

            handleColumnName(columnName);

            /*处理列类型*/
            String columnType = (null == column.type() || column.type().trim().length() == 0) ? handleJavaClass2DBType0(method.getReturnType()) : column.type();
            columnType = columnType.toUpperCase();
            String columnFullType = handleColumnType0(columnName, columnType, column.length(), column.precision(), column.scale());


            /*处理列约束*/
            handleColumnConstraints(columnName, columnFullType, column);


            /*处理列额外信息*/
            handleColumnExtra(columnName, columnFullType, column);
        }

        StringBuilder columnsSb = new StringBuilder();
        for (Map<String, String> columnInfo : columns.values()) {
            String columnSQLTemp = defineColumnSQLTemplate();
            String columnSQL = parseSQLTemplate(columnSQLTemp, columnInfo);
            columnsSb.append(",").append(columnSQL);
        }
        String columnsSQL = columnsSb.substring(1);

        String tableSQLTemp = defineTableSQLTemplate();
        Map<String, String> tableMap = new HashMap<>();
        tableMap.put("table", table);
        tableMap.put("comment", tableComment);
        tableMap.put("columns", columnsSQL);

        handleTableExtra(table, t);
        tableMap.putAll(tableExtra);

        return parseSQLTemplate(tableSQLTemp, tableMap);
    }

    /**
     * 生成列定义
     *
     * @param columnName
     * @param column
     * @return
     */
    private void handleColumnConstraints(String columnName, String columnFullType, Column column) {
        // 主键操作
        handleColumnPrimaryKey(columnName, column.primaryKey());
        // 是否空值
        handleColumnNullable(columnName, column.nullable());
        // 是否自增
        handleColumnAutoIncrement(columnName, column.autoIncrement());
        // 默认值配置
        handleColumnDefaultValue(columnName, columnFullType, column.defaultValue());
        // 列注释
        handleColumnComment(columnName, column.comment());
    }

    protected void handleColumnExtra(String columnName, String columnFullType, Column column) {
        // Empty
    }

    protected String transformClassName(String clazzName) {
        return CamelCaseUtils.camelCase2underscore(clazzName);
    }

    protected String handleTableName(String tableName, String defaultTableName) {
        // 若未配置表名，获取实体名，对实体名进行驼峰转换
        if (null == tableName || tableName.trim().length() == 0) {
            tableName = defaultTableName;
        }
        log.debug("表名：{}", tableName);
        return tableName;
    }

    protected String handleTableComment(String comment) {
        log.debug("表的注释：{}", comment);
        return "";
    }

    protected void handleTableExtra(String tableName, Table t) {
        // Empty
    }

    /**
     * 转换属性的名称成字段，默认为驼峰转换
     *
     * @param fieldName
     * @return
     */
    protected String transformFieldName(String fieldName) {
        return CamelCaseUtils.camelCase2underscore(fieldName);
    }

    /**
     * 转换方法的名称成字段，默认为 get 驼峰转换
     *
     * @param methodName
     * @return
     */
    protected String transformMethodName(String methodName) {
        String str = methodName;
        if (str.startsWith("get")) {
            str = str.substring(3);
            str = Character.toLowerCase(str.charAt(0)) + str.substring(1);
        }
        return CamelCaseUtils.camelCase2underscore(str);
    }

    private void verifyColumnName(String columnName) {
        // 判断列名是否重复
        if (columns.containsKey(columnName)) {
            throw new RuntimeException(LogFormat.format("列名[{}]重复", columnName));
        }
    }

    protected String handleColumnName(String columnName) {
        putParam(columns.get(columnName), "columnName", columnName);
        return columnName;
    }

    protected String handleJavaClass2DBType0(Class javaClass) {
        Class jc = javaClass;
        if (javaClass == int.class) {
            jc = Integer.class;
        } else if (javaClass == boolean.class) {
            jc = Boolean.class;
        } else if (javaClass == char.class) {
            jc = Character.class;
        } else if (javaClass == long.class) {
            jc = Long.class;
        } else if (javaClass == float.class) {
            jc = Float.class;
        } else if (javaClass == double.class) {
            jc = Double.class;
        }
        return handleJavaClass2DBType(jc);
    }

    protected abstract String handleJavaClass2DBType(Class javaClass);

    protected String handleColumnType0(String columnName, String columnType, int length, int precision, int scale) {
        putParam(columns.get(columnName), "columnType", columnType);
        String columnFullType = handleColumnType(columnName, columnType, length, precision, scale);
        putParam(columns.get(columnName), "columnFullType", columnFullType);
        return columnFullType;
    }

    protected abstract String handleColumnType(String columnName, String columnType, int length, int precision, int scale);

    protected String handleColumnPrimaryKey(String columnName, boolean primaryKey) {
        String ret = "";
        if (primaryKey) {
            pkKeys.add(columnName);

            ret = "PRIMARY KEY";
        }
        putParam(columns.get(columnName), "columnPrimaryKey", ret);
        putParam(columns.get(columnName), "columnPK", ret);
        return ret;
    }

    protected String handleColumnNullable(String columnName, boolean nullable) {
        String ret;
        if (!nullable) {
            ret = "NOT NULL";
        } else {
            ret = "NULL";
        }

        putParam(columns.get(columnName), "columnNullable", ret);
        putParam(columns.get(columnName), "columnNull", ret);

        return ret;
    }

    protected String handleColumnAutoIncrement(String columnName, boolean autoIncrement) {
        String ret;
        if (autoIncrement) {
            ret = "AUTO_INCREMENT";
        } else {
            ret = "";
        }
        putParam(columns.get(columnName), "columnAutoIncrement", ret);
        return ret;
    }

    /**
     * 列默认值，可能是常量也可能是函数
     *
     * @param columnName
     * @param columnFullType
     * @param defaultValue
     * @return
     */
    protected String handleColumnDefaultValue(String columnName, String columnFullType, String defaultValue) {
        String str;
        if (null == defaultValue || defaultValue.trim().length() == 0) {
            str = "";
        } else {
            // TODO:如何判断是否是函数
            boolean isNumeric = defaultValue.matches("^[+-]{0,1}(([0-9]([0-9]*|[\\.][0-9]+))|([\\.][0-9]+))$");
            str = "DEFAULT " + (isNumeric ? defaultValue : "'" + defaultValue + "'");
        }
        putParam(columns.get(columnName), "columnDefaultValue", str);
        return str;
    }

    protected String handleColumnComment(String columnName, String comment) {
        String str;
        if (null == comment || comment.trim().length() == 0) {
            str = "";
        } else {
            str = String.format("COMMENT '%s'", comment);
        }
        putParam(columns.get(columnName), "columnComment", str);
        return str;
    }

    protected void putParam(Map<String, String> map, String key, String value) {
        if (null == value) {
            value = "";
        }
        map.put(key, value);
        map.put(CamelCaseUtils.camelCase2underscore(key), value);
    }

    protected String defineColumnSQLTemplate() {
        // 创建表，列的定义如下：
        return "${column_name} ${column_full_type} ${column_nullable} ${column_primary_key} ${column_auto_increment} ${column_default_value} ${column_comment}";
    }

    protected String defineTableSQLTemplate() {
        String tableSQLTemplate = "CREATE TABLE ${table}(" +
                "${columns}" +
                ")";
        return tableSQLTemplate;
    }

    protected String parseSQLTemplate(String sqlTemplate, Map<String, String> params) {
        return PlaceholderResolver.getDefaultResolver().resolveByMap(sqlTemplate, params);
    }
}
