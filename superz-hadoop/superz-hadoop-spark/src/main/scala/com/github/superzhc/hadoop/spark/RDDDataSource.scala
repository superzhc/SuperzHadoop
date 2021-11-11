package com.github.superzhc.hadoop.spark

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.{JdbcRDD, RDD}

/**
 * @author superz
 * @create 2021/9/27 17:33
 */
sealed abstract class RDDDataSource(val sc: SparkContext) {

}

/**
 * 读取集合中的数据
 *
 * @param sc
 */
class SeqRDDDataSource(sc: SparkContext) extends RDDDataSource(sc) {
  def read[T](seq: Seq[T]): RDD[T] = {
    //sc.parallelize(seq)
    null
  }
}

class JDBCRDDDataSource(sc:SparkContext) extends RDDDataSource(sc){
  def read(driver:String,url:String,username:String,password:String,sql:String): Unit ={
    // new JdbcRDD[String](sc,()=>{null},"select * from jd limit ?,?")
  }
}

object RDDDataSourceMain {
  val arr = Array(1 to 10: _*)

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf
    conf.setAppName("RDD Data Source Main")
    conf.setMaster("local[*]")
    val sc = new SparkContext(conf)
    val rdd1 = new SeqRDDDataSource(sc).read(arr)

  }
}
