package com.github.superzhc.hadoop.iceberg;

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
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.hive.HiveCatalog;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/22 17:01
 **/
public class IcebergHiveCatalogTest {
    @Test
    public void catalog0() {
        Map<String, String> properties = new HashMap<>();
        properties.put(CatalogProperties.CATALOG_IMPL, HiveCatalog.class.getName());
        properties.put(CatalogProperties.URI, "thrift://10.90.15.233:9083");
        properties.put(CatalogProperties.WAREHOUSE_LOCATION, "s3a://superz/java/catalog/hive");
        properties.put(CatalogProperties.FILE_IO_IMPL, "org.apache.iceberg.aws.s3.S3FileIO");
        properties.put(AwsProperties.S3FILEIO_ENDPOINT, "http://127.0.0.1:9000");
        properties.put(AwsProperties.S3FILEIO_ACCESS_KEY_ID, "admin");
        properties.put(AwsProperties.S3FILEIO_SECRET_ACCESS_KEY, "admin123456");

        // Hadoop Configuration
        Configuration conf = new Configuration();
        conf.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
        conf.set("fs.s3a.endpoint", "http://127.0.0.1:9000");
        conf.set("fs.s3a.access.key", "admin");
        conf.set("fs.s3a.secret.key", "admin123456");
        conf.set("fs.s3a.connection.ssl.enabled", "false");
        conf.set("fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");

        HiveCatalog hiveCatalog = new HiveCatalog();
        hiveCatalog.setConf(conf);
        hiveCatalog.initialize("hive", properties);

        System.out.println(hiveCatalog.listTables(Namespace.of("xgit")));
    }

    @Test
    public void testHiveS3Catalog() {
        Catalog catalog = new IcebergHiveS3Catalog(
                "thrift://10.90.15.233:9083",
                "s3a://superz/java/catalog/hive",
                "http://127.0.0.1:9000",
                "admin",
                "admin123456"
        ).catalog();

        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("id", "int");
        fields.put("name", "string");
        Schema schema = SchemaUtils.create(fields, "id");

        PartitionSpec spec = SchemaUtils.partition(schema);

        TableIdentifier tableIdentifier = TableIdentifier.of("default","demo");
        if (catalog.tableExists(tableIdentifier)) {
            catalog.dropTable(tableIdentifier);
        }
        catalog.createTable(tableIdentifier, schema, spec);
    }
}
