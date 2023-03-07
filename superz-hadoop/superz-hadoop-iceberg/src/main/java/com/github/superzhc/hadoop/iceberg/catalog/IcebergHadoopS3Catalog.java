package com.github.superzhc.hadoop.iceberg.catalog;

import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.CatalogProperties;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.aws.AwsProperties;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.types.Types;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/6 18:04
 **/
public class IcebergHadoopS3Catalog extends S3Catalog {


    public IcebergHadoopS3Catalog(String warehouse, String endpoint, String username, String password) {
        super(warehouse, endpoint, username, password);
    }

    @Override
    public Catalog catalog(String name) {
        Map<String, String> properties = new HashMap<>();
        properties.put(CatalogProperties.CATALOG_IMPL, HadoopCatalog.class.getName()/*"org.apache.iceberg.hadoop.HadoopCatalog"*/);
        properties.put(CatalogProperties.WAREHOUSE_LOCATION, warehouse/*"s3a://superz/iceberg_java"*/);
        properties.put(CatalogProperties.FILE_IO_IMPL, "org.apache.iceberg.aws.s3.S3FileIO");
        properties.put(AwsProperties.S3FILEIO_ENDPOINT, endpoint/*"http://127.0.0.1:9000"*/);
        properties.put(AwsProperties.S3FILEIO_ACCESS_KEY_ID, username/*"admin"*/);
        properties.put(AwsProperties.S3FILEIO_SECRET_ACCESS_KEY, password/*"admin123456"*/);

        Configuration conf = new Configuration();
        conf.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
        conf.set("fs.s3a.endpoint", endpoint/*"http://127.0.0.1:9000"*/);
        conf.set("fs.s3a.access.key", username/*"admin"*/);
        conf.set("fs.s3a.secret.key", password/*"admin123456"*/);
        conf.set("fs.s3a.connection.ssl.enabled", "false");
        conf.set("fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");

        HadoopCatalog catalog = new HadoopCatalog();
        catalog.setConf(conf);
        catalog.initialize(name, properties);
        return catalog;
    }

    public static void main(String[] args) {
        // 系统参数设置区域，不能使用ap-east-1、cn-north-1，会报错，使用默认region不报错
        System.setProperty("aws.region", "us-east-1");

        Map<String, String> properties = new HashMap<>();
        properties.put(CatalogProperties.CATALOG_IMPL, "org.apache.iceberg.hadoop.HadoopCatalog");
        properties.put(CatalogProperties.WAREHOUSE_LOCATION, "s3a://superz/iceberg_java");
        properties.put(CatalogProperties.FILE_IO_IMPL, "org.apache.iceberg.aws.s3.S3FileIO");
        properties.put(AwsProperties.S3FILEIO_ENDPOINT, "http://127.0.0.1:9000");
        properties.put(AwsProperties.S3FILEIO_ACCESS_KEY_ID, "admin");
        properties.put(AwsProperties.S3FILEIO_SECRET_ACCESS_KEY, "admin123456");

        Configuration conf = new Configuration();
        conf.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
        conf.set("fs.s3a.endpoint", "http://127.0.0.1:9000");
        conf.set("fs.s3a.access.key", "admin");
        conf.set("fs.s3a.secret.key", "admin123456");
        conf.set("fs.s3a.connection.ssl.enabled", "false");
        conf.set("fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");

        HadoopCatalog catalog = new HadoopCatalog();
        catalog.setConf(conf);
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
        Namespace namespace = Namespace.of("hadoop");
        TableIdentifier name = TableIdentifier.of(namespace, "logs");

        // 创建表
        catalog.createTable(name, schema, spec);
    }
}
