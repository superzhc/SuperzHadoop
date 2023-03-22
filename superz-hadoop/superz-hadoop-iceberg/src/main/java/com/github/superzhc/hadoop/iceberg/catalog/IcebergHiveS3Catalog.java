package com.github.superzhc.hadoop.iceberg.catalog;

import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.CatalogProperties;
import org.apache.iceberg.aws.AwsProperties;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.hive.HiveCatalog;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/22 17:49
 **/
public class IcebergHiveS3Catalog extends S3Catalog{
    private String uri;

    public IcebergHiveS3Catalog(String uri,String warehouse, String endpoint, String username, String password) {
        super(warehouse, endpoint, username, password);
        this.uri=uri;
    }

    public Catalog catalog(){
        return catalog("hive");
    }

    @Override
    public Catalog catalog(String name) {
        Map<String, String> properties = new HashMap<>();
        properties.put(CatalogProperties.CATALOG_IMPL, HiveCatalog.class.getName());
        properties.put(CatalogProperties.URI, uri);
        properties.put(CatalogProperties.WAREHOUSE_LOCATION, warehouse);
        properties.put(CatalogProperties.FILE_IO_IMPL, "org.apache.iceberg.aws.s3.S3FileIO");
        properties.put(AwsProperties.S3FILEIO_ENDPOINT, endpoint);
        properties.put(AwsProperties.S3FILEIO_ACCESS_KEY_ID, username);
        properties.put(AwsProperties.S3FILEIO_SECRET_ACCESS_KEY, password);

        // Hadoop Configuration
        Configuration conf = new Configuration();
        conf.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
        conf.set("fs.s3a.endpoint", endpoint);
        conf.set("fs.s3a.access.key", username);
        conf.set("fs.s3a.secret.key", password);
        conf.set("fs.s3a.connection.ssl.enabled", "false");
        conf.set("fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");

        HiveCatalog hiveCatalog = new HiveCatalog();
        hiveCatalog.setConf(conf);
        hiveCatalog.initialize(name, properties);
        return hiveCatalog;
    }
}
