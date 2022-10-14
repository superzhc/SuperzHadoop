package com.github.superzhc.hadoop.spark.java.dataframe;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author superz
 * @create 2022/10/9 15:08
 **/
public class JdbcDFMain {
    private static final Logger log = LoggerFactory.getLogger(JdbcDFMain.class);

    /**
     * 读Jdbc数据源
     */
    static Dataset<Row> read(SparkSession spark) {
        String url = "jdbc:mysql://127.0.0.1:3306/news_dw?useSSL=false";

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "123456");

        /* 表名读取 */
        // Dataset<Row> df = spark.read().jdbc(url, "device_status_result", properties);
        /* 通过创建子表来过滤数据读取 */
        String sql = "(select * from device_status_result where begin_time > '2022-10-10 00:00:00') as t1";
        Dataset<Row> df = spark.read().jdbc(url, sql, properties);
        return df;
    }

    static void write(Dataset<Row> ds) {
        ds.write().format("jdbc")
                .mode(SaveMode.Overwrite)
                .option("url", "jdbc:mysql://127.0.0.1:3306/news_dw?useSSL=false")
                .option("user", "root")
                .option("password", "123456")
                .option("dbtable","device_status_result2")
                .save();
    }


    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .master("local")
                .getOrCreate();

        Dataset<Row> df = read(spark);

        // System.out.println(df.count());
        // df.show(100);

        df.printSchema();

        write(df);

        spark.stop();
    }
}
