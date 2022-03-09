package com.github.superzhc.common.file.parquet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.example.GroupWriteSupport;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.OriginalType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Types;

public class FileParquetWriterMain {
    public static void main(String[] args) throws Exception {
        System.setProperty("hadoop.home.dir", "F:/soft/hadoop");
        String path = "E:\\data\\demo.parquet";

        // 声明 Parquet 的 messagetype
        MessageType messageType = Types.buildMessage()
                // 字段 id
                .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("id")
                // 字段 name
                .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("name")
                // 字段 age
                .required(PrimitiveType.PrimitiveTypeName.INT32).named("age")
                // trigger
                .requiredGroup()
                .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("t1")
                .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("t2")
                .named("group1")
                .named("trigger");

        // 声明 Writer
        Configuration conf = new Configuration();
        GroupWriteSupport.setSchema(messageType, conf);
        GroupWriteSupport writeSupport = new GroupWriteSupport();

        // 写数据
        try (ParquetWriter<Group> writer =
                     new ParquetWriter<>(
                             new Path(path),
                             ParquetFileWriter.Mode.CREATE,
                             writeSupport,
                             CompressionCodecName.UNCOMPRESSED,
                             128 * 1024 * 1024,
                             5 * 1024 * 1024,
                             5 * 1024 * 1024,
                             ParquetWriter.DEFAULT_IS_DICTIONARY_ENABLED,
                             ParquetWriter.DEFAULT_IS_VALIDATING_ENABLED,
                             ParquetWriter.DEFAULT_WRITER_VERSION,
                             conf)
        ) {
            for (int i = 0; i < 1000; i++) {
                Group group = new SimpleGroupFactory(messageType).newGroup();
                group.append("name", i + "@name")
                        .append("id", i + "@id")
                        .append("age", i)
                        .addGroup("group1")
                        .append("t1", i + "@t1")
                        .append("t2", i + "@t2");
                writer.write(group);
            }
        }
    }
}
