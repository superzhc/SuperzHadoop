package com.github.superzhc.hadoop.spark.sql.source

import com.github.superzhc.hadoop.spark.sql.source.config.DockerMySQLConfig.{password, url, username}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.Properties

/**
 * @author superz
 * @create 2021/12/22 9:54
 */
object PenguinAnalyze {
  val table: String = "v_penguin"

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("spark sql jdbc source").setMaster("local")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    val properties: Properties = new Properties()
    properties.put("user", username)
    properties.put("password", password)
    var df: DataFrame = spark.read.jdbc(url, table, "pt", 0, 10000, 10000, properties)

    val c = df.count()
    println(c)

    spark.stop()
  }
}
