package com.github.superzhc.hadoop.spark.java.iceberg;

import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.CatalogProperties;
import org.apache.iceberg.Table;
import org.apache.iceberg.aws.AwsProperties;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.spark.actions.SparkActions;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/22 10:14
 **/
public class SparkActionsTest {
    static {
        System.setProperty("aws.region", "us-east-1");
    }

    SparkSession spark;

    Catalog catalog;

    @Before
    public void setUp() throws Exception {
        String s3Endpoint = "http://127.0.0.1:9000";
        String s3AccessKey = "admin";
        String s3SecretKey = "admin123456";
        String warehouse = "s3a://superz/flink/iceberg";

        SparkConf conf = new SparkConf();
        conf.setAppName("Iceberg SparkActions Test")
                .setMaster("local")
                // s3 配置
                .set("spark.hadoop.fs.s3a.endpoint", s3Endpoint)
                .set("spark.hadoop.fs.s3a.access.key", s3AccessKey)
                .set("spark.hadoop.fs.s3a.secret.key", s3SecretKey)
                .set("spark.hadoop.fs.s3a.connection.ssl.enabled", "false")
                .set("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
                // iceberg 配置
                .set("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
                .set("spark.sql.catalog.test", "org.apache.iceberg.spark.SparkCatalog")
                .set("spark.sql.catalog.test.type", "hadoop")
                .set("spark.sql.catalog.test.warehouse", warehouse)
        ;
        spark = SparkSession.builder().config(conf).getOrCreate();

        Map<String, String> properties = new HashMap<>();
        properties.put(CatalogProperties.CATALOG_IMPL, "org.apache.iceberg.hadoop.HadoopCatalog");
        properties.put(CatalogProperties.WAREHOUSE_LOCATION, warehouse);
        properties.put(CatalogProperties.FILE_IO_IMPL, "org.apache.iceberg.aws.s3.S3FileIO");
        properties.put(AwsProperties.S3FILEIO_ENDPOINT, s3Endpoint);
        properties.put(AwsProperties.S3FILEIO_ACCESS_KEY_ID, s3AccessKey);
        properties.put(AwsProperties.S3FILEIO_SECRET_ACCESS_KEY, s3SecretKey);

        Configuration hadoopConf = new Configuration();
        hadoopConf.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
        hadoopConf.set("fs.s3a.endpoint", s3Endpoint);
        hadoopConf.set("fs.s3a.access.key", s3AccessKey);
        hadoopConf.set("fs.s3a.secret.key", s3SecretKey);
        hadoopConf.set("fs.s3a.connection.ssl.enabled", "false");
        hadoopConf.set("fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");

        HadoopCatalog hadoopCatalog = new HadoopCatalog();
        hadoopCatalog.setConf(hadoopConf);
        hadoopCatalog.initialize("hadoop", properties);
        catalog = hadoopCatalog;
    }

    @Test
    public void testExpireSnapshot() {
        TableIdentifier tableIdentifier = TableIdentifier.of("rsshub", "shopping");
        Table table = catalog.loadTable(tableIdentifier);

        /**
         * 删除历史快照,历史快照是通过ExpireSnapshot来实现的，设置需要删除多久的历史快照 snap-*.avro文件
         */

        SparkActions
                .get(spark)
                .expireSnapshots(table)
                .expireOlderThan(System.currentTimeMillis())// 当前时间>最新snapshot，保留最新的snapshot，其他都删除
                .execute();

        table.refresh();
    }

    @Test
    public void testRewriteDataFiles() {
        TableIdentifier tableIdentifier = TableIdentifier.of("rsshub", "shopping");
        Table table = catalog.loadTable(tableIdentifier);

        /**
         * 合并小文件数据，Iceberg合并小文件时并不会删除被合并的文件，合并是将小文件合并成大文件并创建新的Snapshot。
         * target-file-size-bytes 指定合并后文件的大小
         */

        SparkActions
                .get(spark)
                .rewriteDataFiles(table)
                .option("target-file-size-bytes", Long.toString(5 * 1024 * 1024)) // 5 MB
                .execute();
    }

//    @Test
//    public void testRewriteManifests(){
//        TableIdentifier tableIdentifier = TableIdentifier.of("rsshub", "shopping");
//        Table table = catalog.loadTable(tableIdentifier);
//
//        SparkActions
//                .get(spark)
//                .rewriteManifests(table)
//                .rewriteIf(file -> file.length() < 10 * 1024 * 1024) // 10 MB
//                .execute();
//    }
}
