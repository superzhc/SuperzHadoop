package com.github.superzhc.hadoop.spark.demo

import com.github.superzhc.common.jdbc.JdbcHelper
import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

import java.util
import scala.util.matching.Regex

/**
 * @author superz
 * @create 2021/12/29 13:36
 */
object ErrorDataFix {
  val driver: String = "com.mysql.jdbc.Driver"
  val url: String = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8"
  val username = "root"
  val password = "123456"

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("jdbc source").setMaster("local")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    val sc: SparkContext = spark.sparkContext
    sc.setLogLevel("debug")

    val table = "error_data"

    // 读取表数据
    val rdd: JdbcRDD[String] = new JdbcRDD[String](
      sc,
      () => new JdbcHelper(driver, url, username, password).getConnection,
      "select data from errors_data where origin_schema='company_info' and ? <= id and id < ?",
      155399,
      759555,
      28,
      rs => rs.getString(1)
    )

    rdd
      .filter(data => {
        val arr = data.split(",", -1)
        if (arr.length < 10) {
          println(data)
        }

        arr.length >= 10
      })
      .map(data => {
        // 注意参数，不然会丢掉后缀
        val arr = data.split(",", -1)

        val d = new Array[String](10)
        //      d(9)=arr(arr.length-1)//lon
        //      d(8)=arr(arr.length-2)//lat
        //      d(7)=arr(arr.length-3)//licenseno
        //      d(6)=arr(arr.length-4)//taxno
        //      d(5)=arr(arr.length-5)//oldname
        //      d(4)=arr(arr.length-6)//legal
        //      d(3)=arr(arr.length-7)//regno
        //      d(2)=arr(arr.length-8)//entity
        for (i <- 1 to 8) {
          d(10 - i) = arr(arr.length - i)
        }

        // 邮箱正则表达式
        val pattern = new Regex("^[A-Za-z0-9\\u4e00-\\u9fa5\\._-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")

        for (i <- 0 to arr.length - 9) {
          val str = arr(arr.length - 9 - i)
          if (pattern.findAllIn(str).size > 0) {
            if (null == d(0) || d(0).trim.length == 0)
              d(0) = str
            else
              d(0) = d(0) + "," + str
          } else {
            if (null == d(1) || d(1).trim.length == 0)
              d(1) = str
            else
              d(1) = d(1) + "," + str
          }
        }

        d(1) += ""
        d(0) += ""

        d
      }).foreachPartition(datas => {
      val jdbc: JdbcHelper = new JdbcHelper(driver, url, username, password)

      val params: java.util.List[java.util.List[Object]] = new util.ArrayList[util.List[Object]]()
      while (datas.hasNext) {
        val data = datas.next()

        val param: java.util.List[Object] = new util.ArrayList[Object](data.length)
        for (i <- 0 until data.length)
          param.add(data(i))

        params.add(param)
      }

      jdbc.batchUpdate("company_info", "email,tel,entity,regno,legal,oldname,taxno,licenseno,lat,lon".split(","), params, 1000)
      jdbc.close()
    })

    spark.stop()
  }
}
