package com.github.superzhc.hadoop.spark.sql.source

import com.github.superzhc.common.jdbc.JdbcHelper
import org.apache.avro.generic.GenericData.StringType
import org.apache.spark.SparkConf
import org.apache.spark.sql.functions.{col, concat, lit, monotonically_increasing_id, udf}
import org.apache.spark.sql.types.DataTypes
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

import java.util.Properties

/**
 * @author superz
 * @create 2021/12/21 13:49
 */
object JdbcSource {
  val driver: String = "com.mysql.jdbc.Driver"
  val url: String = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8"
  val username = "root"
  val password = "123456"

  val COLUMN_CHEPAIHAO = "chepaihao"
  val COLUMN_CHEXING = "chexing"
  val COLUMN_LIANXIREN = "lianxiren"
  val COLUMN_LIANXIDIANHUA = "lianxidianhua"
  val COLUMN_SHENFENZHENG = "shenfenzheng"
  val COLUMN_DIZHI = "dizhi"


  def main(args: Array[String]): Unit = {
    //    preview()
    job()
  }

  def preview() = {
    val jdbc: JdbcHelper = new JdbcHelper(url, username, password)
    jdbc.preview("car_import_7shenzhenqiche2100_1")
  }

  def job() = {
    val conf = new SparkConf().setAppName("spark sql jdbc source").setMaster("local")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    val properties: Properties = new Properties()
    properties.put("user", username)
    properties.put("password", password)

    val table = "car_import_09nianguangdongfoshanchezhu517tiaoziduanquan_1"
    var df: DataFrame = spark.read.jdbc(url, table, "id", 1, 1200, 4, properties)
    df = car_import_09nianguangdongfoshanchezhu517tiaoziduanquan_1(df)

    val saveTable = "spark_car_info"
    var saveMode: SaveMode = null
    //    if (new JdbcHelper(url, username, password).exist(saveTable)) {
    saveMode = SaveMode.Append
    //    } else {
    //    saveMode = SaveMode.Overwrite
    //    }

    df.write.mode(saveMode).jdbc(url, saveTable, properties)

    spark.stop()
  }

  def car_import_5yuejiangxi1000_1(df: DataFrame): DataFrame = {
    var df1 = df.select(
      col("c7").as(COLUMN_LIANXIREN) //.cast("varchar(255)")
      , col("c12").as(COLUMN_LIANXIDIANHUA) //.cast("varchar(255)")
      , col("c8").as("shenfenzheng") //.cast("varchar(50)")
      , col("c9").as("dizhi") //.cast("varchar(1024)")
      , col("c4").as("chexing") //.cast("varchar(255)")
      , col("c13").as("vin") //.cast("varchar(255)")
      , concat(lit("赣"), col("c3")).as("chepaihao") //.cast("varchar(10)")
      , col("c15").as("riqi") //.cast(DataTypes.TimestampType)
    )
    // 新增一列自增列
    // .withColumn("id", monotonically_increasing_id())

    df1
  }

  def car_import_7shenzhenqiche2100_1(df: DataFrame): DataFrame = {
    val df1 = df.select(
      col("c1").as(COLUMN_CHEPAIHAO)
      , col("c3").as(COLUMN_CHEXING)
      , col("c4").as(COLUMN_LIANXIREN)
      , col("c5").as(COLUMN_LIANXIDIANHUA)
      , col("c7").as(COLUMN_SHENFENZHENG)
      , col("c8").as(COLUMN_DIZHI)
    )
    df1
  }

  def car_import_09nianfoshanchezhu519_1(df: DataFrame): DataFrame = {

    def lianxidianhua = udf((str1: String, str2: String) => {
      var result: String = null

      if (str1.replaceAll("0", "").trim.length == 0) {
        result = str2
      } else {
        result = str1
      }

      if ("未填写" != str2 && result != str2) {
        result = str2
      }

      result
    })

    val df1 = df.filter("id>2")
      .withColumn(COLUMN_LIANXIDIANHUA, lianxidianhua(col("c16"), col("c17")))
      .select(
        col("c1").as(COLUMN_CHEPAIHAO)
        , col("c5").as(COLUMN_CHEXING)
        , col("c10").as(COLUMN_SHENFENZHENG)
        , col("c11").as(COLUMN_DIZHI)
        , col("c15").as(COLUMN_LIANXIREN)
        , col(COLUMN_LIANXIDIANHUA)
      )

    df1
  }

  def car_import_09nianguangdongfoshanchezhu50tiao_1(df: DataFrame): DataFrame = {
    def lianxidianhua = udf((str1: String, str2: String) => {
      var result: String = null

      if (str1.replaceAll("0", "").trim.length == 0) {
        result = str2
      } else {
        result = str1
      }

      if ("未填写" != str2 && result != str2) {
        result = str2
      }

      result
    })

    val df1 = df
      .withColumn(COLUMN_LIANXIDIANHUA, lianxidianhua(col("c16"), col("c17")))
      .select(
        col("c1").as(COLUMN_CHEPAIHAO)
        , col("c5").as(COLUMN_CHEXING)
        , col("c10").as(COLUMN_SHENFENZHENG)
        , col("c11").as(COLUMN_DIZHI)
        , col("c15").as(COLUMN_LIANXIREN)
        , col(COLUMN_LIANXIDIANHUA)
      )

    df1
  }

  def car_import_09nianguangdongfoshanchezhu517tiaoziduanquan_1(df: DataFrame): DataFrame = {
    def lianxidianhua = udf((str1: String, str2: String) => {
      var result: String = null

      if (str1.replaceAll("0", "").trim.length == 0) {
        result = str2
      } else {
        result = str1
      }

      if ("未填写" != str2 && result != str2) {
        result = str2
      }

      result
    })

    val df1 = df.filter("id>2")
      .withColumn(COLUMN_LIANXIDIANHUA, lianxidianhua(col("c16"), col("c17")))
      .select(
        col("c1").as(COLUMN_CHEPAIHAO)
        , col("c5").as(COLUMN_CHEXING)
        , col("c10").as(COLUMN_SHENFENZHENG)
        , col("c11").as(COLUMN_DIZHI)
        , col("c15").as(COLUMN_LIANXIREN)
        , col(COLUMN_LIANXIDIANHUA)
      )

    df1
  }
}
