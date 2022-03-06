package com.github.superzhc.scala.basic

import java.io.IOException
import java.net.MalformedURLException

/**
 * 异常
 *
 * @author superz
 * @create 2021/9/8 17:48
 */
object ExceptionMain {
  /**
   * 异常
   * 1. Scala 的异常并不是受检的，也就是说抛出异常程序不需要立刻就进行捕获
   */

  def main(args: Array[String]): Unit = {

  }

  def exception() = {
    /* 如下抛出异常，函数等都不需要声明 */
    /* throw 表达式有特殊的返回值类型 Nothing */
    throw new IllegalArgumentException("非法参数异常")
  }

  /**
   * 捕获异常的语法采用的是模式匹配的语法
   */
  def exceptionCatch() = {
    try {

    } catch {
      case _: MalformedURLException => println("Bad URL")
      case ex: IOException => ex.printStackTrace()
    } finally {
      // finally 语句不一定会有，如果有一定会执行
    }
  }
}
