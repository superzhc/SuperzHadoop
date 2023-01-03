package com.github.superzhc.hadoop.hudi;

import com.github.superzhc.hadoop.hudi.data.AbstractData;
import com.github.superzhc.hadoop.hudi.data.BiCiDoData;
import com.github.superzhc.hadoop.hudi.data.FundHistoryData;
import org.apache.avro.Schema;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hudi.common.fs.FSUtils;
import org.apache.hudi.common.model.HoodieAvroPayload;
import org.apache.hudi.common.model.HoodieTableType;
import org.apache.hudi.common.table.HoodieTableMetaClient;
import org.apache.hudi.exception.HoodieException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
            HoodieTableMetaClient.PropertyBuilder builder = HoodieTableMetaClient.withPropertyBuilder()
                    .setTableType(HoodieTableType.COPY_ON_WRITE)
                    .setTableName(tableName)
                    .setTableCreateSchema(schema.toString())
                    .setRecordKeyFields(data.getRecordKeyFields())
                    .setPayloadClass(HoodieAvroPayload.class)
//                    .setBootstrapIndexClass(NoOpBootstrapIndex.class.getName())
                    ;

            if (null != data.getPreCombineField() && data.getPreCombineField().trim().length() > 0) {
                builder.setPreCombineField(data.getPreCombineField());
            }

            if (null != data.getPartitionFields() && data.getPartitionFields().trim().length() > 0) {
                builder.setPartitionFields(data.getPartitionFields());
            }

            builder.initTable(hadoopConf, tablePath);
        }
    }

    public static void delete(AbstractData data) {
        String basePath = data.getBasePath();

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
    }

    public static void main(String[] args) throws Exception {
//        FundHistoryData data = new FundHistoryData();
//        create(data);
    }
}
