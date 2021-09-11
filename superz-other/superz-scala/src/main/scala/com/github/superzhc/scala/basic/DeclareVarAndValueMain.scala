package com.github.superzhc.scala.basic

/**
 * 声明变量和值
 *
 * @author superz
 * @create 2021/9/8 15:44
 */
object DeclareVarAndValueMain {

  /**
   * 定义变量的类型的语法格式如下：
   * var VariableName : DataType [=  Initial Value]
   *
   * 或
   *
   * val VariableName : DataType [=  Initial Value]
   */

  // 2021年9月11日 声明变量的时候必须要初始化默认值，若不知道设置什么值比较好，可以使用下划线来替代
  // 注意：要声明好类型，不然编译器会提示报错，因为 Scala 无法设置类型的默认值
  var v3: String = _
  var v4: Int = _

  def main(args: Array[String]): Unit = {
    /* val 定义的变量实际上是一个常量 */
    val v1 = "test"
    // IDE会报错，因为无法对一个常量进行二次赋值
    // v1="test12" // ×

    /* var 定义的变量是可变的变量 */
    var v2 = "test"
    v2 = "test2" // √

    /* 定义变量的时候指定类型，若不指定，则有表达式自行进行推导 */
    val v3: String = "test"
  }
}
