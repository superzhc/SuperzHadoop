package com.github.superzhc.geo.geotrellis

import geotrellis.spark.store.file.{FileLayerReader, FileLayerWriter}
import geotrellis.spark.store.hadoop.{HadoopLayerReader, HadoopLayerWriter}
import geotrellis.spark.store.{LayerReader, LayerWriter}
import geotrellis.spark.util.SparkUtils
import geotrellis.store.file.FileAttributeStore
import geotrellis.store.hadoop.HadoopAttributeStore
import geotrellis.store.{AttributeStore, ValueReader}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.spark.{SparkConf, SparkContext}

import java.net.URI

/**
 * 通过Spark读取数据
 *
 * @author superz
 * @create 2021/9/11 10:40
 */
object DataReaderBySpark {
  /* LayerReader都需要Spark上下文 */
  implicit val sc: SparkContext = SparkUtils.createSparkContext("SparkDataReader", new SparkConf(true).setMaster("local[*]"))

  /* s3 数据存储和读取 × */
  // 注意：需要引入 geotrellis-s3-spark_${scala.binary.version} 这个实现类
  def S3(path: String): Unit = {
    val uri = new URI(path /*"s3://bucket/catalog"*/)
    val store = AttributeStore(uri)
    val reader = LayerReader(uri)
    val writer = LayerWriter(uri)
    val values = ValueReader(uri)
  }

  /* 文件系统数据存储和读取 */
  def file(catalogPath: String) = {
    val flieStore: FileAttributeStore = FileAttributeStore(catalogPath)
    val reader: FileLayerReader = FileLayerReader(flieStore)
    val writer: FileLayerWriter = FileLayerWriter(flieStore)
  }

  /* HDFS数据存储和读取 */
  def hdfs(rootPath: Path) = {
    val config: Configuration = new Configuration()
    val store1: HadoopAttributeStore = HadoopAttributeStore(rootPath, config)
    val reader = HadoopLayerReader(store1)
    val writer = HadoopLayerWriter(rootPath, store1)
  }

  /*HBASE数据存储和读取
  def hbase(table:String)={
    val instance: HBaseInstance = ...
    implicit val sc: SparkContext = ...
    val store: AttributeStore = HBaseAttributeStore(instance, attrTable)
    val reader = HBaseLayerReader(store) /* Needs the implicit SparkContext */
    val writer = HBaseLayerWriter(store, dataTable)
  }*/
}
