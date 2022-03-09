package com.github.superzhc.common.file.parquet;

import org.apache.hadoop.fs.Path;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.example.GroupReadSupport;

public class FileParquetReaderMain {
    public static void main(String[] args) throws Exception {
        System.setProperty("hadoop.home.dir", "F:/soft/hadoop");
        String path = "E:\\data\\demo.parquet";

        GroupReadSupport readSupport = new GroupReadSupport();
        try (ParquetReader<Group> reader = ParquetReader.<Group>builder(readSupport, new Path(path)).build()) {
            Group group = null;
            while ((group = reader.read()) != null) {
                System.out.println(group);
            }
        }
    }
}
