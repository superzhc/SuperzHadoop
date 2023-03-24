package com.github.superzhc.hadoop.iceberg.project;

import com.github.superzhc.hadoop.iceberg.catalog.IcebergHadoopCatalog;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.junit.Before;

/**
 * @author superz
 * @create 2023/3/24 17:10
 **/
public class HadoopCatalogInHadoop233 {
    static {
        // 如果在windows本地跑，需要从widnows访问HDFS，需要指定一个合法的身份
        System.setProperty("HADOOP_USER_NAME", "root");
    }

    HadoopCatalog catalog;

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

        catalog = (HadoopCatalog) new IcebergHadoopCatalog(
                "hdfs://xgitbigdata/usr/xgit/hive/warehouse",
                conf)
                .catalog();
    }
}
