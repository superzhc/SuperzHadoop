package com.github.superzhc.hadoop.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.sources.{BaseRelation, RelationProvider, SchemaRelationProvider, TableScan}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

/**
 * @author superz
 * @create 2021/9/27 14:04
 */
object TushareDemo {

}

class DefaultSource
  extends RelationProvider // 获取参数列表，返回一个 BaseRelation 对象
    with SchemaRelationProvider // 用户可自定义 schema 信息
{
  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = createRelation(sqlContext, parameters, null)

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String], schema: StructType): BaseRelation = {
    val token = parameters.get("token")

    if (token.isEmpty)
      throw new IllegalArgumentException("token is required")

    val apiName = parameters.get("api_name")
    if (apiName.isEmpty)
      throw new IllegalArgumentException("api_name is required")

    val params = parameters.get("param")
    val fields = parameters.get("fields")

    new TushareDataSourceRelation(sqlContext, token.get, apiName.get, params.get, fields.get, schema)
  }
}

class TushareDataSourceRelation(override val sqlContext: SQLContext, val token: String, val apiName: String, val params: String, val fields: String, val userSchema: StructType)
  extends BaseRelation // 展示从 DataFrame 中产生的底层数据源的关系或者表；定义如何产生 Schema 信息
    with TableScan // 对数据的 schema 信息，进行完整扫描，返回一个没有过滤的 RDD
    with Serializable {

  override def schema: StructType = {
    if (userSchema != null) {
      userSchema
    } else {
      val fieldsArr = fields.split(",")

      val structFields = for (field <- fieldsArr)
        yield StructField(field, StringType, nullable = true)

      val schema = StructType(structFields)
      schema
    }
  }

  /**
   * 为了读取数据，需要实现 TableScan，实现 buildScan() 方法
   * 这个方法会将数据以 Row 组成的 RDD 的形式返回数据，每一个 Row 表示一行数据
   *
   * @return
   */
  override def buildScan(): RDD[Row] = {
    null
  }
}
