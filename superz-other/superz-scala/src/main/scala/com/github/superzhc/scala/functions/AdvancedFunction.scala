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

  def fun1[T](lst: List[T]) = {
    /* 匿名函数 */
    lst.map(x => x + "superz")

    /**
     * 匿名函数参数占位符
     * 当匿名函数传递给方法或其他函数时，如果该匿名函数的参数在=>的右侧只出现一次，那么就可以省略=>，并将参数用下划线代替。
     * 这对一元函数和二元函数都适用
     */
    // 等价于上面的调用
    lst.map(_ + "superz")
  }
}
