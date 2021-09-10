package com.github.superzhc.scala.clazz

/**
 * sealed 关键字（可以修饰类和特质）
 * 1. 防止继承滥用
 *  密封类提供了一种约束：不能在类定义的文件之外定义任何新的子类
 * 2. 模式匹配时可以进行提示
 * @author superz
 * @create 2021/9/10 15:19
 */
object SealedClass {

}

sealed trait People

case class Chinese() extends People
case class Japanese() extends People
