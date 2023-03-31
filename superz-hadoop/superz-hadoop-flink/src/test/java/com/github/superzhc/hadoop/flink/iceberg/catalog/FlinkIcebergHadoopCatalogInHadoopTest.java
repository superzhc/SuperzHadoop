package com.github.superzhc.hadoop.flink.iceberg.catalog;

import com.github.superzhc.common.utils.SystemUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Before;

/**
 * @author superz
 * @create 2023/3/31 10:31
 **/
public class FlinkIcebergHadoopCatalogInHadoopTest {
    static {
        SystemUtils.setEnv("HADOOP_CONF_DIR", "./target/classes/hadoop233");
    }

    StreamExecutionEnvironment env;

    TableEnvironment tEnv;

    @Before
    public void setUp() {
        Configuration conf = new Configuration();
        conf.setString("state.backend", "filesystem");
        conf.setString("state.checkpoints.dir", "hdfs://xgitbigdata/flink/checkpoints");
        conf.setString("state.savepoints.dir", "hdfs://xgitbigdata/flink/savepoints");

        env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
        env.enableCheckpointing(1000);

        tEnv = StreamTableEnvironment.create(env);
    }
}
