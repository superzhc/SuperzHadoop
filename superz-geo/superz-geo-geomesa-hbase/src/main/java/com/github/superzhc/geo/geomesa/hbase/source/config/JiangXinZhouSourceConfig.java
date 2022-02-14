package com.github.superzhc.geo.geomesa.hbase.source.config;

import com.github.superzhc.geo.geomesa.GeomesaAdmin;
import com.github.superzhc.geo.geomesa.GeomesaQuery;
import com.github.superzhc.geo.geomesa.source.GeomesaDataStore;
import com.github.superzhc.geo.geomesa.source.config.GeomesaSourceConfig;
import org.locationtech.geomesa.hbase.data.HBaseDataStoreParams;

import java.io.IOException;

/**
 * @author superz
 * @create 2022/1/18 16:08
 */
public class JiangXinZhouSourceConfig extends GeomesaSourceConfig {
    @Override
    protected void init() {
        sourceParams.put("hbase.zookeepers", "cdh-node1:2181,cdh-node2:2181,cdh-node3:2181");
        sourceParams.put("hbase.coprocessor.url", "hdfs://cdh-node1:8020/hbase/lib/geomesa-hbase-distributed-runtime-hbase2_2.11-3.0.0.jar|org.locationtech.geomesa.hbase.server.coprocessor.GeoMesaCoprocessor|1073741823||org.locationtech.geomesa.hbase.server.coprocessor.GeoMesaCoprocessor|1073741823|");
        sourceParams.put(HBaseDataStoreParams.HBaseCatalogParam().key, "cloud4control");
    }

    public static void main(String[] args) {
        try (GeomesaDataStore geomesaDataStore = new GeomesaDataStore(new JiangXinZhouSourceConfig())) {
            String schema = "quay.crane.plc.test";

//            GeomesaAdmin geomesaAdmin = new GeomesaAdmin(geomesaDataStore);
//            System.out.println(geomesaAdmin.show());

            GeomesaQuery geomesaQuery = new GeomesaQuery(geomesaDataStore);
            System.out.println(geomesaQuery.query(schema));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
