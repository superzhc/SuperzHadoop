package com.github.superzhc.hadoop.iceberg;

import com.github.superzhc.common.date.LocalDateTimeUtils;
import com.github.superzhc.hadoop.iceberg.catalog.IcebergHadoopS3Catalog;
import org.apache.iceberg.Snapshot;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * @author superz
 * @create 2023/3/22 9:56
 **/
public class SnapshotTest {
    Catalog catalog;

    @Before
    public void setUp() throws Exception {
        catalog = new IcebergHadoopS3Catalog(
                "s3a://superz/flink/iceberg"
                , "http://127.0.0.1:9000"
                , "admin"
                , "admin123456")
                .catalog();
    }

    @Test
    public void testCurrentSnapshot() {
        TableIdentifier identifier = TableIdentifier.of("rsshub", "shopping");
        Table table = catalog.loadTable(identifier);

        //获取表的当前快照
        Snapshot currentSnapshot = table.currentSnapshot();
        long currentSnapshotTimestamp = currentSnapshot.timestampMillis();
        LocalDateTime currentSnapshotDT = LocalDateTimeUtils.convert4timestamp(currentSnapshotTimestamp);
        System.out.println(currentSnapshotDT);
    }

    @Test
    public void testSnapshots() {
        TableIdentifier identifier = TableIdentifier.of("rsshub", "shopping");
        Table table = catalog.loadTable(identifier);

        Iterable<Snapshot> snapshotIterable = table.snapshots();
        for (Snapshot snapshot : snapshotIterable) {
            long timestamp = snapshot.timestampMillis();
            LocalDateTime dateTime = LocalDateTimeUtils.convert4timestamp(timestamp);
            System.out.println(dateTime);
        }
    }
}
