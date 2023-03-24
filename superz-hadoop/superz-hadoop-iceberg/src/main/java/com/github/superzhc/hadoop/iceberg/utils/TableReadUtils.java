package com.github.superzhc.hadoop.iceberg.utils;

import org.apache.iceberg.Table;
import org.apache.iceberg.data.IcebergGenerics;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.expressions.Expression;
import org.apache.iceberg.io.CloseableIterable;
import org.apache.iceberg.types.Types;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/24 14:12
 **/
public class TableReadUtils {
    private static final Integer DEFAULT_LIMIT = -1;

    public static List<Map<String, Object>> read(Table table) {
        return read(table, null, null, DEFAULT_LIMIT);
    }

    public static List<Map<String, Object>> read(Table table, int limit) {
        return read(table, null, null, limit);
    }

    public static List<Map<String, Object>> read(Table table, String... fields) {
        return read(table, null, fields, DEFAULT_LIMIT);
    }

    public static List<Map<String, Object>> read(Table table, String[] fields, int limit) {
        return read(table, null, fields, limit);
    }

    public static List<Map<String, Object>> read(Table table, int limit, String... fields) {
        return read(table, null, fields, limit);
    }

    public static List<Map<String, Object>> read(Table table, Expression rowFilter) {
        return read(table, rowFilter, null, DEFAULT_LIMIT);
    }

    public static List<Map<String, Object>> read(Table table, Expression rowFilter, int limit) {
        return read(table, rowFilter, null, limit);
    }

    public static List<Map<String, Object>> read(Table table, Expression rowFilter, String... fields) {
        return read(table, rowFilter, fields, DEFAULT_LIMIT);
    }

    public static List<Map<String, Object>> read(Table table, Expression rowFilter, String[] fields, int limit) {
        IcebergGenerics.ScanBuilder scanBuilder = IcebergGenerics.read(table);

        if (null != rowFilter) {
            scanBuilder.where(rowFilter);
        }

        if (null != fields && fields.length > 0) {
            scanBuilder.select(fields);
        }

        CloseableIterable<Record> records = scanBuilder.build();
        return convert(records, limit);
    }

    public static List<Map<String, Object>> convert(CloseableIterable<Record> records) {
        return convert(records, -1);
    }

    public static List<Map<String, Object>> convert(CloseableIterable<Record> records, int limit) {
        List<Map<String, Object>> data = new ArrayList<>();

        int cursor = 0;
        for (Record record : records) {
            if (limit > 0 && limit <= cursor) break;

            Map<String, Object> item = new LinkedHashMap<>();

            List<Types.NestedField> fields = record.struct().fields();
            for (Types.NestedField field : fields) {
                String fieldName = field.name();
                item.put(fieldName, record.getField(fieldName));
            }

            data.add(item);
            cursor++;
        }
        return data;
    }
}
