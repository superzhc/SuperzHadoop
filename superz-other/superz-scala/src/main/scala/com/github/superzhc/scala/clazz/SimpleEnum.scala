package com.github.superzhc.scala.clazz

/**
 * 枚举
 * 在 Scala 中并没有枚举，不过 Scala 标准库提供了一个 Enumeration 工具类，可以用于产出枚举
 * 使用方式：
 * 定义一个扩展 Enumeration 类的对象并以 Value 方法调用初始化没居中的所有可选值
 *
 * @author superz
 * @create 2021/9/9 15:19
 */
object SimpleEnum extends Enumeration {
  // val Red = Value
  // val Yello = Value
  // val Green = Value

  // 简写方式如下
  // val Red, Yello, Green = Value

  /* 可以向 Value 方法传入 ID、名称，或两个参数都传 */
  val Red = Value(0, "红色")
  val Yello = Value(10) // 名称为 Yello
  val Green = Value("绿色") // ID 为 11
}
