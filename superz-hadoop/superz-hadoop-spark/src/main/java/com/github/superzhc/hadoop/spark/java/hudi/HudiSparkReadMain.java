package com.github.superzhc.hadoop.spark.java.hudi;

import org.apache.hudi.DataSourceReadOptions;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * @author superz
 * @create 2022/12/27 16:19
 **/
public class HudiSparkReadMain {
    public static void main(String[] args) {
        // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
        System.setProperty("HADOOP_USER_NAME", "root");

        SparkConf conf = new SparkConf()
                .setAppName("hudi spark")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .set("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.hudi.catalog.HoodieCatalog")
                .set("spark.sql.extensions", "org.apache.spark.sql.hudi.HoodieSparkSessionExtension")
                .setMaster("local");
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        String basePath = "";
        Dataset<Row> ds = spark.read().format("hudi").load(basePath);
        ds.createOrReplaceTempView("superz_hudi_spark_client");

//        /*查询模式*/
//        spark.read().format("hudi")
//                // 查询模式，支持 snapshot、read_optimized 和 incremental；参数进行配置
//                .option(DataSourceReadOptions.QUERY_TYPE().key(), DataSourceReadOptions.QUERY_TYPE_INCREMENTAL_OPT_VAL())
//                .load(basePath)
//        ;

        spark.sql("select * from superz_hudi_spark_client").show();

        spark.stop();
    }
}
