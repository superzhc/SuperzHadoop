package com.github.superzhc.hudi.flink;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class HudiFlinkMain {
    public static void main(String[] args) {
        Configuration conf=new Configuration();
        conf.setString("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(conf);

        EnvironmentSettings envSetting = null;
        envSetting = EnvironmentSettings.newInstance()
                .useBlinkPlanner().inStreamingMode()
                .build();
        TableEnvironment tableEnv = StreamTableEnvironment.create(env, envSetting);

        // 不可用~~~
//        tableEnv.executeSql("CREATE TABLE t1(\n" +
//                "                   uuid VARCHAR(20),\n" +
//                "                   name VARCHAR(20),\n" +
//                "                   age INT,\n" +
//                "                   ts  TIMESTAMP(3),\n" +
//                "                   `partition` VARCHAR(20)\n" +
//                ")\n" +
//                "    PARTITIONED BY(`partition`)\n" +
//                "WITH(\n" +
//                "'connector'='hudi',\n" +
//                "'path'='hdfs://localhost:9000/user/hudi/t1',\n" +
//                "'table.type'='MERGE_ON_READ'\n" +
//                ")");
//
//        Table table=tableEnv.sqlQuery("select * from t1");
//        table.execute().print();
    }
}
