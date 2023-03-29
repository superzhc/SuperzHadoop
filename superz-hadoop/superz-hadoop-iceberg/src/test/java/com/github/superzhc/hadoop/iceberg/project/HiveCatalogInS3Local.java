package com.github.superzhc.hadoop.iceberg.project;

import com.github.superzhc.hadoop.iceberg.catalog.IcebergHiveS3Catalog;
import org.apache.iceberg.hive.HiveCatalog;
import org.junit.Before;

/**
 * @author superz
 * @create 2023/3/24 17:12
 **/
public class HiveCatalogInS3Local {
    HiveCatalog catalog;

    @Before
    public void setUp() {
        catalog = (HiveCatalog) new IcebergHiveS3Catalog(
                "thrift://127.0.0.1:9083",
                "s3a://superz/akshare",
                "http://127.0.0.1:9000",
                "admin",
                "admin123456"
        ).catalog();
    }
}
