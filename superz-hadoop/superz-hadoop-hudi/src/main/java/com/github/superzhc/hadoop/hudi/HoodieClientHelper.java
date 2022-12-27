package com.github.superzhc.hadoop.hudi;

import org.apache.avro.Schema;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.IMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.ql.metadata.Hive;
import org.apache.hudi.common.config.TypedProperties;
import org.apache.hudi.common.fs.FSUtils;
import org.apache.hudi.common.model.HoodieAvroPayload;
import org.apache.hudi.common.model.HoodieTableType;
import org.apache.hudi.common.table.HoodieTableMetaClient;
import org.apache.hudi.exception.HoodieException;
import org.apache.hudi.hive.HiveSyncConfigHolder;
import org.apache.hudi.hive.HiveSyncTool;
import org.apache.hudi.hive.MultiPartKeysValueExtractor;
import org.apache.hudi.hive.ddl.HiveSyncMode;
import org.apache.hudi.sync.common.HoodieSyncConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author superz
 * @create 2022/12/26 13:54
 **/
public class HoodieClientHelper {
    private static final Logger log = LoggerFactory.getLogger(HoodieClientHelper.class);

    /**
     * 初始化 Hudi 表
     *
     * @param basePath        Hudi的存储路径，格式为：[Hudi统一存储路径]/租户Id/表名
     * @param tableName       Hudi表名
     * @param tableType       Hudi表类型，COW和MOR
     * @param schemaStr       Hudi表定义，格式见Avro定义Schema文件内容
     * @param recordKeyFields Hudi表主键，支持多字段
     * @param partitionFields Hudi表分区字段，支持多字段
     * @param preCombineField Hudi表预合并字段，一般为 ts
     * @throws IOException
     */
    public static void initTable(String basePath, String tableName, HoodieTableType tableType, String schemaStr, String[] recordKeyFields, String[] partitionFields, String preCombineField) throws IOException {
        Schema schema = new Schema.Parser().parse(schemaStr);
        initTable(basePath, tableName, tableType, schema, recordKeyFields, partitionFields, preCombineField);
    }

    /**
     * 初始化 Hudi 表
     *
     * @param basePath        Hudi的存储路径，格式为：[Hudi统一存储路径]/租户Id/表名
     * @param tableName       Hudi表名
     * @param tableType       Hudi表类型，COW和MOR
     * @param schema          Hudi表定义
     * @param recordKeyFields Hudi表主键，支持多字段
     * @param partitionFields Hudi表分区字段，支持多字段
     * @param preCombineField Hudi表预合并字段，一般为 ts
     * @throws IOException
     */
    public static void initTable(String basePath, String tableName, HoodieTableType tableType, Schema schema, String[] recordKeyFields, String[] partitionFields, String preCombineField) throws IOException {
        // 测试用，如果是windows系统，设置为root用户
        if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
            System.setProperty("HADOOP_USER_NAME", "root");
        }

