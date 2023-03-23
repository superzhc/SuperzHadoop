package com.github.superzhc.hadoop.iceberg.catalog;

import com.github.superzhc.common.utils.PathUtils;
import org.apache.iceberg.CatalogProperties;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.aws.AwsProperties;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.jdbc.JdbcCatalog;
import org.apache.iceberg.types.Types;

import java.util.HashMap;
import java.util.Map;

/**
 * MySQL 报错
 * 1.1.0 iceberg 报错误：Specified key was too long
 * 组合索引的大小超过了 3072，见 1.1.0 以上版本合并了一个pull request，见<a>https://github.com/apache/iceberg/pull/6338</a>
 * mysql 组合索引最大为 3072，无法再调整，只能修改代码
 * <p>
 * SQLite 运行没问题
 *
 * @author superz
 * @create 2023/3/6 10:35
 **/
public class IcebergJdbcS3Catalog extends IcebergS3Catalog {
    private String jdbcUrl;

    private String jdbcUsername;

    private String jdbcPassword;

    public IcebergJdbcS3Catalog(String warehouse, String endpoint, String username, String password, String jdbcUrl) {
        this(warehouse, endpoint, username, password, jdbcUrl, null, null);
    }

    public IcebergJdbcS3Catalog(String warehouse, String endpoint, String username, String password, String jdbcUrl, String jdbcUsername, String jdbcPassword) {
        super(warehouse, endpoint, username, password);

        this.jdbcUrl = jdbcUrl;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    @Override
    public Catalog catalog() {
        return catalog("jdbc");
    }

    @Override
    public Catalog catalog(String name) {
        Map<String, String> properties = new HashMap<>();
        properties.put(CatalogProperties.CATALOG_IMPL, "org.apache.iceberg.jdbc.JdbcCatalog");
        properties.put(CatalogProperties.URI, jdbcUrl);
        properties.put("jdbc.useSSL", "false");
        if (null != jdbcUrl && null != jdbcPassword) {
            properties.put("jdbc.user", jdbcUsername);
            properties.put("jdbc.password", jdbcPassword);
        }
        properties.put(CatalogProperties.WAREHOUSE_LOCATION, warehouse/*"s3a://superz/iceberg_java"*/);
        properties.put(CatalogProperties.FILE_IO_IMPL, "org.apache.iceberg.aws.s3.S3FileIO");
        properties.put(AwsProperties.S3FILEIO_ENDPOINT, endpoint/*"http://127.0.0.1:9000"*/);
        properties.put(AwsProperties.S3FILEIO_ACCESS_KEY_ID, username/*"admin"*/);
        properties.put(AwsProperties.S3FILEIO_SECRET_ACCESS_KEY, password/*"admin123456"*/);

        JdbcCatalog catalog = new JdbcCatalog();
        catalog.initialize(name, properties);
        return catalog;
    }

    public static void main(String[] args) {
        // 系统参数设置区域
        System.setProperty("aws.region", "us-east-1");

        Map<String, String> properties = new HashMap<>();
        properties.put(CatalogProperties.CATALOG_IMPL, "org.apache.iceberg.jdbc.JdbcCatalog");
//        properties.put(CatalogProperties.URI, "jdbc:mysql://127.0.0.1:3306/iceberg");
//        properties.put("jdbc.useSSL","false");
//        properties.put("jdbc.user","root");
//        properties.put("jdbc.password","123456");

        properties.put(CatalogProperties.URI, String.format("jdbc:sqlite:%s/%s", PathUtils.project(), "superz-hadoop/superz-hadoop-iceberg/db/iceberg.db"));
        properties.put(CatalogProperties.WAREHOUSE_LOCATION, "s3a://superz/iceberg_java");
        properties.put(CatalogProperties.FILE_IO_IMPL, "org.apache.iceberg.aws.s3.S3FileIO");
        properties.put(AwsProperties.S3FILEIO_ENDPOINT, "http://127.0.0.1:9000");
        properties.put(AwsProperties.S3FILEIO_ACCESS_KEY_ID, "admin");
        properties.put(AwsProperties.S3FILEIO_SECRET_ACCESS_KEY, "admin123456");

        JdbcCatalog catalog = new JdbcCatalog();
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
        Namespace namespace = Namespace.of("jdbc");
        TableIdentifier name = TableIdentifier.of(namespace, "logs");

        // 创建表
        catalog.createTable(name, schema, spec);
    }
}
