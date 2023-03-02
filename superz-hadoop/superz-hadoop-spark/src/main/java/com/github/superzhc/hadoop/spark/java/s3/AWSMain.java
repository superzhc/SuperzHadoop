package com.github.superzhc.hadoop.spark.java.s3;

import com.github.superzhc.data.other.AKTools;
import com.github.superzhc.hadoop.spark.java.dataframe.DataFrameMain;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/1 15:48
 **/
public class AWSMain {
    private SparkConf conf;

    public AWSMain(String endpoint, String username, String password) {
        this(new SparkConf(), endpoint, username, password);
    }

    public AWSMain(SparkConf conf, String endpoint, String username, String password) {
        this.conf = conf;
        this.conf.
                set("spark.hadoop.fs.s3a.endpoint", endpoint/*"127.0.0.1:9000"*/)
                .set("spark.hadoop.fs.s3a.access.key", username/*"admin"*/)
                .set("spark.hadoop.fs.s3a.secret.key", password/*"admin123456"*/)
                .set("spark.hadoop.fs.s3a.connection.ssl.enabled", "false")
                .set("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
        ;
    }

    public SparkConf getConf() {
        return this.conf;
    }

    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf
                .setAppName("S3 Demo")
                .setMaster("local")
                .set("spark.hadoop.fs.s3a.endpoint", "127.0.0.1:9000")
                .set("spark.hadoop.fs.s3a.access.key", "admin")
                .set("spark.hadoop.fs.s3a.secret.key", "admin123456")
                .set("spark.hadoop.fs.s3a.connection.ssl.enabled", "false")
                .set("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
        ;
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
    }
}
