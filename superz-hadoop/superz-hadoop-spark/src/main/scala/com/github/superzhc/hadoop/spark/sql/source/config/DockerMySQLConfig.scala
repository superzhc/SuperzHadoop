package com.github.superzhc.hadoop.spark.sql.source.config

/**
 * @author superz
 * @create 2021/12/22 9:55
 */
object DockerMySQLConfig {
  val driver: String = "com.mysql.jdbc.Driver"
  val url: String = "jdbc:mysql://localhost:13306/data_warehouse?useSSL=false&useUnicode=true&characterEncoding=utf-8"
  val username = "root"
  val password = "123456"
}
