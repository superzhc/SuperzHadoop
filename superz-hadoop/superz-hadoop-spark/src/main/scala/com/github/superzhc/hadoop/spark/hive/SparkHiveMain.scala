package com.github.superzhc.hadoop.spark.hive

import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkHiveMain {
  val driver: String = "com.mysql.jdbc.Driver"
  val url: String = "jdbc:mysql://localhost:3306/news_dw?useSSL=false&useUnicode=true&characterEncoding=utf-8"
  val username = "root"
  val password = "123456"

  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "F:/soft/hadoop")

    val spark:SparkSession=SparkSession.builder()
      .appName("hive sink")
      .master("local[*]")
      .config("fs.defaultFS","hdfs://namenode:9000")
      .config("spark.sql.warehouse.dir", "hdfs://namenode:9000/user/hive/warehouse")
      .config("hive.metastore.uris", "thrift://namenode:9083")
      .enableHiveSupport()
      .getOrCreate()

    val df:DataFrame=spark.read.format("jdbc")
      .option("url", url)
      .option("driver",driver)
      .option("dbtable", "any_knew_hot_news")
      .option("user", username)
      .option("password", password)
      .load()

//    df.show()

    df.write
      .mode("overwrite")
      .format("Hive")
      .insertInto("any_knew_hot_news")
  }
}
