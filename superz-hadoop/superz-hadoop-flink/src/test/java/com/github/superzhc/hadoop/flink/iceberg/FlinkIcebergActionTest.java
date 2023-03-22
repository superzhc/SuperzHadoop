package com.github.superzhc.hadoop.flink.iceberg;

import com.github.superzhc.common.utils.SystemUtils;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.CatalogProperties;
import org.apache.iceberg.Table;
import org.apache.iceberg.aws.AwsProperties;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.flink.actions.Actions;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/21 14:47
 **/
public class FlinkIcebergActionTest {
    StreamExecutionEnvironment env;
    Catalog catalog;

    static {
        System.setProperty("aws.region", "us-east-1");
        // 设置环境变量：HADOOP_CONF_DIR=./target/classes
        SystemUtils.setEnv("HADOOP_CONF_DIR", "./target/classes");
    }

    @Before
    public void setUp() throws Exception {
        env = StreamExecutionEnvironment.getExecutionEnvironment();

        Map<String, String> properties = new HashMap<>();
        properties.put(CatalogProperties.CATALOG_IMPL, HadoopCatalog.class.getName()/*"org.apache.iceberg.hadoop.HadoopCatalog"*/);
        properties.put(CatalogProperties.WAREHOUSE_LOCATION, "s3a://superz/flink/iceberg");
        properties.put(CatalogProperties.FILE_IO_IMPL, "org.apache.iceberg.aws.s3.S3FileIO");
        properties.put(AwsProperties.S3FILEIO_ENDPOINT, "http://127.0.0.1:9000");
        properties.put(AwsProperties.S3FILEIO_ACCESS_KEY_ID, "admin");
        properties.put(AwsProperties.S3FILEIO_SECRET_ACCESS_KEY, "admin123456");

        Configuration conf = new Configuration();
        conf.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
        conf.set("fs.s3a.endpoint", "http://127.0.0.1:9000");
        conf.set("fs.s3a.access.key", "admin");
        conf.set("fs.s3a.secret.key", "admin123456");
        conf.set("fs.s3a.connection.ssl.enabled", "false");
        conf.set("fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");

        HadoopCatalog hadoopCatalog = new HadoopCatalog();
        hadoopCatalog.setConf(conf);
        hadoopCatalog.initialize("hadoop", properties);
        catalog = hadoopCatalog;
    }

    @Test
    public void test() {
        TableIdentifier tableIdentifier = TableIdentifier.of("rsshub", "shopping");
        Table table = catalog.loadTable(tableIdentifier);
        Actions.forTable(env, table).rewriteDataFiles().execute();
        table.refresh();
    }
}
