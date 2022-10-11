package com.github.superzhc.hadoop.spark.scala.deploy

import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkLocalMode {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "F:\\soft\\hadoop")
    System.setProperty("HADOOP_USER_NAME", "root")

    val spark: SparkSession = SparkSession.builder()
      .appName("local mode")
      .master("local[*]")
      .config("spark.sql.warehouse.dir", "hdfs://log-platform01:8020/user/hive/warehouse")
      .enableHiveSupport()
      .getOrCreate()

    val df: DataFrame = spark.sql("select * from any_knew_hot_news")
    df.show()
  }
}
