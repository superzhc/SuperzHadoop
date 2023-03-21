package com.github.superzhc.hadoop.iceberg.write;

import com.github.superzhc.hadoop.iceberg.utils.SchemaUtils;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.fs.Path;
import org.apache.iceberg.Schema;
import org.apache.iceberg.avro.AvroSchemaUtil;
import org.apache.iceberg.data.Record;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/21 15:58
 **/
public class JavaWriteTest {
    public void testParquetAvro() throws Exception {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("id", "int");
        fields.put("col1", "string");
        Schema schema = SchemaUtils.create(fields);
        org.apache.avro.Schema avroSchema = AvroSchemaUtil.convert(schema.asStruct());

        ParquetWriter<GenericRecord> writer = AvroParquetWriter.<GenericRecord>builder(new Path("xxx"))
                .withDataModel(GenericData.get())
                .withSchema(avroSchema)
                .config("parquet.avro.add-list-element-records", "true")
                .config("parquet.avro.write-old-list-structure", "true")
                .build();
    }

    public void testRecord() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("id", "int");
        fields.put("data", "string");
        fields.put("f", "float");
        fields.put("d", "double");
        fields.put("de", "decimal(18,2)");
        fields.put("fi", "fixed(4)");
        //...
        Schema schema = SchemaUtils.create(fields);

        Record record = org.apache.iceberg.data.GenericRecord.create(schema);
        record.setField("id", 1);
        record.setField("data", "content xxx");
        record.setField("f", 1.2f);
        //...

    }
}
