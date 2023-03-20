package com.github.superzhc.hadoop.iceberg.project;

import com.github.superzhc.hadoop.iceberg.catalog.IcebergHadoopS3Catalog;
import com.github.superzhc.hadoop.iceberg.utils.SchemaUtils;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/21 1:17
 */
public class FlinkRsshubShopping {
    public static void main(String[] args) {
        Catalog catalog = new IcebergHadoopS3Catalog(
                "s3a://superz/flink/iceberg"
                , "http://127.0.0.1:9000"
                , "admin"
                , "admin123456")
                .catalog("hadoop");

        Map<String, String> fields = new HashMap<>();
        fields.put("title", "string");
        fields.put("description", "string");
        fields.put("guid", "string");
        fields.put("link", "string");
        fields.put("sourceType", "string");
        fields.put("syncDate", "timestamp");
        fields.put("rsshubKey", "string");
        fields.put("pubDate", "timestamp");

        Schema schema = SchemaUtils.create(fields);
        PartitionSpec spec = SchemaUtils.partition(schema, "rsshubKey", "months(pubDate)");
        TableIdentifier tableIdentifier = TableIdentifier.of("rsshub", "shopping");
        Table table = catalog.createTable(tableIdentifier, schema, spec);
    }
}
