package com.github.superzhc.fund

import com.github.superzhc.fund.akshare.EastMoney
import org.apache.spark.sql.SparkSession

import scala.collection.mutable.ArrayBuffer

object FundMain {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .appName("fund main")
      .master("local[2]")
      .getOrCreate()

    val table: tech.tablesaw.api.Table = EastMoney.e()

    // tech.tablesaw.api.Row 不能序列化，待解决
    var datas: ArrayBuffer[tech.tablesaw.api.Row] = ArrayBuffer[tech.tablesaw.api.Row]()
    import scala.collection.JavaConverters._
    val tableIterator=table.iterator()
    while(tableIterator.hasNext) {
      datas += tableIterator.next()
    }

    val rdd = spark.sparkContext.parallelize(datas)
    rdd.foreach(r => println(r.toString))
  }
}
