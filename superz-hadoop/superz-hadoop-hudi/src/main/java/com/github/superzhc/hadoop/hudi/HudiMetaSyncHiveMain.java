package com.github.superzhc.hadoop.hudi;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hudi.common.config.TypedProperties;
import org.apache.hudi.hive.HiveSyncConfigHolder;
import org.apache.hudi.hive.HiveSyncTool;
import org.apache.hudi.hive.MultiPartKeysValueExtractor;
import org.apache.hudi.hive.ddl.HiveSyncMode;
import org.apache.hudi.sync.common.HoodieSyncConfig;

/**
 * 同步Hudi元数据到hive
 *
 * 每次写完数据都要进行一次同步，不然hive读取不到写入的分区数据
 *
 * @author superz
 * @create 2022/12/12 14:46
 **/
public class HudiMetaSyncHiveMain {
    public static void main(String[] args) {
        // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
        System.setProperty("HADOOP_USER_NAME", "root");

        String tableName = "superz_java_client_20221212173835";
        String tablePath = "hdfs://log-platform01:8020/user/superz/hudi/" + tableName;

        HiveConf conf = new HiveConf();
        conf.set("hive.metastore.uris", "thrift://log-platform02:9083,thrift://log-platform03:9083");

        TypedProperties properties = new TypedProperties();
        properties.put(HiveSyncConfigHolder.HIVE_SYNC_MODE.key(), HiveSyncMode.HMS.name());
        // properties.put(HiveSyncConfigHolder.HIVE_CREATE_MANAGED_TABLE.key(), true);
        properties.put(HoodieSyncConfig.META_SYNC_DATABASE_NAME.key(), "xgitbigdata");
        properties.put(HoodieSyncConfig.META_SYNC_TABLE_NAME.key(), tableName);
        properties.put(HoodieSyncConfig.META_SYNC_BASE_PATH.key(), tablePath);
        properties.put(HoodieSyncConfig.META_SYNC_PARTITION_EXTRACTOR_CLASS.key(), MultiPartKeysValueExtractor.class.getName());
        properties.put(HoodieSyncConfig.META_SYNC_PARTITION_FIELDS.key(), "platform");

        HiveSyncTool hiveSyncTool = new HiveSyncTool(properties, conf);
        hiveSyncTool.syncHoodieTable();
    }
}
