package com.github.superzhc.hadoop.hudi;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.IMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.ql.metadata.Hive;

import static com.github.superzhc.hadoop.hudi.data.AbstractData.DEFAULT_HIVE_METASTORE_URIS;

/**
 * @author superz
 * @create 2022/12/15 17:56
 **/
public class HudiHiveMetaMain {
    public static void main(String[] args) throws Exception {
        HiveConf conf = new HiveConf();
        conf.set("hive.metastore.uris", DEFAULT_HIVE_METASTORE_URIS);

        // 直接通过HMS操作Hive Metadata
        IMetaStoreClient client = Hive.get(conf).getMSC();
        Table table = client.getTable("default", "superz_java_client_20221213150742");
        System.out.println(table);
    }
}
