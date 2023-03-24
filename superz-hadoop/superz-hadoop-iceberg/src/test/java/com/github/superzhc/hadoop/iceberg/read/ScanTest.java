package com.github.superzhc.hadoop.iceberg.read;

import com.github.superzhc.hadoop.iceberg.catalog.IcebergHadoopS3Catalog;
import com.github.superzhc.hadoop.iceberg.catalog.IcebergHiveS3Catalog;
import org.apache.iceberg.CombinedScanTask;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.TableScan;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;

/**
 * @author superz
 * @create 2023/3/24 13:57
 **/
public class ScanTest {
    private static final String WAREHOUSE = "s3a://superz/flink/iceberg";
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

    public void testScan(){
        catalog=hadoopS3Catalog();
        Table table=catalog.loadTable(TableIdentifier.of("rsshub","shopping"));
        TableScan scan=table.newScan();

        // 返回相关文件
        Schema scheam=scan.schema();
        Iterable<CombinedScanTask> tasks=scan.planTasks();
    }
}
