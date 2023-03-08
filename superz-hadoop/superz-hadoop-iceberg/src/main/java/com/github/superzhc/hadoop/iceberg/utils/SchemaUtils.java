package com.github.superzhc.hadoop.iceberg.utils;

import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.types.Type;
import org.apache.iceberg.types.Types;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2023/3/7 22:25
 */
public class SchemaUtils {
    private static final Pattern ICEBERG_PARTITION_TRANSFORM_EXPRESSION_PATTERN = Pattern.compile(
            "(years|months|days|date|hours|date_hour|bucket|truncate)[\\s]*\\(([\\s\\S]+),?([\\s\\S]*)\\)"
    );

    /**
     * @param fields 例如：{"col1":"string","col2":"decimal(9,2)"}
     * @return
     */
    public static Schema create(Map<String, String> fields) {
        Types.NestedField[] nestedFields = new Types.NestedField[fields.size()];
        int i = 0;
        for (Map.Entry<String, String> field : fields.entrySet()) {
            String name = field.getKey();
            Type type = Types.fromPrimitiveString(field.getValue());
            nestedFields[i] = Types.NestedField.optional(i + 1, name, type);
            i++;
        }

        Schema schema = new Schema(nestedFields);
        return schema;
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
