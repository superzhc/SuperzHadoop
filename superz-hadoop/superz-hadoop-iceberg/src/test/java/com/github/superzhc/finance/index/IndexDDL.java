package com.github.superzhc.finance.index;

import com.github.superzhc.hadoop.iceberg.catalog.IcebergHiveS3Catalog;
import com.github.superzhc.hadoop.iceberg.utils.SchemaUtils;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.TableProperties;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.hive.HiveCatalog;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/29 20:19
 */
public class IndexDDL {
    static Map<String, String> commonTBLProperties;

    static {
        commonTBLProperties = new HashMap<>();
        commonTBLProperties.put(TableProperties.FORMAT_VERSION, "2");
        commonTBLProperties.put(TableProperties.UPSERT_ENABLED, "true");
        commonTBLProperties.put(TableProperties.METADATA_DELETE_AFTER_COMMIT_ENABLED, "true");
        commonTBLProperties.put(TableProperties.METADATA_PREVIOUS_VERSIONS_MAX, "3");
    }

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
    public void indexBasic() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("code", "string");
        fields.put("name", "string");
        fields.put("full_name", "string");
        fields.put("sample_number", "int");
        fields.put("publish_date", "date");
        fields.put("description", "string");
        Schema schema = SchemaUtils.create(fields, "code", "name");
        PartitionSpec partition = SchemaUtils.partition(schema);
        TableIdentifier tableIdentifier = TableIdentifier.of("finance", "index_basic");
        if (catalog.tableExists(tableIdentifier)) {
            catalog.dropTable(tableIdentifier);
        }
        catalog.createTable(tableIdentifier, schema, partition, commonTBLProperties);
    }

    @Test
    public void updateIndexBasic(){
        TableIdentifier identifier = TableIdentifier.of("finance", "index_basic");
        Table table= catalog.loadTable(identifier);

    }

    @Test
    public void indexInfo() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("date", "date");
        fields.put("code", "string");
        fields.put("name", "string");
        fields.put("open", "double");
        fields.put("high", "double");
        fields.put("low", "double");
        fields.put("close", "double");
        fields.put("change", "double");
        fields.put("pe_ttm", "double");
        fields.put("volume", "double");
        fields.put("amount", "double");
        fields.put("total_market", "double");
        fields.put("circulation_market", "double");
        Schema schema = SchemaUtils.create(fields, "code", "name");
        PartitionSpec partition = SchemaUtils.partition(schema, "date");
        TableIdentifier identifier = TableIdentifier.of("finance", "index_info");
        if (catalog.tableExists(identifier)) {
            catalog.dropTable(identifier);
        }
        catalog.createTable(identifier, schema, partition, commonTBLProperties);
    }

}
