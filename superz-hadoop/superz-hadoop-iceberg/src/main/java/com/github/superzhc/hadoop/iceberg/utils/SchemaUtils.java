package com.github.superzhc.hadoop.iceberg.utils;

import org.apache.iceberg.*;
import org.apache.iceberg.types.Type;
import org.apache.iceberg.types.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2023/3/7 22:25
 */
public class SchemaUtils {
    private static final Logger LOG = LoggerFactory.getLogger(SchemaUtils.class);
    private static final Pattern DECIMAL = Pattern.compile("decimal\\((\\d+),\\s*(\\d+)\\)");
    private static final Pattern ICEBERG_PARTITION_TRANSFORM_EXPRESSION_PATTERN = Pattern.compile(
            "(years|months|days|date|hours|date_hour|bucket|truncate)[\\s]*\\(([\\s\\S]+),?([\\s\\S]*)\\)"
    );

    public static Type.PrimitiveType fromPrimitiveStringEnhance(String typeString) {
        // 自动去除掉前后端的空格，更简便
        String lowerTypeString = typeString.trim().toLowerCase(Locale.ROOT);

        // iceberg对类型的定义有限，做出部分扩展
        if ("bigint".equals(lowerTypeString)) {
            return Types.fromPrimitiveString("long");
        } else if (lowerTypeString.startsWith("varchar")
                || lowerTypeString.startsWith("char")) {
            return Types.fromPrimitiveString("string");
        }

        // iceberg对 decimal(18,2)不支持，因18,2中间少了个空格，这明显是不合理的
        Matcher decimal = DECIMAL.matcher(lowerTypeString);
        if (decimal.matches()) {
            return Types.DecimalType.of(Integer.parseInt(decimal.group(1)), Integer.parseInt(decimal.group(2)));
        }

        return Types.fromPrimitiveString(lowerTypeString);
    }

    /**
     * @param fields 例如：{"col1":"string","col2":"decimal(9,2)"}
     * @return
     */
    public static Schema create(Map<String, String> fields, String... requiredFields) {
        Types.NestedField[] nestedFields = new Types.NestedField[fields.size()];
        int i = 0;
        for (Map.Entry<String, String> field : fields.entrySet()) {
            String name = field.getKey();
            Type type = fromPrimitiveStringEnhance(field.getValue());
            nestedFields[i] =
                    contain(requiredFields, name)
                            ?
                            Types.NestedField.required(i + 1, name, type)
                            :
                            Types.NestedField.optional(i + 1, name, type);
            i++;
        }

        Schema schema = new Schema(nestedFields);
        return schema;
    }

    private static boolean contain(String[] array, String item) {
        if (null == array || array.length == 0) {
            return false;
        }

        for (String str : array) {
            if (item.equalsIgnoreCase(str)) {
                return true;
            }
        }

        return false;
    }

    public static void addColumn(Table table, String field, String type) {
        addColumn(table, field, type, null);
    }

    public static void addColumn(Table table, String field, String type, String comment) {
        UpdateSchema update = table.updateSchema();

        Type typeObj = fromPrimitiveStringEnhance(type);
        update.addColumn(field, typeObj, comment);

        update.commit();
    }

    public static void deleteColumn(Table table, String field) {
        UpdateSchema update = table.updateSchema();

        update.deleteColumn(field);

        update.commit();
    }

    public static void renameColumn(Table table, String oldName, String newName) {
        UpdateSchema update = table.updateSchema();

        update.renameColumn(oldName, newName);

        update.commit();
    }

    public static void updateColumn(Table table, String field, String type) {
        updateColumn(table, field, type, null);
    }

    /**
     * 更新类型，注意只允许比较广泛的类型可修改，限制比较多
     *
     * @param table
     * @param field
     * @param type
     * @param comment
     */
    public static void updateColumn(Table table, String field, String type, String comment) {
        UpdateSchema update = table.updateSchema();

        if (null != type) {
            Type.PrimitiveType typeObj = fromPrimitiveStringEnhance(type);
            update.updateColumn(field, typeObj);
        }

        if (null != comment) {
            update.updateColumnDoc(field, comment);
        }

        update.commit();
    }

    public static PartitionSpec partition(Map<String, String> fields, String... partitionFields) {
        return partition(create(fields), partitionFields);
    }

    /**
     * @param schema
     * @param fields 支持 transform expressions，其格式同 spark iceberg ddl
     * @return
     */
    public static PartitionSpec partition(Schema schema, String... fields) {
        PartitionSpec.Builder builder = PartitionSpec.builderFor(schema);
        if (null == fields || fields.length == 0) {
            return builder.build();
        }

        for (String field : fields) {
            if (null == field || field.trim().length() == 0) {
                continue;
            }

            Matcher matcher = ICEBERG_PARTITION_TRANSFORM_EXPRESSION_PATTERN.matcher(field);
            if (!matcher.find()) {
                builder.identity(field.trim());
            } else {
                String type = matcher.group(1).trim();
                switch (type) {
                    case "years":
                        builder.year(matcher.group(2));
                        break;
                    case "months":
                        builder.month(matcher.group(2));
                        break;
                    case "days":
                    case "date":
                        builder.day(matcher.group(2));
                        break;
                    case "hours":
                    case "date_hour":
                        builder.hour(matcher.group(2));
                        break;
                    case "bucket":
                        builder.bucket(matcher.group(3), Integer.valueOf(matcher.group(2)));
                        break;
                    case "truncate":
                        builder.truncate(matcher.group(3), Integer.valueOf(matcher.group(2)));
                        break;
                }
            }
        }

        return builder.build();
    }

    private static void regex(String str) {
        if (null == str) {
            return;
        }

        Matcher matcher = ICEBERG_PARTITION_TRANSFORM_EXPRESSION_PATTERN.matcher(str);
        if (!matcher.find()) {
            System.out.println(str.trim());
        } else {
            int count = matcher.groupCount();
            String type = matcher.group(1).trim();
            String[] params = new String[count - 1];
            for (int i = 2; i <= count; i++) {
                params[i - 2] = matcher.group(i).trim();
            }
            System.out.printf("[%s] type:[%s];params:[%s]\n", str, type, String.join(",", params));
        }
    }

    public static void main(String[] args) {
        regex("level");
        regex(" level ");
        regex("years(ts)");
        regex("months(ts)");
        regex("months( ts )");
        regex("days(ts)");
        regex("days(ts )");
        regex("date(ts)");
        regex("date ( ts )");
        regex("hours(ts)");
        regex("date_hour(ts)");
        regex("bucket(10, col)");
        regex("truncate(10, col)");
    }

}
