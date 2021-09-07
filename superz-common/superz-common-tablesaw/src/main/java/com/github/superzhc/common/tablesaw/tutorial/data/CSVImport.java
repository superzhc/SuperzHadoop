package com.github.superzhc.common.tablesaw.tutorial.data;

import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvReadOptions;

import java.io.IOException;

/**
 * @author superz
 * @create 2021/9/7 15:28
 */
public class CSVImport {
    public static void main(String[] args) throws IOException {
        // 包含标题头
        Table t1 = Table.read().file("./superz-common/superz-common-tablesaw/data/tutorial.csv");
        System.out.println(t1.print());

        // 不包含标题头，不能直接使用file，会将第一行当作标题行
        CsvReadOptions.Builder builder = CsvReadOptions.builder("./superz-common/superz-common-tablesaw/data/tutorial2.csv")
                // .separator('\t')                                        // table is tab-delimited
                .header(false)                                            // no header
                ;
        Table t2 = Table.read().usingOptions(builder.build());
        System.out.println(t2.print());
    }
}
