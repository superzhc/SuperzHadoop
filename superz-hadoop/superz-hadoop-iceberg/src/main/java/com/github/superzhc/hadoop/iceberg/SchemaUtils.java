package com.github.superzhc.hadoop.iceberg;

import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.types.Type;
import org.apache.iceberg.types.Types;

import java.util.Map;

/**
 * @author superz
 * @create 2023/3/7 22:25
 */
public class SchemaUtils {
    /**
     * @param fields 例如：{"col1":"string","col2":"decimal(9,2)"}
     *
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


}
