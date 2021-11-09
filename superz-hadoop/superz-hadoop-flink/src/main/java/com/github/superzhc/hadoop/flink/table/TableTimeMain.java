package com.github.superzhc.hadoop.flink.table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * Flink SQL 中声明 Processing Time 和 Event Time
 * @author superz
 * @create 2021/11/5 17:43
 */
public class TableTimeMain {

    public static void main(String[] args) {
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tEnv=StreamTableEnvironment.create(env);

        // tEnv.executeSql(processingTimeDDL());
        tEnv.executeSql(eventTimeDDL(3));
        String sql="select * from faker";
        Table table=tEnv.sqlQuery(sql);
        table.execute().print();
    }

    public static String processingTimeDDL() {
        return "CREATE TEMPORARY TABLE faker(\n" +
                "            name STRING,\n" +
                "            age INT,\n" +
                "            sex STRING,\n" +
                "            email STRING,\n" +
                "            timestamps TIMESTAMP(3),\n" +
                // Processing Time 定义，窗口可基于这个时间来进行计算
                "            process_time AS PROCTIME()\n" +
                "        )WITH(\n" +
                "            'connector'='faker',\n" +
                "            'rows-per-second'='1',\n" +
                "            'fields.name.expression'='#{Name.name}',\n" +
                "            'fields.age.expression' = '#{number.number_between ''18'',''35''}',\n" +
                "            'fields.sex.expression'='#{regexify ''(男|女){1}''}',\n" +
                "            'fields.email.expression'='#{bothify ''????###@gmail.com''}',\n" +
                "            'fields.timestamps.expression'='#{date.past ''5'',''SECONDS''}'\n" +
                "        )";
    }

    public static String eventTimeDDL(int second) {
        return "CREATE TEMPORARY TABLE faker(\n" +
                "            name STRING,\n" +
                "            age INT,\n" +
                "            sex STRING,\n" +
                "            email STRING,\n" +
                "            timestamps TIMESTAMP(3),\n" +
                // 声明 timestamps 是事件事件属性，并且用延时 second 秒的策略来生成 Watermark
                "            WATERMARK FOR timestamps AS timestamps - INTERVAL '" + second + " ' SECOND\n" +
                "        )WITH(\n" +
                "            'connector'='faker',\n" +
                "            'rows-per-second'='100',\n" +
                "            'fields.name.expression'='#{Name.name}',\n" +
                "            'fields.age.expression' = '#{number.number_between ''18'',''35''}',\n" +
                "            'fields.sex.expression'='#{regexify ''(男|女){1}''}',\n" +
                "            'fields.email.expression'='#{bothify ''????###@gmail.com''}',\n" +
                "            'fields.timestamps.expression'='#{date.past ''5'',''SECONDS''}'\n" +
                "        )";
    }

}
