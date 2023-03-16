package com.github.superzhc.hadoop.spark.java.iceberg;

import com.github.superzhc.data.other.AKTools;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/3/16 13:54
 **/
public class SparkIcebergQueryTest {
    SparkSession spark;

    @Before
    public void setUp() throws Exception {
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
                .set("spark.sql.catalog.test.warehouse", "s3a://superz/flink/iceberg")
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
    public void read(){
        Dataset<Row> ds=spark.table("test.test.t_202303161029");
        System.out.println(ds.count());
    }
}
