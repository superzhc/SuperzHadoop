package com.github.superzhc.hadoop.iceberg;

import com.github.superzhc.common.utils.PathUtils;
import com.github.superzhc.hadoop.iceberg.catalog.IcebergJdbcS3Catalog;
import com.github.superzhc.hadoop.iceberg.utils.SchemaUtils;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/14 10:36
 **/
public class IcebergJdbcCatalogTest {
    Catalog catalog;

    @Before
    public void setUp() throws Exception {
        catalog = new IcebergJdbcS3Catalog(
                "s3a://superz/java"
                , "http://127.0.0.1:9000"
                , "admin"
                , "admin123456"
                , String.format("jdbc:sqlite:%s/%s", PathUtils.project(), "/db/iceberg.db")
        ).catalog("jdbc");
    }

    @Test
    public void testCatalog() {
        Map<String, String> fields = new HashMap<>();
        fields.put("id", "uuid");
        fields.put("name", "string");
        fields.put("age", "int");
        fields.put("height", "double");
        fields.put("birthday", "date");
        fields.put("ts", "timestamp");

        Schema schema = SchemaUtils.create(fields);
        PartitionSpec spec = SchemaUtils.partition(schema, "years(birthday)");
        TableIdentifier tableIdentifier = TableIdentifier.of("catalog", "jdbc", "user");
        Table table = catalog.createTable(tableIdentifier, schema, spec);
    }
}
