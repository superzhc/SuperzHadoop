package com.github.superzhc.hadoop.spark.java.project;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * @author superz
 * @create 2023/3/21 13:33
 **/
public class SparkRsshubShopping {
    public static void main(String[] args) {
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
                // 其他配置
                .set("spark.sql.iceberg.handle-timestamp-without-timezone", "true")
        ;
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        Dataset<Row> ds = spark.table("test.rsshub.shopping");
        System.out.println("数据条数："+ds.count());
        ds.show(1000);

        spark.stop();
    }
}
