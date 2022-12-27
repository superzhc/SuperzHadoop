package com.github.superzhc.hadoop.spark.scala.hudi

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkHudiReader {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf()
      .setAppName("hudi demo")
      .setMaster("local[*]")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.sql.extensions","org.apache.spark.sql.hudi.HoodieSparkSessionExtension")

    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

//    val tableName = "superz_s_ne_history_origin"
//    spark.read.format("hudi").load(s"/user/superz/hudi/$tableName/*").show()

    val tableName = "superz_java_client_20221213150742"
    val tablePath = "hdfs://hanyun-3:8020/hudi/superz/"+tableName
    spark.read.format("hudi").load(tablePath).show()
  }
}
