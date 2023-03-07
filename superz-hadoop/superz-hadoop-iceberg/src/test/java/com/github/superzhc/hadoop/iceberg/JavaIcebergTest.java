package com.github.superzhc.hadoop.iceberg;

import com.github.superzhc.hadoop.iceberg.catalog.IcebergHadoopS3Catalog;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.types.Types;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class JavaIcebergTest {
    private Catalog catalog;

    @Before
    public void setUp() throws Exception {
        catalog = new IcebergHadoopS3Catalog(
                "s3a://superz"
                , "http://127.0.0.1:9000"
                , "admin"
                , "admin123456")
                .catalog("hadoop");
    }

    @Test
    public void createTable0() {
        // 定义Schema
        Schema schema = new Schema(
                Types.NestedField.required(1, "level", Types.StringType.get()),
                Types.NestedField.required(2, "event_time", Types.TimestampType.withZone()),
                Types.NestedField.required(3, "message", Types.StringType.get()),
                Types.NestedField.optional(4, "call_stack", Types.ListType.ofRequired(5, Types.StringType.get()))
        );

        // 定义分区
        PartitionSpec spec = PartitionSpec.builderFor(schema)
                .hour("event_time")
                .build();

        // 创建表标识
        TableIdentifier tableIdentifier = TableIdentifier.of("demo", "logs");

        // 创建表
        catalog.createTable(tableIdentifier, schema, spec);
    }

    @Test
    public void createTable1() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("date", "date");
        fields.put("title", "string");
        fields.put("content", "string");

        Schema schema = SchemaUtils.create(fields);
        PartitionSpec partition = PartitionSpec.builderFor(schema).month("date").build();
        TableIdentifier tableIdentifier = TableIdentifier.of("akshare", "news_cctv");
        catalog.createTable(tableIdentifier, schema, partition);
    }

    @Test
    public void listTables() {
        List<TableIdentifier> tables = catalog.listTables(Namespace.of("akshare"));
        System.out.println(tables);
    }

    @Test
    public void dropTable() {
        TableIdentifier tableIdentifier = TableIdentifier.of("demo", "logs");
        catalog.dropTable(tableIdentifier);
    }

}