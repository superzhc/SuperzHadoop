package com.github.superzhc.hadoop.spark.java.iceberg.catalog;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalog.Catalog;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/3/25 14:31
 **/
public class SparkIcebergHadoopCatalogInHadoopTest {
    static {
        // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
        System.setProperty("HADOOP_USER_NAME", "root");
    }

    SparkSession spark;

    @Before
    public void setUp() {
        SparkConf sparkConf = new SparkConf();
        sparkConf.set("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions");
        sparkConf.set("spark.sql.catalog.iceberg_hadoop", "org.apache.iceberg.spark.SparkCatalog");
        sparkConf.set("spark.sql.catalog.iceberg_hadoop.type", "hadoop");
        sparkConf.set("spark.sql.catalog.iceberg_hadoop.warehouse", "hdfs://xgitbigdata/usr/xgit/hive/warehouse");

        spark = SparkSession.builder()
                .appName("Spark Iceberg Usage HadoopCatalog operate Hadoop")
                .master("local")
                .config(sparkConf)
                .getOrCreate();
    }

    @Test
    public void catalog() {
        Catalog catalog = spark.catalog();
        catalog.listDatabases().show(100, false);
    }

    @Test
    public void databases(){
        spark.sql("SHOW DATABASES IN iceberg_hadoop").show();
    }

    @Test
    public void dropDatabase(){
        spark.sql("DROP DATABASE IF EXISTS iceberg_hadoop.`spark_iceberg_superz2.db`");
        spark.sql("SHOW DATABASES IN iceberg_hadoop").show();
    }
}
