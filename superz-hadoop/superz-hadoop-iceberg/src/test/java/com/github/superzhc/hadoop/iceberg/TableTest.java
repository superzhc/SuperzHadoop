package com.github.superzhc.hadoop.iceberg;

import com.github.superzhc.common.date.LocalDateTimeUtils;
import com.github.superzhc.hadoop.iceberg.catalog.IcebergHadoopS3Catalog;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/22 10:53
 **/
public class TableTest {
    Catalog catalog;

    @Before
    public void setUp() throws Exception {
        catalog = new IcebergHadoopS3Catalog(
                "s3a://superz/flink/iceberg",
                "http://127.0.0.1:9000",
                "admin",
                "admin123456"
        ).catalog();
    }

    @Test
    public void testExpireSnapshotsAndClean() {
        TableIdentifier tableIdentifier = TableIdentifier.of("rsshub", "shopping");
        Table table = catalog.loadTable(tableIdentifier);

        table.expireSnapshots().expireOlderThan(System.currentTimeMillis()).cleanExpiredFiles(true).commit();
        table.refresh();
    }

    public void testTableProperties() {
        Map<String, String> properties = new HashMap<>();
        // 每次表提交后是否删除旧的元数据文件
        properties.put("write.metadata.delete-after-commit.enabled", "true");
        // 要保留旧的元数据文件数量
        properties.put("write.metadata.previous-versions-max", "3");
    }

    @Test
    public void deletePartitionField(){
        TableIdentifier tableIdentifier=TableIdentifier.of("rsshub","shopping");
        Table table= catalog.loadTable(tableIdentifier);
        table.updateSpec().removeField("rsshubKey").commit();
        table.refresh();
    }
}
