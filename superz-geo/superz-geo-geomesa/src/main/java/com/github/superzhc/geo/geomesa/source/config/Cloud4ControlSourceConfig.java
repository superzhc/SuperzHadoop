package com.github.superzhc.geo.geomesa.source.config;

import org.locationtech.geomesa.hbase.data.HBaseDataStoreParams;

/**
 * @author superz
 * @create 2021/8/11 21:37
 */
public class Cloud4ControlSourceConfig extends GeomesaSourceConfig {
    @Override
    protected void init() {
        // 下面的写法无法连接远程的zookeeper，使用 hbase.zookeepers 参数项
        // parameters.put("hbase.zookeeper.quorum","namenode,datanode1,datanode2");
        // parameters.put("hbase.zookeeper.property.clientPort","2181");
        sourceParams.put("hbase.zookeepers", "namenode:2181,datanode1:2181,datanode1:2181");
        sourceParams.put("hbase.coprocessor.url", "hdfs://datanode1:8020/hbase/lib/geomesa-hbase-distributed-runtime-hbase2_2.11-3.0.0.jar|org.locationtech.geomesa.hbase.server.coprocessor.GeoMesaCoprocessor|1073741823||org.locationtech.geomesa.hbase.server.coprocessor.GeoMesaCoprocessor|1073741823|");
        // HBaseDataStoreParams.HBaseCatalogParam().key is the string "hbase.catalog"
        // the GeoMesa HBase data store will recognize the key and attempt to load itself
        sourceParams.put(HBaseDataStoreParams.HBaseCatalogParam().key, "cloud4control");
    }
}
