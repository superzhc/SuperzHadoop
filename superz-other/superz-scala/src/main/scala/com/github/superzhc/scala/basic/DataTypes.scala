package com.github.superzhc.scala.basic


/**
 * 数据类型
 * 1. 在 Scala 中有一个根类型 Any，它是所有类的父类
 * 2. 在 Scala 中一切皆为对象，分为两大类 AnyVal(值类型)，AnyRef(引用类型)，它们都是 Any 的子类
 * 3. Null 类型是 ScalaNull 类型是 Scala 的特别类型，它只有一个值 null，它是所有 AnyRef 类型的子类
 * 4. Nothing 类型是所有类的子类
 *
 * @author superz
 * @create 2021/9/13 9:58
 */
object DataTypes {
  var any: Any = _
  val s: Short = 1
  val whole: Int = 10
  val fractional: Double = 1.4
  val trueOrFalse: Boolean = true
  val words: String = "A value"
  val lines: String =
    """
      Triple quotes let you have many lines
      in your string
    """
}
