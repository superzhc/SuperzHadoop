package com.github.superzhc.hadoop.iceberg;

import com.github.superzhc.common.utils.ListUtils;
import com.github.superzhc.hadoop.iceberg.catalog.IcebergHadoopS3Catalog;
import com.github.superzhc.hadoop.iceberg.utils.SchemaUtils;
import org.apache.iceberg.*;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.data.GenericRecord;
import org.apache.iceberg.data.IcebergGenerics;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.data.parquet.GenericParquetWriter;
import org.apache.iceberg.expressions.Expression;
import org.apache.iceberg.expressions.Expressions;
import org.apache.iceberg.io.DataWriter;
import org.apache.iceberg.io.OutputFile;
import org.apache.iceberg.parquet.Parquet;
import org.apache.iceberg.types.Types;
import org.apache.parquet.hadoop.ParquetWriter;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

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
    public void createTable2() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("date", "date");
        fields.put("title", "string");
        fields.put("content", "string");

        Schema schema = SchemaUtils.create(fields);
        PartitionSpec partition = SchemaUtils.partition(schema, "months(date)");//PartitionSpec.builderFor(schema).month("date").build();
        TableIdentifier tableIdentifier = TableIdentifier.of("demo", "t1");
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

    @Test
    public void schema() {
        Table table = catalog.loadTable(TableIdentifier.of("demo", "t1"));
        Schema schema = table.schema();
        System.out.println(schema);
    }

    public void scan0() {
        Table table = catalog.loadTable(TableIdentifier.of("demo", "t1"));
        TableScan scan = table.newScan()
                .filter(Expressions.equal("date", LocalDate.now()))
//                .select("user_id", "user_name")
                // .asOfTime(timestampMillis:Long)    // 从指定时间戳开始读取数据
                // .useSnapshot(snapshotId:Long)         // 从指定snapshot id开始读取数据
                ;

        // 返回files
        List<FileScanTask> fileScanTasks = ListUtils.copyIterator(scan.planFiles().iterator());

        // 返回tasks
        List<CombinedScanTask> combinedScanTasks = ListUtils.copyIterator(scan.planTasks().iterator());

        // 返回读projection
        Schema schema = scan.schema();
    }

    public void read0() {
        Table table = catalog.loadTable(TableIdentifier.of("demo", "t1"));

        IcebergGenerics.ScanBuilder scanBuilder = IcebergGenerics.read(table);

        Iterator<Record> recordIterator = scanBuilder
                //.where()
//                .select()
                .build()
                .iterator();
    }

    public void write0() {
        Table table = catalog.loadTable(TableIdentifier.of("demo", "t1"));

        // 添加data files到表
        table.newAppend().commit();

        // 添加data files到表, 但不会compact metadata
        table.newFastAppend().commit();

        // 添加data files到表, 且删除被覆盖的files
        table.newOverwrite().commit();

        // 删除data files
        table.newDelete();

        // rewrite data files, 用new versions替换已经存在的files
        table.newRewrite();

        // 创建一个新的表级别事务
        table.newTransaction();

        // 为了更快的scan planning，用clustering files重写manifest
        table.rewriteManifests();

        // 对表snapshot进行管理，比如将表state回退到某个snapshot id
        table.manageSnapshots();
    }

//    @Test
//    public void write1() throws Exception {
//        Table table = catalog.loadTable(TableIdentifier.of("demo", "t1"));
//
//        String newPath = table.locationProvider().newDataLocation(
//                table.spec(),
//                null,
//                String.format("%s.parquet", UUID.randomUUID().toString()));
//
//        String path = String.format("%s/data/%s.parquet", table.location(), UUID.randomUUID().toString());
//        OutputFile file = table.io().newOutputFile(path);
//
//        GenericRecord record = GenericRecord.create(table.schema());
//
//        DataWriter<GenericRecord> dataWriter = null;
//        try {
//            dataWriter = Parquet
//                    .writeData(file)
//                    .forTable(table)
//                    //.schema(table.schema())
//                    //.withSpec(table.spec()/*PartitionSpec.unpartitioned()*/)
//                    .createWriterFunc(GenericParquetWriter::buildWriter)
//                    .overwrite()
//                    // .withPartition(record)
//                    .build();
//
//            Map<String, Object> data = new HashMap<>();
//            data.put("date", LocalDate.now());
//            data.put("title", "Iceberg Java API Write");
//            data.put("content", "superz use iceberg java api write data");
//            record = record.copy(data);
//
//            dataWriter.write(record);
//
//        } finally {
//            dataWriter.close();
//        }
//        DataFile dataFile = dataWriter.toDataFile();
//        table.newAppend().appendFile(dataFile).commit();
//    }

    public void ddl0() {
        Table table = catalog.loadTable(TableIdentifier.of("demo", "t1"));

        table.updateSchema()
                .addColumn("col1", Types.StringType.get())
                .deleteColumn("title")
                .updateColumn("col1", Types.IntegerType.get())
                .renameColumn("content", "col2")
                .commit();

        table.updateProperties()
                .set("k1", "v1")
                .commit();

        table.updateLocation()
                .setLocation("xxx")
                .commit();
    }
}