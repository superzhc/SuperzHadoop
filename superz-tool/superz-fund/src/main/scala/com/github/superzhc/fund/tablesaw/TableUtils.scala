package com.github.superzhc.fund.tablesaw

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import tech.tablesaw.columns.SkipColumnType
import tech.tablesaw.columns.booleans.BooleanColumnType
import tech.tablesaw.columns.dates.DateColumnType
import tech.tablesaw.columns.datetimes.DateTimeColumnType
import tech.tablesaw.columns.instant.InstantColumnType
import tech.tablesaw.columns.numbers.{DoubleColumnType, FloatColumnType, IntColumnType, LongColumnType, ShortColumnType}
import tech.tablesaw.columns.strings.{StringColumnType, TextColumnType}
import tech.tablesaw.columns.times.TimeColumnType

import java.util
import scala.collection.mutable

object TableUtils {
  /**
   * 实现 tablesaw 的 Table 转 Seq[Row] 隐式转换
   *
   * @param table
   * @return
   */
  implicit def Table2Row(table: tech.tablesaw.api.Table): Seq[Row] = {
    val r: mutable.ListBuffer[Row] = mutable.ListBuffer[Row]()

    import scala.collection.JavaConverters.iterableAsScalaIterable
    for (row <- iterableAsScalaIterable(table)) {

      val data = for (i <- 0 until row.columnCount()) yield row.getObject(i)
      r += Row(data: _*)
    }
    r
  }

  implicit class TableSawExtend(table: tech.tablesaw.api.Table) {
    def convert2Row(): java.util.List[Row] = {
      val r: java.util.List[Row] = new util.ArrayList[Row]()

      import scala.collection.JavaConverters.iterableAsScalaIterable
      for (row <- iterableAsScalaIterable(table)) {

        val data = for (i <- 0 until row.columnCount()) yield row.getObject(i)
        r.add(Row(data: _*))
      }
      r
    }

    def columnNames: Seq[String] = {
      import scala.collection.JavaConverters._
      asScalaBuffer(table.columnNames())
    }

    def structType: StructType = {
      val structFields = table.columnArray().map(d => {
        val datatype = d.`type`() match {
          case _: ShortColumnType => DataTypes.ShortType
          case _: IntColumnType => DataTypes.IntegerType
          case _: LongColumnType => DataTypes.LongType
          case _: FloatColumnType => DataTypes.FloatType
          case _: BooleanColumnType => DataTypes.BooleanType
          case _: DoubleColumnType => DataTypes.DoubleType
          /* 注意使用这个转换必须配置一个参数：spark.sql.datetime.java8API.enabled 为 true */
          case _: DateColumnType => DataTypes.DateType
          //        case  _: TimeColumnType => DataTypes
          //        case  _: DateTimeColumnType => DataTypes
          case _: StringColumnType | _: TextColumnType => DataTypes.StringType
          //        case  _: InstantColumnType => DataTypes
          //        case  _: SkipColumnType => DataTypes
          case _ => DataTypes.StringType
        }
        StructField(d.name(), datatype, true)
      })

      StructType(structFields)
    }
  }
}
