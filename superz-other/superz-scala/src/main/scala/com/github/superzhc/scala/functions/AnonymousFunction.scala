package com.github.superzhc.scala.functions

/**
 * 匿名函数
 * 在 Scala 中定义匿名函数的语法：
 *  (param:paramType,...) => { 函数体 }
 * 箭头左边是参数列表，右边是函数体，参数的类型是可省略的，Scala 的类型推测系统回推测出参数的类型
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
