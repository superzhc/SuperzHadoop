package com.github.superzhc.hadoop.iceberg;

import com.github.superzhc.common.utils.ListUtils;
import com.github.superzhc.hadoop.iceberg.catalog.IcebergHadoopS3Catalog;
import com.github.superzhc.hadoop.iceberg.utils.SchemaUtils;
import com.github.superzhc.hadoop.iceberg.utils.TableUtils;
import org.apache.iceberg.*;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.data.IcebergGenerics;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.expressions.Expressions;
import org.apache.iceberg.types.Types;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

public class IcebergHadoopCatalogTest {
    private Catalog catalog;

    @Before
    public void setUp() throws Exception {
        catalog = new IcebergHadoopS3Catalog(
                "s3a://superz/java"
                , "http://127.0.0.1:9000"
                , "admin"
                , "admin123456")
                .catalog("hadoop");
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
        TableIdentifier tableIdentifier = TableIdentifier.of("catalog", "hadoop", "user4");
        Table table = catalog.createTable(tableIdentifier, schema, spec);
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

    @Test
    public void ddl0() {
        Table table = catalog.loadTable(TableIdentifier.of("demo", "t1"));

//        System.out.println(table.name());
        System.out.println(TableUtils.drop(catalog, table));

//        table.updateSchema()
//                .addColumn("col1", Types.StringType.get())
//                .deleteColumn("title")
//                .updateColumn("col1", Types.IntegerType.get())
//                .renameColumn("content", "col2")
//                .commit();
//
//        table.updateProperties()
//                .set("k1", "v1")
//                .commit();
//
//        table.updateLocation()
//                .setLocation("xxx")
//                .commit();
    }

    public void renameTable(){
        Table table=TableUtils.loadTable(catalog,"demo","t1");
        TableUtils.rename(catalog,table,"t2");
    }

    @Test
    public void renameColumn() {
        Table table = catalog.loadTable(TableIdentifier.of("akshare", "spark", "stock_zh_index_hist_csindex"));

        SchemaUtils.renameColumn(table, "volumn", "volume");
    }
}