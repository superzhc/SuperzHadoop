package com.github.superzhc.hadoop.iceberg.project;

import com.github.superzhc.hadoop.iceberg.catalog.IcebergHadoopS3Catalog;
import com.github.superzhc.hadoop.iceberg.utils.SchemaUtils;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.TableProperties;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/22 16:34
 **/
public class JavaDDLFundETFHistMinEM {
    public static void main(String[] args) {
        Catalog catalog = new IcebergHadoopS3Catalog(
                "s3a://superz/xgit",
                "http://127.0.0.1:9000",
                "admin",
                "admin123456"
        ).catalog();

        Map<String, String> tableProperties = new HashMap<>();
        tableProperties.put(TableProperties.FORMAT_VERSION, "2");
        tableProperties.put(TableProperties.UPSERT_ENABLED, "true");
        tableProperties.put(TableProperties.METADATA_DELETE_AFTER_COMMIT_ENABLED, "true");
        tableProperties.put(TableProperties.METADATA_PREVIOUS_VERSIONS_MAX, "3");

        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("time", "long");
        fields.put("code", "string");
        fields.put("volume", "double");
        fields.put("account", "double");
        Schema schema = SchemaUtils.create(fields, "time", "code");

        PartitionSpec partitionSpec = SchemaUtils.partition(schema, "code");

        TableIdentifier tableIdentifier = TableIdentifier.of("influxdb", "meta_fund_etf_hist_min_em");
        if (catalog.tableExists(tableIdentifier)) {
            catalog.dropTable(tableIdentifier);
        }
        catalog.createTable(tableIdentifier, schema, partitionSpec, tableProperties);
    }
}
