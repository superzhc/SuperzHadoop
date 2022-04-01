package com.github.superzhc.fund

import com.github.superzhc.fund.akshare.EastMoney
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession}

object FundMain {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .appName("fund main")
      .master("local[*]")
      .config("spark.sql.datetime.java8API.enabled", true)
      .getOrCreate()

    val table: tech.tablesaw.api.Table = EastMoney.estimation()

    import com.github.superzhc.fund.tablesaw.TableUtils._
    // val rdd: RDD[Row] = spark.sparkContext.parallelize(table)
    // val df=spark.createDataFrame(rdd, table.structType)
    val df = spark.createDataFrame(table.convert2Row(), table.structType)
    // df.printSchema()
    df.show()
    // 13931
    println("数据量：" + df.count())

    val rdd: RDD[Row] = df.rdd
    // val ps=rdd.partitions.size
    // println(ps)
    // rdd.glom().foreach(d=>println(d.length))
    rdd.foreachPartition(rows => {
      for (row <- rows) {
        println(row)
        Thread.sleep(1000 * 30)
      }
    })
  }
}
