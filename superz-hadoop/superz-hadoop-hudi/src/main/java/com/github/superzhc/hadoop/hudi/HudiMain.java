package com.github.superzhc.hadoop.hudi;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hudi.common.fs.FSUtils;
import org.apache.hudi.common.model.HoodieTableType;
import org.apache.hudi.common.table.HoodieTableMetaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author superz
 * @create 2022/12/7 15:18
 **/
public class HudiMain {
    private static final Logger log = LoggerFactory.getLogger(HudiMain.class);

    public static void main(String[] args) throws Exception {
        String tableName = "superz_java_client";
        String tablePath = "hdfs://log-platform01:8020/user/superz/hudi/superz_java_client_20221207";
//        tablePath="hdfs://log-platform01:8020/user/superz/hudi/superz_sina_s";

        Configuration hadoopConf = new Configuration();
        hadoopConf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        Path path = new Path(tablePath);
        FileSystem fs = FSUtils.getFs(tablePath, hadoopConf);
        if (!fs.exists(path)) {
            log.info("路径[{}]不存在", tablePath);
            HoodieTableMetaClient.withPropertyBuilder()
                    .setTableType(HoodieTableType.COPY_ON_WRITE)
                    .setTableName(tableName)
                    .initTable(hadoopConf, tablePath);
        }
    }
}
