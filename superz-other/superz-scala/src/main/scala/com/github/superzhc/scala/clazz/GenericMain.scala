package com.github.superzhc.scala.clazz

import java.util.Comparator

/**
 * 泛型
 * 在 Scala 中，类和特质都可以带类型参数，用方括号 `[]` 来定义类型参数
 *
 * @author superz
 * @create 2021/9/9 19:27
 */

class GenericMain[
  /* 类型变量的边界 */
  T <: Comparable[T] /* 限定 T 是 Comparable[T] 或它的子类*/
  , S >: T /* 限定 S 是 T 或 T 的父类 */
](val first: T, val second: S) {
  /* 泛型函数 */
  def getMiddle[K](a: Array[K]) = a(a.length / 2)
}

/**
 * 多重界定
 * 1. 类型变量可以同时有上界和下界 `T >: Lower <: Upper`
 * 2. 一个类型可以约定实现多个特质
 *
 * @param param
 * @tparam T
 */
class MultiBoundsMain[T <: Comparable[T] with Serializable with Cloneable](param: T)

/**
 * 2021年9月11日
 * 泛型定义中的通配符
 * 在 Java 中用问号来指代泛型中不确定类型的定义（如 `List<?>`），单在 Scala 中用下划线来代替它
 *
 * @tparam List
 */
class Wildcard[List[_]]

/**
 * 2021年9月15日 add
 * view bounds（视图界定）
 * 视图界定其实是上边界(`<:`)的加强版本，通过隐式转换将原类型转换成目标类型
 * 简单的理解就是将参数视为目标类型，这过程需要隐式来实现，因此必须实现隐式转换的方法
 */
class ViewBoundsMain[T <% Comparable[T]](param: T) {
  def compare(other: T) = /*param.compareTo(other)*/ -1
}

object ViewBoundsMain {
  implicit def int2Comparable[T](n: T): Comparable[T] = {
    null
  }

  def main(args: Array[String]): Unit = {
    new ViewBoundsMain[Int](10)
  }
}

/**
 * 2021年9月11日
 * context bounds（上下文界定）
 * 这是隐式参数的语法糖
 * 注意：2.8版本以后才新增的
 */
class ContextBoundsMain {
  def max[T](a: T, b: T)(implicit cp: Comparator[T]) = {
    if (cp.compare(a, b) > 0) a else b
  }

  // 上述的函数可以简化为如下：
  def max2[T: Comparator](a: T, b: T) = {
    /* 因为省略了 cp 参数，如果使用隐式参数呢，有如下两个方法 */
    // 方法一：在内部定义函数并声明隐式参数，这种做法只是把外部方法的隐式参数隐藏了，放到内部嵌套函数上；
    def inner(implicit c: Comparator[T]) = c.compare(a, b)

    val res1 = if (inner > 0) a else b

    // 方法二：使用implicitly方法【推荐使用】
    // 用于从冥界召唤隐式值
    val cp = implicitly[Comparator[T]]
    val res2 = if (cp.compare(a, b) > 0) a else b
    res2
  }
}
