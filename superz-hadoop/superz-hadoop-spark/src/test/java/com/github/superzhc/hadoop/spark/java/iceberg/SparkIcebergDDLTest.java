package com.github.superzhc.hadoop.spark.java.iceberg;

import com.github.superzhc.data.other.AKTools;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/3/13 14:49
 **/
public class SparkIcebergDDLTest {
    AKTools akTools;

    SparkSession spark;

    @Before
    public void setUp() throws Exception {
        akTools = new AKTools("127.0.0.1");

        SparkConf conf = new SparkConf();
        conf.setAppName("Spark Iceberg Test")
                .setMaster("local")
                // s3 配置
                .set("spark.hadoop.fs.s3a.endpoint", "127.0.0.1:9000")
                .set("spark.hadoop.fs.s3a.access.key", "admin")
                .set("spark.hadoop.fs.s3a.secret.key", "admin123456")
                .set("spark.hadoop.fs.s3a.connection.ssl.enabled", "false")
                .set("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
                // iceberg 配置
                .set("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
                .set("spark.sql.catalog.test", "org.apache.iceberg.spark.SparkCatalog")
                .set("spark.sql.catalog.test.type", "hadoop")
                .set("spark.sql.catalog.test.warehouse", "s3a://superz/demo")
        ;
        spark = SparkSession.builder().config(conf).getOrCreate();
    }

    @After
    public void tearDown() throws Exception {
        if (null != spark) {
            spark.stop();
        }
    }

    @Test
    public void createDB() {
        spark.sql("CREATE DATABASE IF NOT EXISTS test.iceberg_db");
    }

    @Test
    public void createTable() {
        String sql = "CREATE TABLE test.spark.t1 (" +
                "    id bigint COMMENT 'unique id'," +
                "    data string," +
                "    category string" +
                ")" +
                "USING iceberg";
        spark.sql(sql);
    }

    @Test
    public void createTableWithPartition() {
        String sql = "CREATE TABLE test.spark.t2 (" +
                "    id bigint COMMENT 'unique id'," +
                "    data string," +
                "    category string," +
                "    ts timestamp" +
                ")" +
                "USING iceberg " +
                "PARTITIONED BY (bucket(16, id), days(ts), category)";
        spark.sql(sql);
    }

    @Test
    public void createOrReplaceTable() {
        String sql = "CREATE OR REPLACE TABLE test.spark.t3 " +
                "USING iceberg " +
                "AS SELECT * FROM test.spark.t1";
        spark.sql(sql);
        spark.table("test.spark.t3").show();

        sql = "CREATE OR REPLACE TABLE test.spark.t3 " +
                "USING iceberg " +
                "AS SELECT * FROM test.spark.t2";
        spark.sql(sql);
        spark.table("test.spark.t3").show();
    }
}
