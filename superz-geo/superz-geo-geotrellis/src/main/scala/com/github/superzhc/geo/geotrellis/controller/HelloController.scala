package com.github.superzhc.geo.geotrellis.controller

import org.springframework.web.bind.annotation.{GetMapping, RestController}

/**
 * @author superz
 * @create 2021/9/22 19:02
 */
@RestController("/hello")
class HelloController {
  @GetMapping()
  def hello(): String = {
    "hello world"
  }
}
