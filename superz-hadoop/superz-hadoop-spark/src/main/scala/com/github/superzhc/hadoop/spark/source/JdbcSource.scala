package com.github.superzhc.hadoop.spark.source

import com.github.superzhc.hadoop.spark.util.JdbcHelper
import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

import java.sql.{Connection, DriverManager}
import java.util.Properties

/**
 * @author superz
 * @create 2021/12/21 9:48
 */
object JdbcSource {
  val driver: String = "com.mysql.jdbc.Driver"
  val url: String = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8"
  val username = "root"
  val password = "123456"

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("jdbc source").setMaster("local")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    val sc: SparkContext = spark.sparkContext
    sc.setLogLevel("debug")

    val table = "car_import_5yuejiangxi1000_1"

    // 读取表数据
    val rdd: JdbcRDD[Array[Object]] = new JdbcRDD[Array[Object]](
      sc,
      () => JdbcHelper(driver, url, username, password).getConnection,
      "select * from car_import_5yuejiangxi1000_1 where ? <= id and id <= ?",
      1,
      1200,
      4,
      JdbcRDD.resultSetToObjectArray
    )

    rdd.collect().foreach(d => println(d.mkString(",")))

    spark.stop()
  }
}
