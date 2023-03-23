package com.github.superzhc.hadoop.iceberg;

import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.hadoop.iceberg.catalog.IcebergHiveCatalog;
import com.github.superzhc.hadoop.iceberg.utils.SchemaUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.TableProperties;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.hive.HiveCatalog;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/22 17:01
 **/
public class IcebergHiveCatalogTest {
    static {
        // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
        System.setProperty("HADOOP_USER_NAME", "root");
    }

    private HiveCatalog getHiveCatalog() {
        /* 创建表的时候，客户端会需要连接hadoop，需要进行如下配置 */
        Configuration conf = new Configuration();
        conf.set("fs.hdfs.impl","org.apache.hadoop.hdfs.DistributedFileSystem");
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
//        conf.set("dfs.ha.automatic-failover.enabled","true");
//        conf.set("","");
//        conf.set("","");
//        conf.set("","");

        Catalog catalog = new IcebergHiveCatalog(
                "thrift://10.90.15.233:9083",
                "hdfs://xgitbigdata/usr/xgit/hive/warehouse",
                conf
        ).catalog();
        return (HiveCatalog) catalog;
    }

    @Test
    public void catalog() {
        HiveCatalog hiveCatalog = getHiveCatalog();
    }

    @Test
    public void databases() {
        HiveCatalog hiveCatalog = getHiveCatalog();
        // 获取所有的库
        List<Namespace> databases = hiveCatalog.listNamespaces();
        databases.stream().forEach(System.out::println);
    }

    @Test
    public void database() {
        HiveCatalog hiveCatalog = getHiveCatalog();

        Map<String, String> databaseMetadata = hiveCatalog.loadNamespaceMetadata(Namespace.of("influxdb_superz"));
        MapUtils.show(databaseMetadata);
    }

    @Test
    public void createDatabase() {
        HiveCatalog hiveCatalog = getHiveCatalog();

        Map<String, String> databaseProperties = new HashMap<>();
        databaseProperties.put("comment", "InfluxDB ETL Database");

        hiveCatalog.createNamespace(Namespace.of("influxdb_superz"), databaseProperties);
    }

    @Test
    public void tables() {
        HiveCatalog hiveCatalog = getHiveCatalog();

        List<TableIdentifier> tables = hiveCatalog.listTables(Namespace.of("influxdb_superz"));
        tables.stream().forEach(System.out::println);
    }

    @Test
    public void createTable() {
        HiveCatalog hiveCatalog = getHiveCatalog();

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
        TableIdentifier tableIdentifier = TableIdentifier.of("influxdb_superz", "t202303231638");

        hiveCatalog.createTable(tableIdentifier, schema, spec, tableProperties);
    }
}
