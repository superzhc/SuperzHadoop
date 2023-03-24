package com.github.superzhc.hadoop.iceberg.read;

import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.hadoop.iceberg.catalog.IcebergHadoopS3Catalog;
import com.github.superzhc.hadoop.iceberg.utils.TableReadUtils;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/24 14:03
 **/
public class JavaReadTest {
    private static final String WAREHOUSE = "s3a://superz/flink/iceberg";
    private static final String S3_ENDPOINT = "http://127.0.0.1:9000";
    private static final String S3_USERNAME = "admin";
    private static final String S3_PASSWORD = "admin123456";
    Catalog catalog = null;

    @Test
    public void testRead() {
        catalog = new IcebergHadoopS3Catalog(WAREHOUSE, S3_ENDPOINT, S3_USERNAME, S3_PASSWORD).catalog();
        Table table = catalog.loadTable(TableIdentifier.of("rsshub", "shopping"));

        List<Map<String, Object>> data = TableReadUtils.read(table, new String[]{"title", "pubDate"}, 30);
        MapUtils.show(data, 10);
    }
}
