package com.github.superzhc.hadoop.flink.table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author superz
 * @create 2021/10/24 1:26
 */
public class FlinkFakerMain {
    public static void main(String[] args) {
        /*
        CREATE TEMPORARY TABLE test(
            name STRING,
            age INT,
            sex INT
        )WITH(
            'connector'='faker',
            'fields.name.expression'='#{Name.first_name}',
            'fields.age.expression' = '#{number.numberBetween ''1'',''100''}',
            'fields.sex.expression' = '#{options.option ''0'',''1''}',
        )
        */
        String sql="CREATE TEMPORARY TABLE superz(\n" +
                "            `name` STRING,\n" +
                "            `age` INT" +
                //"            `,sex` INT" +
                "        )WITH(" +
                "            'connector'='faker'," +
                "            'rows-per-second'='1'," +
                "            'fields.name.expression'='#{Name.name}'," +
                "            'fields.age.expression' = '#{number.numberBetween ''1'',''100''}'" +
                //"            ',fields.sex.expression' = '#{options.option 0,1}'" +
                "        )";

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        TableEnvironment tableEnv = StreamTableEnvironment.create(env);
        tableEnv.executeSql(sql);
        Table table=tableEnv.sqlQuery("select * from superz");
        table.execute().print();
    }
}
