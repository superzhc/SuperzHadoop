package com.github.superzhc.spark;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * 2020年10月10日 superz add
 */
public class SparkContextUtils {
    public static JavaSparkContext local(){
        JavaSparkContext jsc=new JavaSparkContext(SparkConfUtils.local());
        return jsc;
    }
}
