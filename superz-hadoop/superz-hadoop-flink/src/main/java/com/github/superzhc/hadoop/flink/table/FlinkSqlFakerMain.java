package com.github.superzhc.hadoop.flink.table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author superz
 * @create 2021/10/24 1:26
 */
public class FlinkSqlFakerMain {
    /*
        CREATE TEMPORARY TABLE superz(
            name STRING,
            age INT,
            sex STRING,
            email STRING,
            timestamps TIMESTAMP(3)
        )WITH(
            'connector'='faker',
            'rows-per-second'='1',
            'fields.name.expression'='#{Name.name}',
            'fields.age.expression' = '#{number.number_between ''1'',''100''}',
            'fields.sex.expression'='#{regexify ''(男|女){1}''}',
            'fields.email.expression'='#{bothify ''????##@gmail.com''}',
            'fields.timestamps.expression'='#{date.past ''5'',''SECONDS''}'
        )
        */
    public static String FLINK_TABLE_SOURCE_DDL = "CREATE TEMPORARY TABLE superz(\n" +
            "            name STRING,\n" +
            "            age INT,\n" +
            "            sex STRING,\n" +
            "            email STRING,\n" +
            "            timestamps TIMESTAMP(3)\n" +
            "        )WITH(\n" +
            "            'connector'='faker',\n" +
            "            'rows-per-second'='1',\n" +
            "            'fields.name.expression'='#{Name.name}',\n" +
            "            'fields.age.expression' = '#{number.number_between ''18'',''35''}',\n" +
            "            'fields.sex.expression'='#{regexify ''(男|女){1}''}',\n" +
            "            'fields.email.expression'='#{bothify ''????###@gmail.com''}',\n" +
            "            'fields.timestamps.expression'='#{date.past ''5'',''SECONDS''}'\n" +
            "        )";

    public static void main(String[] args) {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism(1);
//        EnvironmentSettings envSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
//        TableEnvironment tableEnv = StreamTableEnvironment.create(env, envSettings);
//        tableEnv.executeSql(FLINK_TABLE_SOURCE_DDL);
//        Table table = tableEnv.sqlQuery("select * from superz where age>20 and age<35");
//        table.execute().print();
    }
}
