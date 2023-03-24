package com.github.superzhc.hadoop.iceberg;

import com.github.superzhc.hadoop.iceberg.catalog.IcebergHadoopCatalog;
import com.github.superzhc.hadoop.iceberg.utils.SchemaUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.TableProperties;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IcebergHadoopCatalogTest {
    static {
        // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
        System.setProperty("HADOOP_USER_NAME", "root");
    }

    private HadoopCatalog getHadoopCatalog() {
        Configuration conf = new Configuration();
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        conf.set("fs.defaultFS", "hdfs://xgitbigdata");
        conf.set("hadoop.proxyuser.root.hosts", "*");
        conf.set("hadoop.proxyuser.root.groups", "*");
        conf.set("dfs.nameservices", "xgitbigdata");
        conf.set("dfs.ha.namenodes.xgitbigdata", "nn1,nn2");
        conf.set("dfs.namenode.rpc-address.xgitbigdata.nn1", "10.90.15.142:8020");
        conf.set("dfs.namenode.rpc-address.xgitbigdata.nn2", "10.90.15.233:8020");
        conf.set("dfs.namenode.http-address.xgitbigdata.nn1", "10.90.15.142:50070");
        conf.set("dfs.namenode.http-address.xgitbigdata.nn2", "10.90.15.233:50070");
        //访问代理类：client，mycluster，active配置失败自动切换实现方式 采用默认
        conf.set("dfs.client.failover.proxy.provider.xgitbigdata", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

        Catalog catalog = new IcebergHadoopCatalog(
                "hdfs://xgitbigdata/usr/xgit/hive/warehouse",
                conf)
                .catalog();
        return (HadoopCatalog) catalog;
    }

    @Test
    public void databases() {
        List<Namespace> databases = getHadoopCatalog().listNamespaces();
        databases.stream().forEach(System.out::println);
    }

    @Test
    public void tables() {
        List<TableIdentifier> tables = getHadoopCatalog().listTables(Namespace.of("influxdb_superz.db"));
        tables.stream().forEach(System.out::println);
    }

    @Test
    public void createTable() {
        HadoopCatalog hadoopCatalog = getHadoopCatalog();

        Map<String, String> tableProperties = new HashMap<>();
        tableProperties.put(TableProperties.FORMAT_VERSION, "2");
        tableProperties.put(TableProperties.UPSERT_ENABLED, "true");
        tableProperties.put(TableProperties.METADATA_DELETE_AFTER_COMMIT_ENABLED, "true");
        tableProperties.put(TableProperties.METADATA_PREVIOUS_VERSIONS_MAX, "3");

        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("id", "int");
        fields.put("name", "string");
        Schema schema = SchemaUtils.create(fields, "id");
        PartitionSpec spec = SchemaUtils.partition(schema);
        TableIdentifier tableIdentifier = TableIdentifier.of("influxdb_superz.db", "hadoop_t202303240932");
        hadoopCatalog.createTable(tableIdentifier, schema, spec, tableProperties);
    }
}