        Configuration hadoopConf = new Configuration();
        hadoopConf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        Path path = new Path(basePath);
        FileSystem fs = FSUtils.getFs(basePath, hadoopConf);
        if (!fs.exists(path)) {
            log.info("路径[{}]不存在", basePath);
            HoodieTableMetaClient.withPropertyBuilder()
                    .setTableType(tableType/*HoodieTableType.COPY_ON_WRITE*/)
                    .setTableName(tableName)
                    .setTableCreateSchema(schema.toString())
                    .setRecordKeyFields(String.join(",", recordKeyFields))
                    .setPreCombineField(preCombineField)
                    .setPartitionFields(String.join(",", partitionFields))
                    .setPayloadClass(HoodieAvroPayload.class)
                    .initTable(hadoopConf, basePath);
        }
    }

    /**
     * Hudi 表同步到 Hive
     *
     * @param hiveMetastoreUris hive.metastore.uris值
     * @param basePath          Hudi的存储路径，格式为：[Hudi统一存储路径]/租户Id/表名
     * @param database          同步到Hive的数据库
     * @param tableName         同步到Hive的表名
     * @param partitionFields   分区字段
     */
    public static void syncHive(String hiveMetastoreUris, String basePath, String database, String tableName, String[] partitionFields) {
        HiveConf conf = new HiveConf();
        conf.set("hive.metastore.uris", hiveMetastoreUris);

        TypedProperties properties = new TypedProperties();
        properties.put(HiveSyncConfigHolder.HIVE_SYNC_MODE.key(), HiveSyncMode.HMS.name());
        // 是否为hive内部表，默认为外部表
        // properties.put(HiveSyncConfigHolder.HIVE_CREATE_MANAGED_TABLE.key(), true);
        // 同步到hive的数据库，默认为 default
        properties.put(HoodieSyncConfig.META_SYNC_DATABASE_NAME.key(), database);
        properties.put(HoodieSyncConfig.META_SYNC_TABLE_NAME.key(), tableName);
        properties.put(HoodieSyncConfig.META_SYNC_BASE_PATH.key(), basePath);
        properties.put(HoodieSyncConfig.META_SYNC_PARTITION_EXTRACTOR_CLASS.key(), MultiPartKeysValueExtractor.class.getName());
        properties.put(HoodieSyncConfig.META_SYNC_PARTITION_FIELDS.key(), String.join(",", partitionFields));

        HiveSyncTool hiveSyncTool = new HiveSyncTool(properties, conf);
        hiveSyncTool.syncHoodieTable();
    }

    /**
     * 删除 Hudi 表，若已将 Hudi 元数据同步到 Hive 中，也会删除 Hive 元数据信息
     * 【注：Hive元数据删除目前仅支持 COW】
     *
     * @param hiveMetastoreUris hive.metastore.uris值
     * @param basePath          Hudi的存储路径，格式为：[Hudi统一存储路径]/租户Id/表名
     * @param database          同步到Hive的数据库
     * @param tableName         同步到Hive的表名
     * @throws Exception
     */
    public static void deleteTable(String hiveMetastoreUris, String basePath, String database, String tableName) throws Exception {
        /*删除hdfs*/
        // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
        System.setProperty("HADOOP_USER_NAME", "root");

        Configuration hadoopConf = new Configuration();
        hadoopConf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

        FileSystem fs = FSUtils.getFs(basePath, hadoopConf);
        try {
            Path metadataTablePath = new Path(basePath);
            if (fs.exists(metadataTablePath)) {
                fs.delete(metadataTablePath, true);
            }
        } catch (Exception e) {
            throw new HoodieException("Failed to remove table from path " + basePath, e);
        }

        /*删除hive元数据*/
        HiveConf conf = new HiveConf();
        conf.set("hive.metastore.uris", hiveMetastoreUris);
        IMetaStoreClient client = Hive.get(conf).getMSC();

        boolean isExists = client.tableExists(database, tableName);
        if (isExists) {
            client.dropTable(database, tableName);
        }
    }

    /**
     * 新增字段，目前仅支持 COW
     * 【注：本质上是对同步到Hive表新增字段】
     *
     * @param hiveMetastoreUris hive.metastore.uris值
     * @param database          同步到Hive的数据库
     * @param tableName         同步到Hive的表名
     * @param columnName        新增列的名称
     * @param columnType        新增列的类型
     * @param columnComment     新增列的注释
     * @throws Exception
     */
    public static void addColumn(String hiveMetastoreUris, String database, String tableName, String columnName, String columnType, String columnComment) throws Exception {
        HiveConf conf = new HiveConf();
        conf.set("hive.metastore.uris", hiveMetastoreUris);
        IMetaStoreClient client = Hive.get(conf).getMSC();

        Table table = client.getTable(database, tableName);
        FieldSchema newField = new FieldSchema(columnName, columnType, columnComment);
        table.getSd().addToCols(newField);
        client.alter_table(database, tableName, table);
    }

    public static void main(String[] args) throws Exception {
        String hiveMetastoreUris = "thrift://log-platform03:9083";
        String hudiBasePath = "hdfs://log-platform01:8020/user/superz/hudi";
        String tanent = "xgitdmp";
        String tableName = "superz_java_client_20221226001";
        String basePath = String.format("%s/%s/%s", hudiBasePath, tanent, tableName);

        Schema schema = Schema.createRecord(tableName, null, null, false);
        Schema.Field id = new Schema.Field("id", Schema.create(Schema.Type.STRING), "uuid", null);
        Schema.Field title = new Schema.Field("title", Schema.create(Schema.Type.STRING), "标题", null);
        Schema.Field url = new Schema.Field("url", Schema.create(Schema.Type.STRING), "地址", null);
        Schema.Field r = new Schema.Field("r", Schema.create(Schema.Type.STRING), "", null);
        Schema.Field ts = new Schema.Field("ts", Schema.create(Schema.Type.LONG), "时间戳", null);
        List<Schema.Field> fields = Arrays.asList(id, title, url, r, ts);
        schema.setFields(fields);

//        initTable(basePath, tableName, HoodieTableType.MERGE_ON_READ, schema, new String[]{"id"}, new String[]{"r"}, "ts");
//
//        syncHive(hiveMetastoreUris,basePath,"default",tableName,new String[]{"r"});

//        addColumn(hiveMetastoreUris, "default", tableName, "col1", "string", "新增列1");

        deleteTable(hiveMetastoreUris, basePath, "default", tableName);
    }
}