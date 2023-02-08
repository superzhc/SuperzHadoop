package com.github.superzhc.hadoop.flink.table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

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
      'connector' = 'datagen'
    )
    */
    public static final String DEMO_SQL = "CREATE TABLE datagen_source(\n" +
            "  id  BIGINT,\n" +
            "  name STRING\n" +
            ") WITH (\n" +
            "  'connector' = 'datagen'\n" +
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
        Table table= tableEnv.sqlQuery("select * from datagen_source");
        table.execute().print();
    }
}
