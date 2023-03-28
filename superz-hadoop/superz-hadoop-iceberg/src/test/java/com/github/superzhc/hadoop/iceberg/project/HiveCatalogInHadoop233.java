package com.github.superzhc.hadoop.iceberg.project;

import com.github.superzhc.common.utils.ListUtils;
import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.hadoop.iceberg.catalog.IcebergHiveCatalog;
import com.github.superzhc.hadoop.iceberg.utils.SchemaUtils;
import com.github.superzhc.hadoop.iceberg.utils.TableReadUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.TableProperties;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.hive.HiveCatalog;
import org.apache.iceberg.types.Types;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/24 17:04
 **/
public class HiveCatalogInHadoop233 {
    static {
        // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
        System.setProperty("HADOOP_USER_NAME", "root");
    }

    private HiveCatalog catalog;

    @Before
    public void setUp() {
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
        conf.set("dfs.client.failover.proxy.provider.xgitbigdata", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

        catalog = (HiveCatalog) new IcebergHiveCatalog(
                "thrift://10.90.15.221:9083,thrift://10.90.15.233:9083",
                "hdfs://xgitbigdata/usr/xgit/hive/warehouse",
                conf
        ).catalog();
    }

    @Test
    public void databases() {
        List<Namespace> databases = catalog.listNamespaces();
        databases.stream().forEach(System.out::println);
    }

    @Test
    public void databaseInfo() {
        Map<String, String> info = catalog.loadNamespaceMetadata(Namespace.of("influxdb_superz"));
        MapUtils.show(info);
    }

    @Test
    public void tables() {
        List<TableIdentifier> tables = catalog.listTables(Namespace.of("influxdb_superz"));
        tables.stream().forEach(System.out::println);
    }

    @Test
    public void fields(){
        TableIdentifier tableIdentifier=TableIdentifier.of("xgit_admin","superz_influxdb_device_daily_data");
        Table table=catalog.loadTable(tableIdentifier);
        Schema schema= table.schema();
        for (Types.NestedField field:schema.columns()){
            System.out.println(field);
        }
    }

    @Test
    public void createTableDeviceDailyData() {
        Map<String, String> tableProperties = new HashMap<>();
        tableProperties.put(TableProperties.FORMAT_VERSION, "2");
        tableProperties.put(TableProperties.UPSERT_ENABLED, "true");
        tableProperties.put(TableProperties.METADATA_DELETE_AFTER_COMMIT_ENABLED, "true");
        tableProperties.put(TableProperties.METADATA_PREVIOUS_VERSIONS_MAX, "3");

        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("time", "long");
        fields.put("tc", "string");
        fields.put("fc", "double");
        fields.put("fv", "double");
        Schema schema = SchemaUtils.create(fields, "time", "tc");
        PartitionSpec spec = SchemaUtils.partition(schema, "tc");
        TableIdentifier tableIdentifier = TableIdentifier.of("influxdb_superz", "device_daily_data");
        if (catalog.tableExists(tableIdentifier)) {
            catalog.dropTable(tableIdentifier);
        }
        catalog.createTable(tableIdentifier, schema, spec, tableProperties);
    }

    @Test
    public void tableDeviceDailyData() {
        Table table = catalog.loadTable(TableIdentifier.of("influxdb_superz", "device_daily_data"));

        MapUtils.show(table.properties());
    }

    @Test
    public void readDeviceDailyData() {
        Table table = catalog.loadTable(TableIdentifier.of("influxdb_superz", "device_daily_data"));
        List<Map<String, Object>> data = TableReadUtils.read(table);
        MapUtils.show(data, 1000);
    }
}
