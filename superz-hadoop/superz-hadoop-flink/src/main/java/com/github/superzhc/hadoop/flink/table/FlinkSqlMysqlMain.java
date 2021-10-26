package com.github.superzhc.hadoop.flink.table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author superz
 * @create 2021/10/25 11:51
 */
public class FlinkSqlMysqlMain {
    public static void main(String[] args) {
        /*
        CREATE TABLE statistics_vehicle_model(
            id              int,
            statistics_date date,
            start_time      timestamp,
            end_time        timestamp,
            model_type      int,
            vehicle_num     int,
            del_flag        tinyint,
            update_time     timestamp,
            create_time     timestamp
        ) WITH(
            'connector'='jdbc',
            'driver'='com.mysql.cj.jdbc.Driver',
            'url'='jdbc:mysql://127.0.0.1:3306/superz_hadoop?zeroDateTimeBehavior=convertToNull&serverTimezone=GMT%2b8&useSSL=true',
            'username'='root',
            'password'='123456',
            'table-name'='t_statistics_vehiclemodel'
        )
        */
        String sql = "CREATE TABLE statistics_vehicle_model(\n" +
                "            id              int,\n" +
                "            statistics_date date,\n" +
                "            start_time      timestamp,\n" +
                "            end_time        timestamp,\n" +
                "            model_type      int,\n" +
                "            vehicle_num     int,\n" +
                "            del_flag        tinyint,\n" +
                "            update_time     timestamp,\n" +
                "            create_time     timestamp\n" +
                "        ) WITH(\n" +
                "            'connector'='jdbc',\n" +
                "            'driver'='com.mysql.cj.jdbc.Driver',\n" +
                "            'url'='jdbc:mysql://localhost:3306/superz_hadoop?zeroDateTimeBehavior=convertToNull&serverTimezone=GMT%2b8&useSSL=true',\n" +
                "            'username'='root',\n" +
                "            'password'='123456',\n" +
                "            'table-name'='t_statistics_vehiclemodel'\n" +
                "        )";

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);
        tEnv.executeSql(sql);
        Table table = tEnv.sqlQuery("select * from statistics_vehicle_model");
        TableResult ts=table.execute();
        ts.print();
    }
}
