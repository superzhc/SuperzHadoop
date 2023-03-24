package com.github.superzhc.hadoop.iceberg.project;

import com.github.superzhc.hadoop.iceberg.catalog.IcebergHadoopS3Catalog;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.junit.Before;

/**
 * @author superz
 * @create 2023/3/24 17:08
 **/
public class HadoopCatalogInS3Local {
    HadoopCatalog catalog;

    @Before
    public void setUp(){
        catalog =(HadoopCatalog) new IcebergHadoopS3Catalog(
                "s3a://superz/java"
                , "http://127.0.0.1:9000"
                , "admin"
                , "admin123456")
                .catalog("hadoop");
    }
}
