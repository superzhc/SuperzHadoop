package com.github.superzhc.common.file.orc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.ColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.OrcFile;
import org.apache.orc.Reader;
import org.apache.orc.RecordReader;

public class FileOrcReaderMain {
    public static void main(String[] args) throws Exception {
        System.setProperty("hadoop.home.dir", "F:/soft/hadoop");
        Configuration conf = new Configuration();

        String path = "E:\\data\\demo.orc";
        Reader reader = OrcFile.createReader(new Path(path), OrcFile.readerOptions(conf));

        // 读取文件
        RecordReader rows = reader.rows();
        VectorizedRowBatch batch = reader.getSchema().createRowBatch();

        while (rows.nextBatch(batch)) {
            for (int r = 0; r < batch.size; ++r) {
                StringBuilder sb=new StringBuilder();
                for (ColumnVector col : batch.cols) {
//                    System.out.print(col.type);
//                    System.out.print();
//                    System.out.print(",");
                    col.stringifyValue(sb,r);
                    sb.append(",");
                }
                System.out.println(sb);
            }
        }
        rows.close();
    }
}
