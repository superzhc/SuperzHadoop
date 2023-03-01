package com.github.superzhc.hadoop.spark.java.s3;

import org.apache.spark.SparkConf;

/**
 * @author superz
 * @create 2023/3/1 15:48
 **/
public class AWSMain {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.set("spark.hadoop.fs.s3a.endpoint", "127.0.0.1:9000")
                .set("spark.hadoop.fs.s3a.access.key", "admin")
                .set("spark.hadoop.fs.s3a.secret.key", "admin123456")
                .set("spark.hadoop.fs.s3a.connection.ssl.enabled", "false")
        ;
    }
}
