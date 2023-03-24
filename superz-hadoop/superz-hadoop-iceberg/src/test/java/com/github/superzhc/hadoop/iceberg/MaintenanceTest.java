package com.github.superzhc.hadoop.iceberg;

import com.github.superzhc.hadoop.iceberg.catalog.IcebergHadoopS3Catalog;
import com.github.superzhc.hadoop.iceberg.catalog.IcebergHiveS3Catalog;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;
import org.junit.Before;

/**
 * @author superz
 * @create 2023/3/24 11:17
 **/
public class MaintenanceTest {
    private static final String WAREHOUSE = "s3a://superz/iceberg";
    private static final String S3_ENDPOINT = "http://127.0.0.1:9000";
    private static final String S3_USERNAME = "admin";
    private static final String S3_PASSWORD = "admin123456";
    Catalog catalog = null;

//    @Before
//    public void setUp(){
//        catalog=hadoopS3Catalog();
//    }

    private Catalog hadoopS3Catalog() {
        Catalog catalog = new IcebergHadoopS3Catalog(
                WAREHOUSE,
                S3_ENDPOINT,
                S3_USERNAME,
                S3_PASSWORD
        ).catalog();
        return catalog;
    }

    private Catalog hiveS3Catalog() {
        Catalog catalog = new IcebergHiveS3Catalog(
                "thrift://127.0.0.1:9083",
                WAREHOUSE,
                S3_ENDPOINT,
                S3_USERNAME,
                S3_PASSWORD
        ).catalog();
        return catalog;
    }

    public void expireSnapshots() {
        catalog = hadoopS3Catalog();
        Table table = catalog.loadTable(TableIdentifier.of("data", "index_info"));

        // 删除3天前的快照
        long tsToExpire = System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000);//3 days
        table.expireSnapshots().expireOlderThan(tsToExpire).commit();
        table.refresh();
    }

//    // Spark支持，合并压缩文件
//    public void compactDataFiles(){
//        catalog = hadoopS3Catalog();
//        Table table = catalog.loadTable(TableIdentifier.of("data", "index_info"));
//    }
}
