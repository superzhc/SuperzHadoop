package com.github.superzhc.hadoop.hudi;

import com.github.superzhc.hadoop.hudi.data.AbstractData;
import com.github.superzhc.hadoop.hudi.data.BiCiDoData;
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

/**
 * @author superz
 * @create 2022/12/7 15:18
 **/
public class HudiMetaMain {
    private static final Logger log = LoggerFactory.getLogger(HudiMetaMain.class);

    public static void create(AbstractData data) throws Exception {
        String tableName = data.getTableName();
        log.info("table name：{}", tableName);

        String tablePath = data.getBasePath();
        log.info("hudi base path：{}", tablePath);

        Schema schema = data.getSchema();
        log.info("schema:{}", schema);

        // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
        System.setProperty("HADOOP_USER_NAME", "root");

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
                    .setTableCreateSchema(schema.toString())
                    .setRecordKeyFields(data.getRecordKeyFields())
                    .setPreCombineField(data.getPreCombineField())
                    .setPartitionFields(data.getPartitionFields())
                    .setPayloadClass(HoodieAvroPayload.class)
//                    .setBootstrapIndexClass(NoOpBootstrapIndex.class.getName())
                    .initTable(hadoopConf, tablePath);
        }
    }

    public static void main(String[] args) throws Exception {
        BiCiDoData data = new BiCiDoData();
        create(data);
    }
}
