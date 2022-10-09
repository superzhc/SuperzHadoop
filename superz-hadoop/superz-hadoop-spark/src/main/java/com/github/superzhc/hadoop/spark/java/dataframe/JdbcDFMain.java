package com.github.superzhc.hadoop.spark.java.dataframe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

/**
 * @author superz
 * @create 2022/10/9 15:08
 **/
public class JdbcDFMain {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .master("local")
                .getOrCreate();

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "123456");
        Dataset<Row> df = spark.read().jdbc("jdbc:mysql://127.0.0.1:3306/news_dw?useSSL=false", "xgit_spider", properties);

        // System.out.println(df.count());
        df.show(100);

        spark.stop();
    }
}
