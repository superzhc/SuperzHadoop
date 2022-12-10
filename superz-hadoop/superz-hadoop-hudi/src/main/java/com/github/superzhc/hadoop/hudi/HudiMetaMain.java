package com.github.superzhc.hadoop.hudi;

import org.apache.avro.Schema;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hudi.common.fs.FSUtils;
import org.apache.hudi.common.model.HoodieAvroPayload;
import org.apache.hudi.common.model.HoodieTableType;
import org.apache.hudi.common.table.HoodieTableMetaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * @author superz
 * @create 2022/12/7 15:18
 **/
public class HudiMetaMain {
    private static final Logger log = LoggerFactory.getLogger(HudiMetaMain.class);

    public static void main(String[] args) throws Exception {
        // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
        System.setProperty("HADOOP_USER_NAME", "root");

        String suffix = "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String tableName = "superz_java_client" + suffix;
        log.info("表名：" + tableName);
        String basePath = "hdfs://log-platform01:8020/user/superz/hudi/" + tableName;
        String tablePath = "hdfs://log-platform01:8020/user/superz/hudi/" + tableName;

        // 创建Schema
        Schema.Field id = new Schema.Field("id", Schema.create(Schema.Type.INT), null, null);
        Schema.Field title = new Schema.Field("title", Schema.createUnion(Arrays.asList(Schema.create(Schema.Type.STRING), Schema.create(Schema.Type.NULL))), null, null);
        Schema.Field price = new Schema.Field("price", Schema.create(Schema.Type.STRING), null, null);
        Schema.Field sync = new Schema.Field("sync", Schema.create(Schema.Type.STRING), null, null);
        Schema.Field platform = new Schema.Field("platform", Schema.create(Schema.Type.STRING), null, null);
        Schema.Field brief = new Schema.Field("brief", Schema.create(Schema.Type.STRING), null, null);
        Schema.Field ts = new Schema.Field("ts", Schema.create(Schema.Type.LONG), "获取时间戳", null);
        Schema defineSchema = Schema.createRecord(tableName, null, null, false);
        defineSchema.setFields(Arrays.asList(id, title, price, /*sync,*/ platform, brief, ts));
        log.info("Schema：{}", defineSchema.toString());

        Configuration hadoopConf = new Configuration();
        hadoopConf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        Path path = new Path(tablePath);
        FileSystem fs = FSUtils.getFs(tablePath, hadoopConf);
        if (!fs.exists(path)) {
            log.info("路径[{}]不存在", tablePath);
            HoodieTableMetaClient.withPropertyBuilder()
                    .setTableType(HoodieTableType.COPY_ON_WRITE)
                    .setTableName(tableName)
                    // 目前没什么作用
                    .setTableCreateSchema(defineSchema.toString())
                    .setRecordKeyFields("id")
                    .setPreCombineField("ts")
                    .setPartitionFields("platform")
                    // payload 的意义？？？
                    .setPayloadClass(HoodieAvroPayload.class)
//                    .setBootstrapIndexClass(NoOpBootstrapIndex.class.getName())
                    .initTable(hadoopConf, tablePath);
        }
    }
}
