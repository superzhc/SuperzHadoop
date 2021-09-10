package com.github.superzhc.scala.basic

/**
 * 字符串
 *
 * @author superz
 * @create 2021/9/10 9:38
 */
class StringMain {
  /**
   * 字符串插值：字符串插值允许使用者将变量引用直接插入处理过的字面字符中。
   * Scala 提供了三种创新的字符串插值方法：`s`,`f` 和 `raw`
   */

  /* 在任何字符串前加上s，就可以直接在串中使用变量 */
  def interpolation_s(str: String) = s"Hello $str"

  /* 在任何字符串字面前加上 f，就可以生成简单的格式化串，功能相似于其他语言中的 printf 函数 */
  def interpolation_f(f: Float) = f"$f%2.2f"

  /* 除了对字面值中的字符不做编码外，raw 插值器与 s 插值器在功能上是相同的 */
  // 变量中包含特殊字符依旧会转码的
  def interpolation_raw(str: String) = raw"$str\n$str"
}

object StringMain extends App {
  /* 单行字符串 */
  val str: String = "Hello World"

  /* 多行字符串 */
  val str2: String =
    """tttt
      xxxx
      yyuyy
      """

  val sm = new StringMain

  println(str)
  println("-------------------------------华丽的分割线-----------------------------------")
  println(str2)
  println("-------------------------------华丽的分割线-----------------------------------")
  println(sm.interpolation_s("superz"))
  /* Hello superz */
  println("-------------------------------华丽的分割线-----------------------------------")
  println(sm.interpolation_f(10.22222222222222222222222222222222222222222f))
  /* 10.22 */
  println("-------------------------------华丽的分割线-----------------------------------")
  println(sm.interpolation_raw("superz"))
  /* superz\nsuperz */
}
