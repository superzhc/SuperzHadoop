package com.github.superzhc.scala.basic

/**
 * for 循环
 *
 * @author superz
 * @create 2021/9/8 16:15
 */
object ForMain {
  def main(args: Array[String]): Unit = {
    /**
     * for 语法结构：
     * for(i <- 表达式)
     * 让变量 i 遍历 <- 右边表达式得所有值
     */

    /* 遍历字符串或数组时，通常需要使用从 0 到 n-1 的区间 */
    val s = "hello"
    var sum = 0
    for (i <- 0 until s.length) { // util 不包含结束值
      sum += s(i)
    }
    println(sum)

    sum = 0
    for (ch <- s) {
      sum += ch
    }
    println(sum)

    /**
     * Scala 并没有提供 break 或 continue 语句来退出循环，可以使用 Breaks 对象中的 break 方法
     */
    import scala.util.control.Breaks._
    breakable {
      for (i <- 0 to 10) {
        if (i % 2 == 0) break
        println(i)
      }
    }

    /* 可以以 `变量<-表达式` 的形式提供多个生成器，用分号隔开 */
    for (i <- 1 to 3; j <- 1 to 3) print(10 * i + j + " ")

    /* 每个生成器都可以带一个守卫，以 if 开头的 Boolean 表达式；注意 if 之前没有分号进行隔开的 */
    for (i <- 1 to 10 if i % 2 == 0; j <- 1 to 10 if j % 2 == 1) print(10 * i + j + " ")

    /* 可以使用任意多的定义，循环中可以使用这些定义的变量 */
    for (i <- 1 to 3; from = 4 - i; j <- from to 3) print(10 * i + j + " ")
    println()

    /* 可以使用 yield 来进行迭代构建集合 */
    val v = for (i <- 1 to 10) yield i * 10
    println(v)
  }
}
