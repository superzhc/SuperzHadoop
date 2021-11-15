package com.github.superzhc.geo.geomesa.hbase.source.config;

import com.github.superzhc.geo.geomesa.GeomesaAdmin;
import com.github.superzhc.geo.geomesa.GeomesaQuery;
import com.github.superzhc.geo.geomesa.QueryWrapper;
import com.github.superzhc.geo.geomesa.source.GeomesaDataStore;
import com.github.superzhc.geo.geomesa.source.config.GeomesaSourceConfig;
import org.locationtech.geomesa.hbase.data.HBaseDataStoreParams;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
        /*sourceParams.put("hbase.security.enabled", "false");*/
    }

    public static void main(String[] args) {
        try (GeomesaDataStore geomesaDataStore = new GeomesaDataStore(new Cloud4ControlSourceConfig())) {
//            GeomesaQuery geomesaQuery = new GeomesaQuery(geomesaDataStore, 1);
//            QueryWrapper queryWrapper = new QueryWrapper();
//            queryWrapper.during("timestamp", LocalDateTime.of(2021, 8, 4, 0, 0), LocalDateTime.of(2021, 8, 4, 23, 59))
//                    .eq("plate_number", "苏A19096");
//            List<Map<String, Object>> lst = geomesaQuery.query("bsm.gps", queryWrapper, 100, "timestamp", "desc");
//            System.out.println(lst);

            GeomesaAdmin geomesaAdmin=new GeomesaAdmin(geomesaDataStore);
//            String[] schemas=geomesaAdmin.list();
//
//            if(null==schemas || schemas.length==0){
//                System.out.println("无 Schema");
//                return;
//            }
//
//            for(String schema:schemas){
//                System.out.println(schema);
//            }

            String sft=geomesaAdmin.formatSft("bsm.gps");
            System.out.println(sft);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
