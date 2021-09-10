package com.github.superzhc.scala.matchs

/**
 * @author superz
 * @create 2021/9/9 17:41
 */
object SimpleMatch extends App {
  var sign: Int = _
  val ch: Char = '+'

  ch match {
    case '+' => sign = 1
    case '-' => sign = -1
    /* case _ 与 default 等效，这回捕获所有其他的项。如果不写这个，在没有模式能匹配时，代码会抛出 MatchError 异常 */
    case _ => sign = 0
  }


}
