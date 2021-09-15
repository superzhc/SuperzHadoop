package com.github.superzhc.scala.json


import io.circe
import io.circe.Json

/**
 * @author superz
 * @create 2021/9/15 11:03
 */
object CirceMain {
  case class People(name: String, age: Int)

  /**
   * 实体转JSON字符串
   *
   * @param entity
   * @tparam T
   * @return
   */
  def entity2Json(entity: People): String = {
    import io.circe.generic.auto._
    import io.circe.syntax._
    val json = entity.asJson.noSpaces
    json
  }

  /**
   * JSON字符串转实体
   *
   * @param json
   * @tparam T
   * @return
   */
  def json2Entity[People](json: String): Either[circe.Error, People] = {
    import io.circe.generic.auto._
    import io.circe.generic.codec._
    import io.circe.parser._
    import io.circe.Decoder
    /* 报错：could not find implicit value for evidence parameter of type io.circe.Decoder[People] */
    /*val entity = decode[People](json)
    entity*/
    null
  }

  def parse(json: String): Either[Error, Json] = {
    val result = parse(json)
    result
  }

  def main(args: Array[String]): Unit = {
    val people = People("zhangsan", 28)
    val json = entity2Json(people)
    println(json)
    // val entity = json2Entity[People](json)
    // println(entity)
    val result=parse(json)

  }
}
