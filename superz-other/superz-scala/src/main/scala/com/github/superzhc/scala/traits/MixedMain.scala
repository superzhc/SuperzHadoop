package com.github.superzhc.scala.traits

import java.util.Date

/**
 * 混织示例
 *
 * @author superz
 * @create 2021/9/9 16:13
 */
object MixedMain extends App {
  val r1 = new Record
  r1.record("this is a msg")
  /* 结果：【无任何数据】 */
  println("-------------------------------华丽的分割线-----------------------------------")
  /* 可以在构造对象时混入具体实现的特质 */
  val r2 = new Record with ConsoleLogger
  r2.record("this is a msg")
  /* 结果：this is a msg */
  println("-------------------------------华丽的分割线-----------------------------------")
  /* 混入多个特质，从最后一个开始调用，再调用之前的*/
  val r3 = new Record with ConsoleLogger with TimestampLogger with ShortLogger
  r3.record("this is a msg")
  /* 结果：Thu Sep 09 16:28:31 CST 2021 this is a msg */
  println("-------------------------------华丽的分割线-----------------------------------")
  val r4 = new Record with ConsoleLogger with ShortLogger with TimestampLogger
  r4.record("this is a msg")
  /* 结果：Thu Sep 09 1... */
}

/**
 * 继承Logger，如果直接使用的话将不会看到任何东西
 */
class Record extends Logger {
  def record(msg: String) {
    log(msg)
  }
}

trait Logger {
  def log(msg: String) {}
}

trait ConsoleLogger extends Logger {
  override def log(msg: String): Unit = {
    println(msg)
  }
}

trait TimestampLogger extends Logger {
  override def log(msg: String): Unit = {
    super.log(new Date() + " " + msg)
  }
}

trait ShortLogger extends Logger {
  override def log(msg: String): Unit = {
    super.log(if (msg.length <= 15) msg else msg.substring(0, 12) + "...")
  }
}
