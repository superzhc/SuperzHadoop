package com.github.superzhc.spark;

import org.apache.spark.SparkConf;

/**
 * 2020年10月09日 superz add
 */
public class SparkConfUtils
{
    private static final String DEFAULT_APP_NAME = "superz";

    public static SparkConf local(String appName) {
        SparkConf conf = new SparkConf();
        return conf.setAppName(appName).setMaster("local");
    }

    public static SparkConf local() {
        return local(DEFAULT_APP_NAME);
    }

    public static SparkConf local_stream(String appName) {
        SparkConf conf = new SparkConf();
        return conf.setAppName(appName).setMaster("local[2]");
    }

    public static SparkConf local_stream() {
        return local_stream(DEFAULT_APP_NAME);
    }
}
