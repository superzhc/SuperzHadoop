package com.github.superzhc.geo.geotrellis.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 时间工具类
 *
 * @author superz
 * @create 2021/9/13 19:46
 */
object DateUtils {
  /**
   * 时间格式化
   *
   * @param date
   * @param pattern
   * @return
   */
  def format(date: LocalDateTime = LocalDateTime.now(), pattern: String = "yyyyMMddHHmmss"): String = {
    date.format(DateTimeFormatter.ofPattern(pattern))
  }
}
