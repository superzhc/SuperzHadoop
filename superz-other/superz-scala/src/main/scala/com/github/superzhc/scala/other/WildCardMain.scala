package com.github.superzhc.scala.other

/**
 * 下划线用法
 * 详细见：<a>https://stackoverflow.com/questions/8000903/what-are-all-the-uses-of-an-underscore-in-scala</a>
 *
 * @author superz
 * @create 2021/9/11 15:58
 */
object WildCardMain {
  /*
  import scala._    // Wild card -- all of Scala is imported
  import scala.{ Predef => _, _ } // Exception, everything except Predef
  def f[M[_]]       // Higher kinded type parameter
  def f(m: M[_])    // Existential type
  _ + _             // Anonymous function placeholder parameter
  m _               // Eta expansion of method into method value
  m(_)              // Partial function application
  _ => 5            // Discarded parameter
  case _ =>         // Wild card pattern -- matches anything
  val (a, _) = (1, 2) // same thing
  for (_ <- 1 to 10)  // same thing
  f(xs: _*)         // Sequence xs is passed as multiple parameters to f(ys: T*)
  case Seq(xs @ _*) // Identifier xs is bound to the whole matched sequence
  var i: Int = _    // Initialization to the default value
  def abc_<>!       // An underscore must separate alphanumerics from symbols on identifiers
  t._2              // Part of a method name, such as tuple getters
  1_000_000         // Numeric literal separator (Scala 2.13+)
   */
}
