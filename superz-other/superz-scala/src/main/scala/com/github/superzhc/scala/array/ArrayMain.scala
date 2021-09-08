package com.github.superzhc.scala.array

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * 数组
 *
 * @author superz
 * @create 2021/9/8 18:00
 */
object ArrayMain {
  /* 定长数组 */
  // 定义了10个整数的数组，所有元素初始化为 0
  val nums = new Array[Int](10)
  // 定义一个10个元素的字符串数组，所有元素初始化为null
  val a = new Array[String](10)
  // 定义一个赋值的数组，类型通过赋值来推断出来的；说明，已提供初始值就不需要new
  val s = Array("Hello", "World")

  /* 变长数组：数据缓冲 */
  val b = ArrayBuffer[Int]()
  // 跟上面是等效的
  val b2 = new ArrayBuffer[Int]()

  def main(args: Array[String]): Unit = {
    /* 获取指定下标的数组的数据，注意通过括号（`()`）而不是中括号（`[]`） */
    println(s(0))

    /* 变长数组新增数据 */
    // 在尾端添加元素
    b += 1
    // 在尾端添加多个元素，以括号括起来
    b += (2, 3, 4, 5)
    // 追加数组
    b ++= Array(6, 7, 8, 9)
    println(b)
  }

  /**
   * 将可变数组转换成 Java List
   *
   * @param arr
   * @return
   */
  def arrayBufferConvertJavaList(arr: ArrayBuffer[Int]): java.util.List[Int] = {
    import scala.collection.JavaConversions.bufferAsJavaList
    arr // 自动进行了隐式转换
  }

  def javaListConvertArrayBuffer(list: java.util.List[Int]): mutable.Buffer[Int] = {
    import scala.collection.JavaConversions.asScalaBuffer
    list
  }
}
