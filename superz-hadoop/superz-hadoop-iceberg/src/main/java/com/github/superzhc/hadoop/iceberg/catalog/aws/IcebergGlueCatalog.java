package com.github.superzhc.hadoop.iceberg.catalog.aws;

import org.apache.iceberg.CatalogProperties;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.aws.AwsProperties;
import org.apache.iceberg.aws.glue.GlueCatalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.types.Types;

import java.util.HashMap;
import java.util.Map;

/**
 * 报错
 * @author superz
 * @create 2023/3/6 14:02
 **/
public class IcebergGlueCatalog {
    public static void main(String[] args) {
        Map<String, String> properties = new HashMap<>();
        properties.put(CatalogProperties.CATALOG_IMPL, "org.apache.iceberg.aws.glue.GlueCatalog");
//        properties.put(CatalogProperties.URI, "");
        properties.put(CatalogProperties.WAREHOUSE_LOCATION, "s3a://superz/iceberg_java");
        properties.put(CatalogProperties.FILE_IO_IMPL, "org.apache.iceberg.aws.s3.S3FileIO");
        properties.put(AwsProperties.S3FILEIO_ENDPOINT, "http://127.0.0.1:9000");
        properties.put(AwsProperties.S3FILEIO_ACCESS_KEY_ID, "admin");
        properties.put(AwsProperties.S3FILEIO_SECRET_ACCESS_KEY, "admin123456");

        GlueCatalog catalog = new GlueCatalog();
        catalog.initialize("demo", properties);

        // 定义Schema
        Schema schema = new Schema(
                Types.NestedField.required(1, "level", Types.StringType.get()),
                Types.NestedField.required(2, "event_time", Types.TimestampType.withZone()),
                Types.NestedField.required(3, "message", Types.StringType.get()),
                Types.NestedField.optional(4, "call_stack", Types.ListType.ofRequired(5, Types.StringType.get()))
        );

        // 定义分区
        PartitionSpec spec = PartitionSpec.builderFor(schema)
                .hour("event_time")
                .build();

        // 定义命名空间
        Namespace namespace = Namespace.of("glue");
        TableIdentifier name = TableIdentifier.of(namespace, "logs");

        // 创建表
        catalog.createTable(name, schema, spec);
    }
}