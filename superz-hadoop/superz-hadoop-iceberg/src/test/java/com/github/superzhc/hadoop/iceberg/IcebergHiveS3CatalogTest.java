package com.github.superzhc.hadoop.iceberg;

import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.hadoop.iceberg.catalog.IcebergHiveS3Catalog;
import com.github.superzhc.hadoop.iceberg.utils.SchemaUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.CatalogProperties;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.aws.AwsProperties;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.hive.HiveCatalog;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/23 15:38
 **/
public class IcebergHiveS3CatalogTest {
    @Test
    public void catalog0() {
        System.setProperty("aws.region", "us-east-1");

        Map<String, String> properties = new HashMap<>();
        properties.put(CatalogProperties.CATALOG_IMPL, HiveCatalog.class.getName());
        properties.put(CatalogProperties.URI, "thrift://127.0.0.1:9083");
        properties.put(CatalogProperties.WAREHOUSE_LOCATION, "s3a://superz/java/catalog/hive");
        properties.put(CatalogProperties.FILE_IO_IMPL, "org.apache.iceberg.aws.s3.S3FileIO");
        properties.put(AwsProperties.S3FILEIO_ENDPOINT, "http://127.0.0.1:9000");
        properties.put(AwsProperties.S3FILEIO_ACCESS_KEY_ID, "admin");
        properties.put(AwsProperties.S3FILEIO_SECRET_ACCESS_KEY, "admin123456");

        // Hadoop Configuration
        Configuration conf = new Configuration();
        conf.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
        conf.set("fs.s3a.endpoint", "http://127.0.0.1:9000");
        //fs.s3a.awsAccessKeyId
        conf.set("fs.s3a.access.key", "admin");
        //fs.s3a.awsSecretAccessKey
        conf.set("fs.s3a.secret.key", "admin123456");
        conf.set("fs.s3a.connection.ssl.enabled", "false");
        conf.set("fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");
        //conf.set("fs.s3a.region", "us-east-1");
        conf.set("fs.s3a.path.style.access", "true");

        HiveCatalog hiveCatalog = new HiveCatalog();
        hiveCatalog.setConf(conf);
        hiveCatalog.initialize("hive", properties);

        // hiveCatalog.listNamespaces().stream().forEach(System.out::println);

        MapUtils.show(hiveCatalog.loadNamespaceMetadata(Namespace.of("xgit")));
    }

    @Test
    public void testHiveS3Catalog() {
        Catalog catalog = new IcebergHiveS3Catalog(
                "thrift://127.0.0.1:9083",
                "s3a://superz/java/catalog/hive",
                "http://127.0.0.1:9000",
                "admin",
                "admin123456"
        ).catalog();

        HiveCatalog hiveCatalog = (HiveCatalog) catalog;

        // show databases
        hiveCatalog.listNamespaces().stream().forEach(System.out::println);

        // 创建库
//        hiveCatalog.createNamespace(Namespace.of("xgit"));

        // 创建表
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("id", "int");
        fields.put("name", "string");
        Schema schema = SchemaUtils.create(fields, "id");

        PartitionSpec spec = SchemaUtils.partition(schema);

        TableIdentifier tableIdentifier = TableIdentifier.of("xgit", "demo4");
        if (catalog.tableExists(tableIdentifier)) {
            catalog.dropTable(tableIdentifier);
        }
        hiveCatalog.createTable(tableIdentifier, schema, spec);
    }
}
