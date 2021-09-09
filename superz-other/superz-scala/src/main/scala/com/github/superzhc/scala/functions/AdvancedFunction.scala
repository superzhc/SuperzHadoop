package com.github.superzhc.scala.functions

/**
 * 高级函数
 *
 * @author superz
 * @create 2021/9/9 17:19
 */
object AdvancedFunction {
  /* 参数为函数的函数 */
  // 该函数可以是任何接受 Double 并返回 Double 的函数
  // 函数类型的语法：(参数类型) => 结果类型
  def valueAtOneQuarter(f: (Double) => Double) = f(0.25)
}
