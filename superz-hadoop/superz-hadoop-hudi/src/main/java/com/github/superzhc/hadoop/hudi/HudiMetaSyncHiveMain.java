package com.github.superzhc.hadoop.hudi;

import com.github.superzhc.hadoop.hudi.data.AbstractData;
import com.github.superzhc.hadoop.hudi.data.BiCiDoData;
import com.github.superzhc.hadoop.hudi.data.FundHistoryData;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hudi.common.config.TypedProperties;
import org.apache.hudi.hive.HiveSyncConfigHolder;
import org.apache.hudi.hive.HiveSyncTool;
import org.apache.hudi.hive.MultiPartKeysValueExtractor;
import org.apache.hudi.hive.ddl.HiveSyncMode;
import org.apache.hudi.sync.common.HoodieSyncConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.superzhc.hadoop.hudi.data.AbstractData.DEFAULT_HIVE_DATABASE;
import static com.github.superzhc.hadoop.hudi.data.AbstractData.DEFAULT_HIVE_METASTORE_URIS;

/**
 * 同步Hudi元数据到hive
 * <p>
 * 每次写完数据都要进行一次同步，不然hive读取不到写入的分区数据
 *
 * @author superz
 * @create 2022/12/12 14:46
 **/
public class HudiMetaSyncHiveMain {
    private static final Logger log = LoggerFactory.getLogger(HudiMetaSyncHiveMain.class);

    public static void sync(AbstractData data) {
        // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
        System.setProperty("HADOOP_USER_NAME", "root");

        String tableName = data.getTableName();
        String tablePath = data.getBasePath();

        HiveConf conf = new HiveConf();
        conf.set("hive.metastore.uris", DEFAULT_HIVE_METASTORE_URIS);

        TypedProperties properties = new TypedProperties();
        properties.put(HiveSyncConfigHolder.HIVE_SYNC_MODE.key(), HiveSyncMode.HMS.name());
        // 是否为hive内部表，默认为外部表
        // properties.put(HiveSyncConfigHolder.HIVE_CREATE_MANAGED_TABLE.key(), true);
        // 同步到hive的数据库，默认为 default
        properties.put(HoodieSyncConfig.META_SYNC_DATABASE_NAME.key(), DEFAULT_HIVE_DATABASE);
        properties.put(HoodieSyncConfig.META_SYNC_TABLE_NAME.key(), tableName);
        properties.put(HoodieSyncConfig.META_SYNC_BASE_PATH.key(), tablePath);
        properties.put(HoodieSyncConfig.META_SYNC_PARTITION_EXTRACTOR_CLASS.key(), MultiPartKeysValueExtractor.class.getName());
        if (null != data.getPartitionFields() && data.getPartitionFields().trim().length() > 0) {
            properties.put(HoodieSyncConfig.META_SYNC_PARTITION_FIELDS.key(), data.getPartitionFields());
        }

        HiveSyncTool hiveSyncTool = new HiveSyncTool(properties, conf);
        hiveSyncTool.syncHoodieTable();
    }

    public static void main(String[] args) {
        // superz_java_client_20221230103829
        String ts = "20221230173512";
        AbstractData data = AbstractData.generate(FundHistoryData.class, ts);
        sync(data);
    }
}
