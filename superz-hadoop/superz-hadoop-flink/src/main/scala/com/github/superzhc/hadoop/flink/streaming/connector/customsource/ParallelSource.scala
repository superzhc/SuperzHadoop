package com.github.superzhc.hadoop.flink.streaming.connector.customsource

import org.apache.flink.streaming.api.functions.source.{ParallelSourceFunction, SourceFunction}

class ParallelSource extends ParallelSourceFunction[Long] {
  var count = 1L
  var isRunning = true

  override def run(ctx: SourceFunction.SourceContext[Long]): Unit = {
    while (isRunning) {
      ctx.collect(count)
      count += 1
      Thread.sleep(1000)
    }
  }

  override def cancel(): Unit = {
    isRunning = false
  }
}
