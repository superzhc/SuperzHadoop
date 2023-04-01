package com.github.superzhc.finance.maintenance;

import org.apache.iceberg.Table;
import org.apache.iceberg.spark.Spark3Util;
import org.apache.iceberg.spark.actions.SparkActions;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/4/1 15:19
 **/
public class FinanceSparkAction {
    SparkSession spark = null;

    @Before
    public void setUp() throws Exception {
        SparkConf sparkConf = new SparkConf()
                .set("spark.sql.legacy.timeParserPolicy", "LEGACY")
                .set("spark.sql.sources.partitionOverwriteMode", "dynamic")
                .set("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
                .set("spark.sql.iceberg.check-nullability", "false")
                .set("spark.sql.catalog.bigdata_hive", "org.apache.iceberg.spark.SparkCatalog")
                .set("spark.sql.catalog.bigdata_hive.type", "hive")
                .set("spark.sql.catalog.bigdata_hive.uri", "thrift://127.0.0.1:9083")
                .set("spark.sql.catalog.bigdata_hive.warehouse", "s3a://superz/finance")
                .set("spark.sql.catalog.bigdata_hive.hadoop.fs.s3a.endpoint", "127.0.0.1:9000")
                .set("spark.sql.catalog.bigdata_hive.hadoop.fs.s3a.access.key", "admin")
                .set("spark.sql.catalog.bigdata_hive.hadoop.fs.s3a.secret.key", "admin123456")
                .set("spark.sql.catalog.bigdata_hive.hadoop.fs.s3a.connection.ssl.enabled", "false")
                .set("spark.sql.catalog.bigdata_hive.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");

        spark = SparkSession.builder()
                .appName("Spark Maintenace Iceberg")
                .master("local")
                .config(sparkConf)
                .getOrCreate();
    }

    @After
    public void tearDown() throws Exception {
        if (null != spark) {
            spark.stop();
        }
    }

    @Test
    public void compactDataFiles() throws Exception {
        Table table = Spark3Util.loadIcebergTable(spark, "bigdata_hive.finance.index_one_minute_info");
        SparkActions.get(spark)
                .rewriteDataFiles(table)
                .option("target-file-size-bytes", Long.toString(10 * 1024 * 1024))
                .execute();
    }

    @Test
    public void rewriteManifests() throws Exception {
        Table table = Spark3Util.loadIcebergTable(spark, "bigdata_hive.finance.index_one_minute_info");
        SparkActions.get(spark)
                .rewriteManifests(table)
                .rewriteIf(file -> file.length() < 1 * 1024 * 1024)
                .execute();
    }

    @Test
    public void deleteOrphanFiles() throws Exception{
        Table table = Spark3Util.loadIcebergTable(spark, "bigdata_hive.finance.index_one_minute_info");
        SparkActions
                .get(spark)
                .deleteOrphanFiles(table)
                .execute();
    }
}
