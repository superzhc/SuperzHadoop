package com.github.superzhc.scala.collection

/**
 * 列表
 *
 * Scala 中列表是非常类似于数组的，这意味着，一个列表的所有元素都具有相同的类型。
 *
 * 列表和数组之间的区别：
 * 1. 列表是不可变的，这意味着一个列表中的元素可以不被分配来改变
 * 2. 列表是一个链表，而数组是平坦的
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

  val fruit: List[String] = List("apple", "pears", "oranges")

  // 定义空 List
  val empty: List[Nothing] = List()
}
