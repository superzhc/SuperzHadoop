package com.github.superzhc.finance;

import com.github.superzhc.hadoop.iceberg.catalog.IcebergHiveS3Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.hive.HiveCatalog;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/30 1:16
 */
public class FinanceDDL {
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

    @Test
    public void createDB() {
        Map<String, String> dbProperties = new HashMap<>();
        dbProperties.put("comment", "Finance Database");

        Namespace db = Namespace.of("finance");
        if (!catalog.namespaceExists(db)) {
            catalog.createNamespace(db, dbProperties);
        }
    }

    @Test
    public void dropDB() {
        Namespace db = Namespace.of("finance");
        if (catalog.namespaceExists(db)) {
            catalog.dropNamespace(db);
        }
    }
}
