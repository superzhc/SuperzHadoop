package com.github.superzhc.hadoop.flink.streaming

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time

object WordCount {
  def main(args: Array[String]): Unit = {
    val env:StreamExecutionEnvironment=StreamExecutionEnvironment.getExecutionEnvironment

    val text=env.socketTextStream("localhost",9090)

    import org.apache.flink.api.scala._
    val wordCount=text.flatMap(line=>line.split("\\s"))
      .map((_,1L))//
      .keyBy(0)//分组
      .timeWindow(Time.seconds(2),Time.seconds(1))//指定窗口大小，指定间隔时间
      .sum(1)//
//      .reduce((value1,value2)=>(value1._1,value1._2+value2._2))

    //打印到控制台
    wordCount.print().setParallelism(1)

    //执行任务
    env.execute("wordCount")
  }
}
