package com.github.superzhc.scala.basic

/**
 * @author superz
 * @create 2021/9/8 15:53
 */
object IfMain {
  def main(args: Array[String]): Unit = {
    var x = readLine("输出数字").toInt

    /* 可以将if/else表达式的值赋值给变量 */
    val s = if (x > 0) 1 else -1
    // 等价于
    var s2: Int = 0
    if (x > 0) s2 = 1 else s2 = -1
  }
}
