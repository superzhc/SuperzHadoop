package com.github.superzhc.hadoop.iceberg.project;

import com.github.superzhc.hadoop.iceberg.catalog.IcebergHiveS3Catalog;
import com.github.superzhc.hadoop.iceberg.utils.SchemaUtils;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.TableProperties;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.hive.HiveCatalog;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/24 0:59
 */
public class JavaDDLFinancialDatabase {

    public static void main(String[] args) {
        Catalog catalog = new IcebergHiveS3Catalog(
                "thrift://127.0.0.1:9083",
                "s3a://superz/project",
                "http://127.0.0.1:9000",
                "admin",
                "admin123456"
        ).catalog();

        HiveCatalog hiveCatalog = (HiveCatalog) catalog;

        createTable4IndexInfo(hiveCatalog);
    }

    public static Map<String, String> commonTableProperties() {
        Map<String, String> tableProperties = new HashMap<>();
        tableProperties.put(TableProperties.FORMAT_VERSION, "2");
        tableProperties.put(TableProperties.UPSERT_ENABLED, "true");
        tableProperties.put(TableProperties.METADATA_DELETE_AFTER_COMMIT_ENABLED, "true");
        tableProperties.put(TableProperties.METADATA_PREVIOUS_VERSIONS_MAX, "3");
        return tableProperties;
    }

    public static void createDatabase(HiveCatalog hiveCatalog) {
        hiveCatalog.createNamespace(Namespace.of("financial"), Collections.singletonMap("comment", "Financial management"));
    }

    // index_stock_info
    public static void createTable4IndexInfo(HiveCatalog hiveCatalog) {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("code", "string");
        fields.put("name", "string");
        fields.put("simple_number","int");
        fields.put("publish_date", "date");
        Schema schema = SchemaUtils.create(fields, fields.keySet().toArray(new String[fields.size()]));
        PartitionSpec spec = SchemaUtils.partition(schema);
        TableIdentifier tableIdentifier = TableIdentifier.of("financial", "index_info");
        if (hiveCatalog.tableExists(tableIdentifier)) {
            hiveCatalog.dropTable(tableIdentifier);
        }
        hiveCatalog.createTable(tableIdentifier, schema, spec, commonTableProperties());
    }

    // stock_zh_index_spot
    public static void createTable4IndexData(HiveCatalog hiveCatalog) {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("date", "timestamp");
        fields.put("code", "string");
        fields.put("name", "string");
        fields.put("new", "double");
        fields.put("change_amount", "double");
        fields.put("change", "double");
        fields.put("last_close", "double");
        fields.put("open", "double");
        fields.put("high", "double");
        fields.put("low", "double");
        fields.put("volume", "decimal(18, 2)");
        fields.put("amount", "decimal(18, 2)");
        Schema schema = SchemaUtils.create(fields, "date", "code");
        PartitionSpec spec = SchemaUtils.partition(schema, "code");
        TableIdentifier tableIdentifier = TableIdentifier.of("financial", "index_data");
        if (hiveCatalog.tableExists(tableIdentifier)) {
            hiveCatalog.dropTable(tableIdentifier);
        }
        hiveCatalog.createTable(tableIdentifier, schema, spec, commonTableProperties());
    }
}
