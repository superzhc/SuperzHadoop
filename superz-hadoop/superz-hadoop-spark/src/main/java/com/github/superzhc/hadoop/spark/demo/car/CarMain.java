package com.github.superzhc.hadoop.spark.demo.car;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * @author superz
 * @create 2021/11/13 15:24
 */
public class CarMain {
    private static final String DOWNLOAD_BASE = "D:\\downloads\\";
    private static final String DOWNLOAD_BASE_BAIDU = DOWNLOAD_BASE + "baidu\\";

    public static void main(String[] args) {
        SparkConf conf = new SparkConf();

        SparkSession spark = SparkSession.builder().appName("SparkSessionDemo").master("local[*]").config(conf)
                .getOrCreate();

        Dataset<Row> df=spark.read()
                .format("com.crealytics.spark.excel")
                // .option("dataAddress", "'My Sheet'!B3:C35") // Optional, default: "A1"
                .option("header", "false") // Required
                .option("treatEmptyValuesAsNulls", "false") // Optional, default: true
                .option("setErrorCellsToFallbackValues", "true") // Optional, default: false, where errors will be converted to null. If true, any ERROR cell values (e.g. #N/A) will be converted to the zero values of the column's data type.
                .option("usePlainNumberFormat", "false") // Optional, default: false, If true, format the cells without rounding and scientific notations
                .option("inferSchema", "false") // Optional, default: false
                .option("addColorColumns", "true") // Optional, default: false
                .option("timestampFormat", "MM-dd-yyyy HH:mm:ss") // Optional, default: yyyy-mm-dd hh:mm:ss[.fffffffff]
                .option("maxRowsInMemory", 20) // Optional, default None. If set, uses a streaming reader which can help with big files (will fail if used with xls format files)
                .option("excerptSize", 10) // Optional, default: 10. If set and if schema inferred, number of rows to infer schema from
                .option("workbookPassword", "pass") // Optional, default None. Requires unlimited strength JCE for older JVMs
                //.schema(myCustomSchema) // Optional, default: Either inferred schema, or all columns are Strings
                .load(DOWNLOAD_BASE_BAIDU+"car\\数据包二\\江苏\\13及以前\\2010年江苏南通车主 1000.xls")
                ;

        df.show();
    }
}
