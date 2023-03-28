package com.github.superzhc.hadoop.hive.metastore;

import com.github.superzhc.common.utils.ListUtils;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.IMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.Database;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.ql.metadata.Hive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author superz
 * @create 2023/3/28 10:11
 **/
public class HiveMetaStoreClientTest {
    IMetaStoreClient client0 = null;

//    HiveMetaStoreClient client;

    @Before
    public void setUp() throws Exception {
        HiveConf hiveConf = new HiveConf();

        //方式一：直接将服务器的配置文件down下来，加载配置文件即可
        // hiveConf.addResource("hive-site.xml");

        //方式二：代码配置配置项
        hiveConf.set("hive.metastore.uris", "thrift://10.90.15.221:9083,thrift://10.90.15.233:9083");
        hiveConf.set("hive.metastore.warehouse.dir", "hdfs://xgitbigdata/usr/xgit/hive/warehouse");

        Hive hive = Hive.get(hiveConf);
        client0 = hive.getMSC();

//        client = new HiveMetaStoreClient(hiveConf);
    }

    @After
    public void tearDown() throws Exception {
        if (null != client0) {
            client0.close();
        }
    }

    @Test
    public void catalogs() throws Exception {
        List<String> catalogs = client0.getCatalogs();
        System.out.println(ListUtils.print("CATALOG", catalogs));
    }

    @Test
    public void databases() throws Exception {
        List<String> databases = client0.getAllDatabases();
        System.out.println(ListUtils.print("DATABASE", databases));
    }

    @Test
    public void database() throws Exception {
        Database db = client0.getDatabase("influxdb_superz");
        System.out.println(db.toString());
    }

    @Test
    public void tables() throws Exception {
        List<String> tables = client0.getAllTables("influxdb_superz");
        System.out.println(ListUtils.print("TABLE", tables));
    }

    @Test
    public void table() throws Exception {
        Table table = client0.getTable("influxdb_superz", "t20230328");
        System.out.println(table.toString());
    }
}
