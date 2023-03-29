package com.github.superzhc.finance.fund;

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
 * @create 2023/3/29 23:52
 */
public class FundDDL {
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
    public void fundBasic() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("code", "string");
        fields.put("name", "string");
        fields.put("full_name", "string");
        fields.put("type", "string");
        fields.put("index", "string");
        fields.put("indicator", "string");
        fields.put("description","string");
        Schema schema = SchemaUtils.create(fields, "code", "name");
        PartitionSpec partition = SchemaUtils.partition(schema);
        TableIdentifier identifier = TableIdentifier.of("finance", "fund_basic");
        if (catalog.tableExists(identifier)) {
            catalog.dropTable(identifier);
        }
        catalog.createTable(identifier, schema, partition, commonTBLProperties);
    }

    @Test
    public void updateFundBasic() {
        TableIdentifier identifier = TableIdentifier.of("finance", "fund_basic");
        Table table = catalog.loadTable(identifier);
    }

    @Test
    public void fundInfo() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("date", "date");
        fields.put("code", "string");
        fields.put("name", "string");
        fields.put("new", "double");
        fields.put("last_close", "double");
        fields.put("open", "double");
        fields.put("high", "double");
        fields.put("low", "double");
        fields.put("close", "double");
        fields.put("change_amount", "double");
        fields.put("change", "double");
        fields.put("volume", "double");
        fields.put("amount", "double");
        fields.put("turnover", "double");
        fields.put("circulation_market", "double");
        fields.put("total_market", "double");
        Schema schema = SchemaUtils.create(fields, "date", "code", "name");
        PartitionSpec partition = SchemaUtils.partition(schema, "date");
        TableIdentifier identifier = TableIdentifier.of("finance", "fund_info");
        if (catalog.tableExists(identifier)) {
            catalog.dropTable(identifier);
        }
        catalog.createTable(identifier, schema, partition, commonTBLProperties);
    }
}
