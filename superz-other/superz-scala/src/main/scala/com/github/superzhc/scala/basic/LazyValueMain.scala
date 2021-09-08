package com.github.superzhc.scala.basic

/**
 * 惰值
 * 当 val 被声明为 lazy 时，它的初始化将被推迟
 *
 * @author superz
 * @create 2021/9/8 17:39
 */
object LazyValueMain {
  lazy val words = "hello"

  def main(args: Array[String]): Unit = {
    // 断点打在下面的一行语句上，可以很明显的观察到words的值是null
    println(words)
  }
}
