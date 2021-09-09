package com.github.superzhc.scala.clazz

/**
 * 泛型
 * 在 Scala 中，类和特质都可以带类型参数，用方括号 `[]` 来定义类型参数
 *
 * @author superz
 * @create 2021/9/9 19:27
 */

class GenericMain[
  T <: Comparable[T] /* 限定 T 是 Comparable[T] 的子类*/
  , S >: T /* 限定 S 是 T 的父类 */
](val first: T, val second: S) {
  /* 泛型函数 */
  def getMiddle[K](a: Array[K]) = a(a.length / 2)
}
