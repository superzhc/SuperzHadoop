package com.github.superzhc.finance.maintenance;

import com.github.superzhc.hadoop.iceberg.catalog.IcebergHiveS3Catalog;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.hive.HiveCatalog;
import org.junit.Before;

/**
 * @author superz
 * @create 2023/4/1 15:07
 **/
public class FinanceMaintenance {
    HiveCatalog catalog;

    @Before
    public void setUp() {
        catalog = (HiveCatalog) new IcebergHiveS3Catalog(
                "thrift://127.0.0.1:9083",
                "s3a://superz/finance",
                "http://127.0.0.1:9000",
                "admin",
                "admin123456"
        ).catalog();
    }

    public void indexOneMinuteInfoOps() {
        TableIdentifier identifier = TableIdentifier.of("finance", "index_one_minute_info");
        Table table = catalog.loadTable(identifier);


    }
}
