package com.github.superzhc.hadoop.flink.iceberg;

import com.github.superzhc.common.utils.SystemUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Before;
import org.junit.Test;

/**
 * 无论通过SQL还是Datastream入湖，都必须开启checkpoint
 *
 * @author superz
 * @create 2023/3/15 0:38
 */
public class FlinkIcebergWriteTest {
    static {
        SystemUtils.setEnv("HADOOP_CONF_DIR", "./target/classes/s3local");
    }

    StreamExecutionEnvironment env;
    TableEnvironment tEnv;

    @Before
    public void setUp() throws Exception {
        // 设置环境变量：HADOOP_CONF_DIR=./target/classes

        Configuration conf = new Configuration();
        conf.setString("state.backend", "filesystem");
        conf.setString("state.checkpoints.dir", "s3a://superz/flink/checkpoint");
        conf.setString("state.savepoints.dir", "s3a://superz/flink/savepoint");

        // EnvironmentSettings settings = EnvironmentSettings.newInstance()
        //         .inStreamingMode()
        //         .withConfiguration(conf)
        //         .build();
        // tEnv = TableEnvironment.create(settings);

        env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
        tEnv = StreamTableEnvironment.create(env);

        env.setParallelism(1);
        env.enableCheckpointing(1000);
//        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
//        checkpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
//        checkpointConfig.setMinPauseBetweenCheckpoints(500);
//        checkpointConfig.setCheckpointTimeout(60000);
//        checkpointConfig.setMaxConcurrentCheckpoints(1);
//        checkpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        // 设置Catalog
        String sql = "CREATE CATALOG hadoop_catalog WITH (" +
                "  'type'='iceberg'," +
                "  'catalog-type'='hadoop'," +
                "  'warehouse'='s3a://superz/flink/iceberg'," +
                "  'property-version'='1'" +
                ")";
        tEnv.executeSql(sql);
    }

    @Test
    public void insert() {
        String sql = "INSERT INTO `hadoop_catalog`.`test`.`t1` VALUES (2, 'b')";
        tEnv.executeSql(sql);

        sql = "select * from hadoop_catalog.test.t1";
        tEnv.sqlQuery(sql).printSchema();
    }

    @Test
    public void insertIntoSelect() {
        //tEnv.executeSql("set table.dynamic-table-options.enabled=true");

        String sql = "CREATE TABLE datagen (\n" +
                " f_random INT,\n" +
                " f_random_str STRING\n" +
                ") WITH (\n" +
                " 'connector' = 'datagen',\n" +
                " 'rows-per-second'='5',\n" +
                " 'fields.f_random.min'='1',\n" +
                " 'fields.f_random.max'='1000',\n" +
                " 'fields.f_random_str.length'='10'\n" +
                ")";
        tEnv.executeSql(sql);

        tEnv.executeSql("INSERT INTO hadoop_catalog.test.t_202303161029 SELECT * from datagen");
    }
}
