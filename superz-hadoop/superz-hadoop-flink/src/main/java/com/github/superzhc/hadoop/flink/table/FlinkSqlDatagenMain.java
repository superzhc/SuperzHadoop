package com.github.superzhc.hadoop.flink.table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import static org.apache.flink.table.api.Expressions.$;

/**
 * @author superz
 * @create 2023/2/8 17:21
 **/
public class FlinkSqlDatagenMain {
    /*
    CREATE TABLE datagen_source(
      id  BIGINT,
      name STRING
    ) WITH (
      'connector' = 'datagen',
      'rows-per-second'='5',
      'fields.s.length'='10'
    )
    */
    public static final String DEMO_SQL = "CREATE TABLE datagen_source(\n" +
            "  id  BIGINT,\n" +
            "  s STRING\n" +
            ") WITH (\n" +
            "  'connector' = 'datagen',\n" +
            "    'rows-per-second'='5',\n" +
            "    'fields.s.length'='10'\n" +
            ")";

    public static void main(String[] args) {
        EnvironmentSettings envSetting = null;
        envSetting = EnvironmentSettings.newInstance()
                // Planner 和 Model
//                .useOldPlanner().inStreamingMode()//
//                .useOldPlanner().inBatchMode()//
//                .useBlinkPlanner().inStreamingMode()//
//                .useBlinkPlanner().inBatchMode()//
                /* 如果/lib目录中只有一种计划器的 jar 包，则可以使用useAnyPlanner */
//                .useAnyPlanner().inStreamingMode()
//                .useAnyPlanner().inBatchMode()
                .build();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        TableEnvironment tableEnv = StreamTableEnvironment.create(env, envSetting);

        tableEnv.executeSql(DEMO_SQL);

        /*获取表的两种方式*/
        // 方式1
        Table t1 = tableEnv.from("datagen_source");
        // 方式2
        Table t2 = tableEnv.sqlQuery("select * from datagen_source");

//        t1.select($("name")).execute().print();
        t2.execute().print();

//        Table table = t1;
//        table.execute().print();
    }
}
