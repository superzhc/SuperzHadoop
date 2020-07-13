package com.github.superzhc.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

/**
 * 2020年07月10日 superz add
 */
public class SparkSessionDemo
{
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();

        SparkSession sparkSession = SparkSession.builder().appName("SparkSessionDemo").master("local").config(conf)
                .getOrCreate();
    }
}
