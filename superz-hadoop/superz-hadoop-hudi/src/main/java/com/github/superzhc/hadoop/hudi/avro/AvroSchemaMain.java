package com.github.superzhc.hadoop.hudi.avro;

import org.apache.avro.Schema;

import java.util.Arrays;

/**
 * @author superz
 * @create 2022/12/8 23:49
 */
public class AvroSchemaMain {
    /**
     * Avro Schema 格式是 json
     * <p>
     * 数据类型
     * - 原生类型：“null”，“boolean”，“int”，“long”，“float”，“double”，“bytes”，“string”；
     * - 复合类型：“record”，“enum”，“array”，“map”，“union”，“fixed”。
     * <p>
     * record：这个和java中的“class”有同等的意义，它支持如下属性：
     * 1. name：必要属性，表示record的名称，在java生成代码时作为类的名称。
     * 2. namespace：限定名，在java生成代码时作为package的名字。其中namespace + name最终构成record的全名。
     * 3。doc：可选，文档信息，备注信息。
     * 4。aliases：别名
     * 5。fields：field列表，严格有序。每个filed又包括“name”、“doc”、“type”、“default”、“order”、“aliases”几个属性。
     * 其中在fields列表中每个filed应该拥有不重复的name，“type”表示field的数据类型。
     * “default”很明显，用来表示field的默认值，当reader读取时，当没有此field时将会采用默认值；“order”：可选值，排序（ascending、descending、ignore），在Mapreduce集成时有用。
     * “aliases”别名，JSON Array，表示此field的别名列表。
     * 示例：
     * {
     * "type": "record",
     * "name": "User",
     * "namespace":"com.test.avro",
     * "aliases": ["User1","User2"],
     * "fields" : [
     * {"name": "age", "type": "int","default":10},
     * {"name": "email", "type": ["null", "string"]}
     * ]
     * }
     * enum：枚举类型
     * {
     * "type": "enum",
     * "name": "Suit",
     * "symbols" : ["SPADES", "HEARTS", "DIAMONDS", "CLUBS"]
     * }
     * array：数组
     * fixed：表示field的值的长度为“固定值”，“size”属性表示值的字节长度
     */

    public static void main(String[] args) {

        // 创建列
        Schema.Field field1 = new Schema.Field("col1", Schema.create(Schema.Type.STRING), "", null);
        // 要么string类型，要么为null
        Schema.Field field2 = new Schema.Field("col2", Schema.createUnion(Arrays.asList(Schema.create(Schema.Type.STRING), Schema.create(Schema.Type.NULL))), "", null);

        // 创建record
        Schema record = Schema.createRecord(Arrays.asList(field1, field2));
        System.out.println(record.toString());
    }
}
