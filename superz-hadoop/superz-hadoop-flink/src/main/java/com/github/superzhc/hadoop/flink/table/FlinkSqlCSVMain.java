package com.github.superzhc.hadoop.flink.table;

/**
 * @author superz
 * @create 2021/11/5 1:43
 */
public class FlinkSqlCSVMain {
    public static final String CSV_TABLE_SOURCE_DDL = "" +
            "CREATE TABLE csv_source (\n" +
            " user_id bigint,\n" +
            " item_id bigint,\n" +
            " category_id bigint,\n" +
            " behavior varchar,\n" +
            " ts bigint,\n" +
            " proctime as PROCTIME() \n" +
            ") WITH (\n" +
            " 'connector.type' = 'filesystem', -- 指定连接类型\n" +
            " 'connector.path' = 'C:\\Users\\tzmaj\\Desktop\\教程\\3\\UserBehavior.csv',-- 目录 \n" +
            " 'format.type' = 'csv', -- 文件格式 \n" +
            " 'format.field-delimiter' = ',' ,-- 字段分隔符 \n" +
            " 'format.fields.0.name' = 'user_id',-- 第N字段名，相当于表的schema，索引从0开始 \n" +
            " 'format.fields.0.data-type' = 'bigint',-- 字段类型\n" +
            " 'format.fields.1.name' = 'item_id', \n" +
            " 'format.fields.1.data-type' = 'bigint',\n" +
            " 'format.fields.2.name' = 'category_id',\n" +
            " 'format.fields.2.data-type' = 'bigint',\n" +
            " 'format.fields.3.name' = 'behavior', \n" +
            " 'format.fields.3.data-type' = 'String',\n" +
            " 'format.fields.4.name' = 'ts', \n" +
            " 'format.fields.4.data-type' = 'bigint'\n" +
            ")      ";
}
