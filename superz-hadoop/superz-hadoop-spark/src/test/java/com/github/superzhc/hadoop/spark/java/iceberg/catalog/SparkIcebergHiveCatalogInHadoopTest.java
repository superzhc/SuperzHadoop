package com.github.superzhc.hadoop.spark.java.iceberg.catalog;

import org.apache.iceberg.CachingCatalog;
import org.apache.iceberg.spark.Spark3Util;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalog.Catalog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/3/25 14:20
 **/
public class SparkIcebergHiveCatalogInHadoopTest {
    static {
        // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
        System.setProperty("HADOOP_USER_NAME", "root");
        // System.setProperty("HADOOP_HOME","D:\\\\soft\\hadoop");
    }

    SparkSession spark = null;

    @Before
    public void setUp() {
        SparkConf sparkConf = new SparkConf()
                .set("spark.sql.sources.partitionOverwriteMode", "dynamic")
                .set("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
                .set("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkSessionCatalog")
                .set("spark.sql.catalog.spark_catalog.type", "hive")
                .set("spark.sql.catalog.bigdata_hive", "org.apache.iceberg.spark.SparkCatalog")
                .set("spark.sql.catalog.bigdata_hive.type", "hive")
                .set("spark.sql.catalog.bigdata_hive.uri", "thrift://10.90.15.221:9083,thrift://10.90.15.233:9083")
                .set("spark.sql.catalog.bigdata_hive.warehouse", "hdfs://xgitbigdata/usr/xgit/hive/warehouse");

        spark = SparkSession.builder()
                .appName("Spark Iceberg Usage HiveCatalog operate Hadoop")
                .master("local")
                .config(sparkConf)
                .getOrCreate();
    }

    @After
    public void tearDown() {
        spark.stop();
    }

    @Test
    public void catalog() {
        CachingCatalog catalog = (CachingCatalog) Spark3Util.loadIcebergCatalog(spark, "bigdata_hive");
    }

    @Test
    public void catalog2() {
        Catalog catalog = spark.catalog();
    }

    @Test
    public void databases() {
        spark.sql("SHOW DATABASES IN bigdata_hive").show();
    }

    @Test
    // 等同databases
    public void namespaces() {
        spark.sql("SHOW NAMESPACES IN bigdata_hive").show();
    }

    @Test
    public void createDatabase() {
        String sql = "CREATE DATABASE bigdata_hive.spark_iceberg_superz2";
        spark.sql(sql);
    }

    @Test
    public void dropDatabase() {
        String sql = "DROP DATABASE IF EXISTS bigdata_hive.spark_iceberg_superz2";
        spark.sql(sql);
    }

    @Test
    public void tables() {
        spark.sql("SHOW TABLES IN bigdata_hive.spark_iceberg_superz").show();
    }

    @Test
    public void table() {
        Dataset<Row> ds = null;

        ds = spark.sql("SELECT * FROM bigdata_hive.influxdb_superz.device_daily_data");

        ds.show(1000);
    }

    @Test
    public void snapshots() {
        Dataset<Row> ds = spark.sql("SELECT * FROM bigdata_hive.influxdb_superz.device_daily_data.snapshots");
        ds.show();
    }

    public void history() {
        Dataset<Row> ds = spark.sql("SELECT * FROM bigdata_hive.influxdb_superz.device_daily_data.history");
        ds.show();
    }

    public void files() {
        Dataset<Row> ds = spark.sql("SELECT * FROM bigdata_hive.influxdb_superz.device_daily_data.files");
        ds.show();
    }

    public void manifests() {
        Dataset<Row> ds = spark.sql("SELECT * FROM bigdata_hive.influxdb_superz.device_daily_data.manifests");
        ds.show();
    }

    @Test
    public void createTable() {
        String sql = "CREATE TABLE bigdata_hive.`influxdb_superz`.t_202303281427 (id bigint,data string) USING iceberg";
        spark.sql(sql);
    }
}
