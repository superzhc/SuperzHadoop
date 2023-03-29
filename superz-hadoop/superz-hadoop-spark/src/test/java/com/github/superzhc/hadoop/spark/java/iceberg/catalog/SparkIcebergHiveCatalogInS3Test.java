package com.github.superzhc.hadoop.spark.java.iceberg.catalog;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/3/29 20:47
 */
public class SparkIcebergHiveCatalogInS3Test {
    SparkSession spark=null;

    @Before
    public void setUp() throws Exception{
        SparkConf sparkConf = new SparkConf()
                .set("spark.sql.sources.partitionOverwriteMode", "dynamic")
                .set("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
                .set("spark.sql.catalog.bigdata_hive", "org.apache.iceberg.spark.SparkCatalog")
                .set("spark.sql.catalog.bigdata_hive.type", "hive")
                .set("spark.sql.catalog.bigdata_hive.uri", "thrift://127.0.0.1:9083")
                .set("spark.sql.catalog.bigdata_hive.warehouse", "s3a://superz/finance")
                .set("spark.sql.catalog.bigdata_hive.hadoop.fs.s3a.endpoint", "127.0.0.1:9000")
                .set("spark.sql.catalog.bigdata_hive.hadoop.fs.s3a.access.key", "admin")
                .set("spark.sql.catalog.bigdata_hive.hadoop.fs.s3a.secret.key", "admin123456")
                .set("spark.sql.catalog.bigdata_hive.hadoop.fs.s3a.connection.ssl.enabled", "false")
                .set("spark.sql.catalog.bigdata_hive.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
                ;

        spark = SparkSession.builder()
                .appName("Spark Iceberg Usage HiveCatalog operate S3")
                .master("local")
                .config(sparkConf)
                .getOrCreate();
    }

    @After
    public void tearDown() throws Exception{
        if(null!=spark){
            spark.stop();
        }
    }

    @Test
    public void databases() {
        spark.sql("SHOW DATABASES IN bigdata_hive").show();
    }

    @Test
    public void tables() {
        spark.sql("SHOW TABLES IN bigdata_hive.finance").show();
    }
}
