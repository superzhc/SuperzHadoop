package com.github.superzhc.scala.db

import java.sql.{Connection, DriverManager, ResultSet, Statement}

/**
 * JDBC 连接示例
 *
 * @author superz
 * @create 2021/9/10 14:45
 */
object ScalaJDBC extends App {
  val url = "jdbc:mysql://localhost:3306/superz"
  val username = "root"
  val password = "123456"

  var connection: Connection = null
  var statement: Statement = null
  var resultSet: ResultSet = null

  try {
    Class.forName("com.mysql.jdbc.Driver")

    connection = DriverManager.getConnection(url, username, password)
    statement = connection.createStatement()
    resultSet = statement.executeQuery("select host, user from user")
    while (resultSet.next()) {
      val host = resultSet.getString("host")
      val user = resultSet.getString("user")
      println(s"$user, $host")
    }
  } catch {
    case e: Exception => println(e.printStackTrace())
  } finally {
    connection.close()
  }
}
