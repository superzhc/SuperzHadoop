package com.github.superzhc.hadoop.spark.java.iceberg;

import org.apache.spark.SparkConf;

/**
 * @author superz
 * @create 2023/3/1 13:49
 **/
public class IcebergMain {
    public static void main(String[] args) {
        SparkConf hadoopConf = new SparkConf();
        hadoopConf.setAppName("Spark Iceberg Main")
                // 所有的配置都是以spark.sql.catalog开头，hadoop_catalog为用户自定义部分，表示 Catalog Name，在后续的查询和写入操作都会用到。
                .set("spark.sql.catalog.hadoop_catalog", "org.apache.iceberg.spark.SparkCatalog")
                // spark.sql.catalog.hadoop_catalog.type取值只能为hadoop或hive，表示 Iceberg Catalog 的下层实现是HadoopCatalog或HiveCatalog，用户也可以自定义实现 Catalog。
                .set("spark.sql.catalog.hadoop_catalog.type", "hadoop")
                // spark.sql.catalog.catalog-name.default-namespace表示默认的库名。
                .set("spark.sql.catalog.hadoop_catalog.default-namespace", "iceberg_db")
                // spark.sql.catalog.hadoop_catalog.warehouse表示 Iceberg 表存储位置。可以是本地文件系统，也可以是 HDFS、S3、OSS 等。如：hdfs://nn:8020/warehouse/path。
                .set("spark.sql.catalog.hadoop_catalog.warehouse", "/opt/module/spark-3.2.1/spark-warehouse")
        ;

        SparkConf hiveConf = new SparkConf();
        hiveConf.setAppName("Spark Iceberg Main")
                .set("spark.sql.catalog.hadoop_catalog", "org.apache.iceberg.spark.SparkCatalog")
                .set("spark.sql.catalog.hadoop_catalog.type", "hive")
                .set("spark.sql.catalog.hadoop_catalog.default-namespace", "iceberg_db")
                // 如果要实现 Metastore 高可用，可以添加多个 uri 属性，用逗号隔开，如：thrift://metastore-host:port,thrift://metastore-host:port。
                .set("spark.sql.catalog.hive_catalog.uri", "thrift://metastore-host:port")
        ;


    }
}
