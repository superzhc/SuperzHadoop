package com.github.superzhc.hadoop.spark.scala

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
 * RDD
 *
 * RDD 是分布式内存的一个抽象概念，是一种高度受限的共享内存模型，即 RDD 是只读的记录分区的集合。
 *
 * BlockManager 管理 RDD 的物理分区，每个 Block 就是节点上对应的一个数据块，可以存储在内存或者磁盘上；而 RDD 中的 Partition 是一个逻辑数据块，对应相应的物理块 Block。
 *
 * 本质上，一个 RDD 在代码中相当于数据的一个元数据结构，存储着数据分区及其逻辑结构映射关系，存储着 RDD 之前的依赖转换关系。
 *
 * @author superz
 * @create 2021/12/22 11:58
 */
object RDDMain {

  /*
  * RDD 有如下五大特性：
  * 1. 分区列表
  * 2. 每一个分区都有一个计算函数，即 RDD 都会实现 compute 函数，对具体的分片进行计算
  * 3. 依赖于其他 RDD 的列表
  * 4. key-value 数据类型的 RDD 分区器，控制分区策略和分区数
  * 5. 每个分区都有一个优先位置列表
  * */

  /*
  * RDD 源码分析：
  * // 通过子类实现给定分区的计算
  * def compute(split: Partition, context: TaskContext): Iterator[T]
  *
  * // 通过子类实现，返回一个 RDD 分区列表
  * // 该方法只会被调用一次，它是安全地执行一次耗时计算
  * // 此数组中的分区必须满足以下属性： rdd.partitions.zipWithIndex.forall { case (partition, index) => partition.index == index }
  * protected def getPartitions: Array[Partition]
  *
  * // 返回对父 RDD 的依赖列表，这个方法只被调用一次，它是安全地执行一次耗时计算
  * protected def getDependencies: Seq[Dependency[_]] = deps
  *
  * // 可选的，指定优先位置，输入参数是 split 分片，输出结果是一组优先的节点位置
  * protected def getPreferredLocations(split: Partition): Seq[String] = Nil
  *
  * // 可选的，通过子类来实现，指定如何分区【如果非key-value数据类型，这个一般是为 None】
  * @transient val partitioner: Option[Partitioner] = None
  * */

  /*
  * RDD 弹性特性的 7 个方面
  * 1. 自动进行内存和磁盘数据存储的切换：Spark 会优先把数据放到内存中，如果内存空间不够，会将数据放到磁盘中，Spark 不仅能对内存中的数据进行计算，也能对磁盘中的数据进行计算
  * 2. 基于 Lineage（血缘）的高效容错机制
  * 3. Task 如果失败，会自动进行特定次数的重试
  * 4. Stage 如果失败，会自动进行特定次数的重试
  * 5. checkpoint 和 persist，可主动或被动触发
  * 6. 数据调度弹性，DAGScheduler、TaskScheduler 和资源管理无关
  * 7. 数据分片的高度弹性
  * */

  /*
  * RDD 依赖关系
  *
  * RDD 依赖关系分为两种：窄依赖（Narrow Dependency）和宽依赖（Shuffle Dependency）
  *
  * 1. 窄依赖表示父 RDD 中的 Partition 最多被子 RDD 的一个 Partition 所使用
  * 2. 宽依赖表示父 RDD 中的 Partition 会被多个子 RDD 的 Partition 所使用
  * */

  val STRING_EMPTY: String = null

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("rdd demo").setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    val sc: SparkContext = spark.sparkContext
    // 这个设置没啥作用，fixme
    // sc.setLogLevel("debug")

    collection(sc).foreach(println)

    spark.stop()
  }

  def collection(sc: SparkContext): RDD[Int] = {
    val data = Array(1, 2, 3, 4, 5)
    val distData = sc.parallelize(data)
    distData
  }
}
