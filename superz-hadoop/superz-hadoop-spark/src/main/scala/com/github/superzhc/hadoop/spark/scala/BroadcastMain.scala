package com.github.superzhc.hadoop.spark.scala

import com.github.superzhc.common.jdbc.JdbcHelper
import org.apache.spark.sql.SparkSession

object BroadcastMain {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local").getOrCreate()

    val url = "jdbc:mysql://127.0.0.1:3306/news_dw?useSSL=false"
    val username = "root"
    val password = "123456"

    var myConfigs: java.util.List[java.util.Map[String, Object]] = null
    var jdbc: JdbcHelper = null
    try {
      jdbc = new JdbcHelper(url, username, password)
      myConfigs = jdbc.query("select * from my_configs")
    } finally {
      if (null != jdbc) {
        jdbc.close()
      }
    }

    val myConfigBD=spark.sparkContext.broadcast(myConfigs)

    spark.stop()
  }
}
