package com.github.superzhc.hadoop.flink.streaming

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.ParallelSource
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

object CustomSourceDemo {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.api.scala._
    val dss = env.addSource(new ParallelSource)

    val v=dss.map(d=>(d))



    dss.print().setParallelism(1)
    env.execute("custom source demo")
  }
}
