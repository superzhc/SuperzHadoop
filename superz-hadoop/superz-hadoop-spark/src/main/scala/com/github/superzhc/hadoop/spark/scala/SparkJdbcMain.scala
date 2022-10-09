package com.github.superzhc.hadoop.spark.scala

import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.Properties

object SparkJdbcMain {

  def main(args: Array[String]): Unit = {

  }

  def mysql(url: String, username: String, password: String, tableName: String): DataFrame = {
    val spark: SparkSession = SparkSession.builder().getOrCreate()

    val properties: Properties = new Properties()
    properties.setProperty("user", username)
    properties.setProperty("password", password)

    spark.read.jdbc(url, tableName, properties)
  }

  def postgres(): DataFrame = {
    null
  }

}
