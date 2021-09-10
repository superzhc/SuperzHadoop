package com.github.superzhc.scala.traits

/**
 * 特质
 * 一个类扩展自一个或多个特质，以便使用这些特质提供的服务
 * Scala 提供的特质可以定义抽象方法，也可以定义具体方法
 *
 * @author superz
 * @create 2021/9/9 15:56
 */
trait SimpeTrait {
  /* 2021年9月10日 10点52分 定义常量 */
  val constant: String = "常量"
  /* 2021年9月10日 10点52分 定义抽象变量 */
  var variable: String

  /* 特质完全可以当作Java 接口来使用 */
  //  特质中未实现的方法默认就是抽象方法
  def log(msg: String) // 这是个抽象方法

  /* 带有具体实现的特质 */
  def log2(msg: String) {
    println(msg)
  }

  /* 编译后源码有两部分 */

  /**
   * 接口部分
   * public interface SimpeTrait {
   * void com$github$superzhc$scala$traits$SimpeTrait$_setter_$constant_$eq(String paramString);
   *
   * String constant();
   *
   * String variable();
   *
   * void variable_$eq(String paramString);
   *
   * void log(String paramString);
   *
   * void log2(String paramString);
   * }
   */

  /**
   * 抽象类部分
   * public abstract class SimpeTrait$class {
   * public static void $init$(SimpeTrait $this) {
   * $this.com$github$superzhc$scala$traits$SimpeTrait$_setter_$constant_$eq(");
   * }
   *
   * public static void log2(SimpeTrait $this, String msg) {
   * Predef$.MODULE$.println(msg);
   * }
   * }
   */
}
