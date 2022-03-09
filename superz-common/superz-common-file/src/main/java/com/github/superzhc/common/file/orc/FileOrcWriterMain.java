package com.github.superzhc.common.file.orc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.*;
import org.apache.orc.OrcFile;
import org.apache.orc.TypeDescription;
import org.apache.orc.Writer;

import java.nio.charset.StandardCharsets;

public class FileOrcWriterMain {
    public static void main(String[] args) throws Exception {
        System.setProperty("hadoop.home.dir", "F:/soft/hadoop");
        Configuration conf = new Configuration();

        // 定义Schema
        String schemaString = "struct<" +
                // 整数类型，LongColumnVector
//                "smallint1:smallint," +
//                "tinyint1:tinyint," +
                "i:int," +
//                "bigint1:bigint," +
                // boolean，date也使用整数类型
                "b:boolean," +
                "date1:date," +
                // 时间戳类型，TimestampColumnVector
                "timestamp1:timestamp," +
                // 浮点数类型，DoubleColumnVector
                "d:double," +
//                "f:float," +
                // 十进制小数类型，DecimalColumnVector
//                "decimal1:decimal," +
                // 二进制类型，BytesColumnVector
//                "binary1:binary," +
//                "c:char," +
                "s:string" +
//                "varchar1:varchar," +
                // StructColumnVector
//                "struct1:struct<>," +
                // UnionColumnVector
//                "uniontype1:uniontype," +
                // 数组类型，ListColumnVector
//                "array1:array," +
                // Map数据，MapColumnVector
//                "map1:map<string,int>" +
                // MultiValuedColumnVector
                ">";
        TypeDescription schema = TypeDescription.fromString(schemaString);

        String path = "E:\\data\\demo.orc";
        /* 写文件 */
        Writer writer = OrcFile.createWriter(new Path(path), OrcFile.writerOptions(conf).setSchema(schema));

        VectorizedRowBatch batch = schema.createRowBatch();

        LongColumnVector i = (LongColumnVector) batch.cols[0];
        LongColumnVector b = (LongColumnVector) batch.cols[1];
        LongColumnVector date1 = (LongColumnVector) batch.cols[2];
        TimestampColumnVector timestamp1 = (TimestampColumnVector) batch.cols[3];
        DoubleColumnVector d = (DoubleColumnVector) batch.cols[4];
        BytesColumnVector s = (BytesColumnVector) batch.cols[5];

//        MapColumnVector map1 = (MapColumnVector) batch.cols[6];
//        BytesColumnVector mapKey = (BytesColumnVector) map1.keys;
//        LongColumnVector mapValue = (LongColumnVector) map1.values;

        for (int r = 0; r < 1000; r++) {
            int cursor = batch.size;
            i.vector[cursor] = r;
            b.vector[cursor] = r % 2;
            date1.vector[cursor] = System.currentTimeMillis();
            timestamp1.time[cursor] = System.currentTimeMillis();
            d.vector[cursor] = r * 1.0;
            s.vector[cursor] = String.valueOf(r).getBytes(StandardCharsets.UTF_8);
            batch.size++;

            if (cursor == batch.getMaxSize() - 1) {
                writer.addRowBatch(batch);
                batch.reset();
            }

        }

        if (batch.size != 0) {
            writer.addRowBatch(batch);
            batch.reset();
        }

        writer.close();
    }
}
