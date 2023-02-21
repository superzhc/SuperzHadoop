package com.github.superzhc.common.file.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.FileReader;
import java.io.Reader;

/**
 * @author superz
 * @create 2023/2/21 17:36
 **/
public class CSVReaderTest {

    @Test
    public void demo1() {
        String path = "E:\\data\\news_dw_any_knew_hot_news.csv";

        try (Reader in = new FileReader(path)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            for (CSVRecord record : records) {
                int len = record.size();
                for (int i = 0; i < len; i++) {
                    System.out.print(record.get(i) + "\t");
                }
                System.out.println();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
