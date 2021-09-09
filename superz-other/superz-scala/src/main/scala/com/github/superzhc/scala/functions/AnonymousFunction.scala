package com.github.superzhc.scala.functions

/**
 * 匿名函数
 *
 * @author superz
 * @create 2021/9/9 17:15
 */
object AnonymousFunction extends App {
  val triple = (x: Double) => 3 * x

  // 等同于
  def triple2(x: Double) = {
    3 * x
  }
}
