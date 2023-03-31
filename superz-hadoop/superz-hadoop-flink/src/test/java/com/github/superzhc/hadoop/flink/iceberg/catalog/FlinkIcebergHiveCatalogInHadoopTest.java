package com.github.superzhc.hadoop.flink.iceberg.catalog;

import com.github.superzhc.common.utils.SystemUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/3/31 9:12
 **/
public class FlinkIcebergHiveCatalogInHadoopTest {
    static {
        // 全局配置HADOOP配置文件所在路径
        SystemUtils.setEnv("HADOOP_CONF_DIR", "./target/classes/hadoop233");

        // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
        System.setProperty("HADOOP_USER_NAME", "root");
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

    @Test
    public void catalog() {
        String sql = "CREATE CATALOG iceberg_hive WITH (" +
                "  'type'='iceberg'," +
                "  'catalog-type'='hive'," +
                "  'uri'='thrift://10.90.15.221:9083,thrift://10.90.15.233:9083'," +
                "  'warehouse'='hdfs://xgitbigdata/usr/xgit/hive/warehouse/'," +
//                // 创建表的时候需要配置hadoop的配置文件：core-site.xml hdfs-site.xml
//                "  'hadoop-conf-dir'='./target/classes/hadoop233'," +
                "  'clients'='5'," +
                "  'property-version'='1'" +
                ")";
        tEnv.executeSql(sql);
        tEnv.executeSql("USE CATALOG iceberg_hive");
        tEnv.executeSql("SHOW DATABASES").print();
    }

    @Test
    public void createDatabase() {
        catalog();

        tEnv.executeSql("CREATE DATABASE flink_iceberg_superz");
        tEnv.executeSql("SHOW DATABASES").print();
    }

    @Test
    public void createTable() {
        catalog();

        String sql = "CREATE TABLE iceberg_hive.flink_iceberg_superz.t_20230331 (" +
                "    id BIGINT COMMENT 'unique id'," +
                "    data STRING" +
                ")" +
                // 设置注释
                "   COMMENT '测试表'" +
                // 设置分区
                "   PARTITIONED BY (data)" +
                // table configuration
                "   WITH ('format-version'='2', 'write.upsert.enabled'='true')";
        tEnv.executeSql(sql);
    }

    public void createSupportUpsertTable() {
        catalog();

        String sql = "CREATE TABLE iceberg_hive.flink.t_20230331_001 (" +
                "    id BIGINT COMMENT 'unique id'," +
                "    data STRING" +
                // upsert表需要设置iceberg主键
                "    ,PRIMARY KEY(`id`) NOT ENFORCED" +
                ")" +
                // 设置注释
                "   COMMENT '测试表'" +
                // 设置分区
                "   PARTITIONED BY (data)" +
                // table configuration，对于upsert需要设置如下的参数
                "   WITH ('format-version'='2', 'write.upsert.enabled'='true')";
        tEnv.executeSql(sql);
    }

    // 读取已存在的表的效果会比较好
    @Test
    public void tableByConnector() {
        String sql = "CREATE TABLE my_table (" +
                "    id INT," +
                "    name STRING" +
                "   ,PRIMARY KEY(id) NOT ENFORCED" +
                ") WITH (" +
                "    'connector'='iceberg'," +
                "    'catalog-name'='iceberg_hive'," +
                "    'catalog-type'='hive'," +
                "    'catalog-database'='flink_iceberg_superz'," +
                "    'catalog-table'='t_20230331_002'," +
                "    'uri'='thrift://10.90.15.221:9083,thrift://10.90.15.233:9083'," +
//                "    'hadoop-conf-dir'='./target/classes/hadoop233'," +
                "    'warehouse'='hdfs://xgitbigdata/usr/xgit/hive/warehouse/'" +
                ")";
        tEnv.executeSql(sql);
        tEnv.executeSql("SELECT * FROM my_table").print();
    }
}
