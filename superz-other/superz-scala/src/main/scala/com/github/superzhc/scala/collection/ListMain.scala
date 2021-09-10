package com.github.superzhc.scala.collection

/**
 * 列表
 *
 * @author superz
 * @create 2021/9/9 17:32
 */
object ListMain extends App {
  val digits = List(4, 2)
  /* 定义列表方式一 */
  // :: 操作符从给定的头和尾创建一个新的列表
  val digits2 = 9 :: digits
  // 等同于
  val digits3 = 9 :: 4 :: 2 :: Nil /* 定义列表方式二 */
}
