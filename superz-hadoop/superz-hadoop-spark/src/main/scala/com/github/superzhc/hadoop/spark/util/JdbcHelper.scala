package com.github.superzhc.hadoop.spark.util

import java.sql.{Connection, DatabaseMetaData, DriverManager, PreparedStatement, ResultSet, SQLException, Statement}
import java.util.Properties
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * 统一公共common中的JdbcHelper
 * @author superz
 * @create 2021/12/21 11:24
 */
//@deprecated
//class JdbcHelper(val driver: String, val url: String, val username: String, val password: String) {
//
//  private var conn: Connection = null
//
//  def this(driver: String, url: String) {
//    this(driver, url, null, null)
//  }
//
//  def getConnection: Connection = {
//    try {
//      if (null == conn || conn.isClosed) {
//        this.synchronized {
//          if (null == conn || conn.isClosed) {
//            Class.forName(driver)
//            val info = new Properties
//            if (null != username && username.trim.length > 0) {
//              info.put("user", username)
//              info.put("password", password)
//            }
//            conn = DriverManager.getConnection(url, info)
//          }
//        }
//      }
//    } catch {
//      case e => throw new RuntimeException(e)
//    }
//    conn
//  }
//
//  /**
//   * 释放连接
//   *
//   * @param conn
//   */
//  private def freeConnection(conn: Connection): Unit = {
//    try if (null != conn) conn.close()
//    catch {
//      case e: SQLException =>
//        throw new RuntimeException(e)
//    }
//  }
//
//  /**
//   * 释放statement
//   *
//   * @param statement
//   */
//  private def freeStatement(statement: Statement): Unit = {
//    try if (null != statement) statement.close()
//    catch {
//      case e: SQLException =>
//        throw new RuntimeException(e)
//    }
//  }
//
//  /**
//   * 释放resultset
//   *
//   * @param rs
//   */
//  private def freeResultSet(rs: ResultSet): Unit = {
//    try if (null != rs) rs.close()
//    catch {
//      case e: SQLException =>
//        throw new RuntimeException(e)
//    }
//  }
//
//  /**
//   * 释放资源
//   *
//   * @param conn
//   * @param statement
//   * @param rs
//   */
//  def free(conn: Connection, statement: Statement, rs: ResultSet): Unit = {
//    freeResultSet(rs)
//    freeStatement(statement)
//    freeConnection(conn)
//  }
//
//  /**
//   * 判断表是否存在
//   *
//   * @param table
//   * @return
//   */
//  def exist(table: String): Boolean = {
//    var rs: ResultSet = null
//    try {
//      val metaData = getConnection.getMetaData
//      rs = metaData.getTables(conn.getCatalog, conn.getSchema, table, Array[String]("TABLE"))
//      rs.next
//    } catch {
//      case e: Exception =>
//        println("判断表[" + table + "]是否存在异常，", e)
//        false
//    } finally free(null, null, rs)
//  }
//
//  /**
//   * 查询sql列表数据
//   *
//   * @param sql
//   * @param params
//   * @return
//   */
//  def query(sql: String, params: Any*): List[mutable.Map[String, Any]] = {
//    var pstmt: PreparedStatement = null
//    var rs: ResultSet = null
//    try {
//      println("查询语句：{}", sql)
//      pstmt = getConnection.prepareStatement(sql)
//      if (null != params && params.length != 0) {
//        println("查询参数：{}", params.mkString(","))
//        for (i <- 0 to params.length) {
//          pstmt.setObject(i + 1, params(i))
//        }
//      }
//      rs = pstmt.executeQuery
//      JdbcHelper.result2ListMap(rs)
//    } catch {
//      case ex: SQLException => println("查询异常", ex)
//        null
//    } finally free(null, pstmt, rs)
//  }
//
//  /**
//   * 总计
//   *
//   * @param sql
//   * @param params
//   * @param < T>
//   * @return
//   */
//  def aggregate[T](sql: String, params: Any*): T = {
//    var pstmt: PreparedStatement = null
//    var rs: ResultSet = null
//    var result: Any = null
//    try {
//      println("查询语句：{}", sql)
//      pstmt = getConnection.prepareStatement(sql)
//      if (null != params && params.length != 0) {
//        println("查询参数：{}", params.mkString(","))
//        for (i <- 0 to params.length) {
//          pstmt.setObject(i + 1, params(i))
//        }
//      }
//      rs = pstmt.executeQuery
//      if (rs.next) {
//        result = rs.getObject(1)
//      }
//    } catch {
//      case ex: SQLException =>
//        println("查询异常", ex)
//    } finally free(null, pstmt, rs)
//    result.asInstanceOf[T]
//  }
//}
//
//object JdbcHelper {
//  def driverName(url: String): String = url match {
//    case _ if url.startsWith("jdbc:mysql:") => "com.mysql.jdbc.Driver"
//    case _ if url.startsWith("jdbc:microsoft:sqlserver:") => "com.microsoft.sqlserver.jdbc.SQLServerDriver"
//    case _ if url.startsWith("jdbc:oracle:thin:") => "oracle.jdbc.driver.OracleDriver"
//    case _ if url.startsWith("jdbc:postgresql:") => "org.postgresql.Driver"
//    case _ if url.startsWith("jdbc:odbc:") => "sun.jdbc.odbc.JdbcOdbcDriver"
//    case _ => null
//  }
//
//  def result2ListMap(rs: ResultSet) = {
//    val list = new ListBuffer[mutable.Map[String, Any]]
//    while (rs.next) {
//      val map = mutable.Map[String, Any]();
//      val metaData = rs.getMetaData
//      val numberOfColumns = metaData.getColumnCount
//      for (i <- 1 until numberOfColumns)
//        map += (metaData.getColumnLabel(i) -> rs.getObject(i))
//
//      list.append(map)
//    }
//    list.toList
//  }
//
//  def apply(driver: String, url: String, username: String, password: String): JdbcHelper = new JdbcHelper(driver, url, username, password)
//
//  def apply(url: String, username: String, password: String): JdbcHelper = new JdbcHelper(driverName(url), url, username, password)
//
//  def apply(driver: String, url: String): JdbcHelper = new JdbcHelper(url, url)
//
//  def apply(url: String): JdbcHelper = new JdbcHelper(driverName(url), url)
//}
