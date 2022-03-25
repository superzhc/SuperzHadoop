package com.github.superzhc.hadoop.spark.scala

import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkHiveMain {
  val driver: String = "com.mysql.jdbc.Driver"
  val url: String = "jdbc:mysql://127.0.0.1:3306/news_dw?useSSL=false&useUnicode=true&characterEncoding=utf-8"
  val username = "root"
  val password = "123456"

  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "F:/soft/hadoop")
    //System.setProperty("user.name","root");
    System.setProperty("HADOOP_USER_NAME", "root");

    val spark: SparkSession = SparkSession.builder()
      .appName("hive sink")
      .master("local")
      // .config("fs.defaultFS","hdfs://log-platform01:8020")
      .config("spark.sql.warehouse.dir", "hdfs://log-platform01:8020/user/hive/warehouse")
      // .config("hive.metastore.uris", "thrift://log-platform02:9083,thrift://log-platform03:9083")
      .enableHiveSupport()
      .getOrCreate()

    // spark.sql("show tables").show()

    val df: DataFrame = spark.read.format("jdbc")
      .option("url", url)
      .option("driver", driver)
      .option("dbtable", "any_knew_hot_news")
      .option("user", username)
      .option("password", password)
      .load()

    //    df.show()

    df.write
      .mode("overwrite")
      .format("Hive")
      .saveAsTable("any_knew_hot_news")
  }
}
