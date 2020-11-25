package com.github.superzhc.flink.batch

import org.apache.flink.api.scala.ExecutionEnvironment

object BatchWordCount {
  def main(args: Array[String]): Unit = {
    // 获取环境
    val env:ExecutionEnvironment=ExecutionEnvironment.getExecutionEnvironment

    // 获取文件内容
    val text=env.readTextFile("D://data/wordcount.txt")

    import org.apache.flink.api.scala._
    val counts=text.flatMap(_.split("\\W+"))//
      .filter(_.nonEmpty)//
      .map((_,1))//
      .groupBy(0)//
      .sum(1)

    counts.writeAsCsv("D://data/flink","\n"," ").setParallelism(1)
    env.execute("batch word count")
  }
}
