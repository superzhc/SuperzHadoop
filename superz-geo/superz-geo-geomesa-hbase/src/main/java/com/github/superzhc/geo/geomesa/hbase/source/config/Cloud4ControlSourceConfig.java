package com.github.superzhc.geo.geomesa.hbase.source.config;

import com.github.superzhc.geo.geomesa.GeomesaAdmin;
import com.github.superzhc.geo.geomesa.GeomesaQuery;
import com.github.superzhc.geo.geomesa.GeomesaUpsert;
import com.github.superzhc.geo.geomesa.QueryWrapper;
import com.github.superzhc.geo.geomesa.source.GeomesaDataStore;
import com.github.superzhc.geo.geomesa.source.config.GeomesaSourceConfig;
import org.locationtech.geomesa.hbase.data.HBaseDataStoreParams;
import org.locationtech.geomesa.utils.geotools.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
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
            GeomesaQuery geomesaQuery = new GeomesaQuery(geomesaDataStore/*, 1*/);

//            QueryWrapper queryWrapper = new QueryWrapper();
//            queryWrapper.during("timestamp", LocalDateTime.of(2021, 8, 4, 0, 0), LocalDateTime.of(2021, 8, 4, 23, 59))
//                    .eq("plate_number", "苏A19096");
//            List<Map<String, Object>> lst = geomesaQuery.query("bsm.gps", queryWrapper, 100, "timestamp", "desc");
//            System.out.println(lst);

            // 无条件查询
            System.out.println(geomesaQuery.scan("plc_info"));

            GeomesaAdmin geomesaAdmin = new GeomesaAdmin(geomesaDataStore);
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

            String sft=geomesaAdmin.formatSft("plc_info");
            System.out.println("SimepleFeatureType 信息："+sft);

//            StringBuilder attributes = new StringBuilder();
//            attributes.append("CraneName:String,");
//            attributes.append("ControlOn:String,");
//            attributes.append("WindSpeed:String,");
//            attributes.append("IsLock:String,");
//            attributes.append("HTPos:String,");
//            attributes.append("GTPos:String,");
//            attributes.append("TTPos:String,");
//            attributes.append("PTPos:String,");
//            attributes.append("HTState:String,");
//            attributes.append("TTState:String,");
//            attributes.append("GTState:String,");
//            attributes.append("PTState:String,");
//            attributes.append("HTFault_Fault1:String,");
//            attributes.append("HTFault_Fault2:String,");
//            attributes.append("GTFault_Fault1:String,");
//            attributes.append("GTFault_Fault2:String,");
//            attributes.append("TTFault_Fault1:String,");
//            attributes.append("TTFault_Fault2:String,");
//            attributes.append("PTFault_Fault1:String,");
//            attributes.append("PTFault_Fault2:String,");
//            attributes.append("ReadTime:Date");
//
//            SimpleFeatureType sft = SimpleFeatureTypes.createType("plc_test2", attributes.toString());
//            // 设置时空索引时间字段
//            sft.getUserData().put("geomesa.index.dtg", "ReadTime");
//            geomesaDataStore.getDataStore().createSchema(sft);
//
            // 2021年11月16日 创建表报错
//            geomesaAdmin.create("plc_test3", attributes.toString());
//            System.out.println("创建表成功");

//            GeomesaUpsert geomesaUpsert=new GeomesaUpsert(geomesaDataStore);
//            Map<String,Object> map=new HashMap<>();
//            map.put("ReadTime",new Date());
//            map.put("CraneName","Q10");
//            map.put("ControlOn","1");
//            map.put("WindSpeed","13");
//            map.put("IsLock","1");
//            map.put("HTPos","10.52");
//            map.put("GTPos","15");
//            map.put("TTPos","5.8");
//            map.put("PTPos","50");
//            map.put("HTState","1");
//            map.put("TTState","1");
//            map.put("GTState","1");
//            map.put("PTState","1");
//            map.put("HTFault_Fault1","0");
//            map.put("HTFault_Fault2","0");
//            map.put("GTFault_Fault1","0");
//            map.put("GTFault_Fault2","0");
//            map.put("TTFault_Fault1","0");
//            map.put("TTFault_Fault2","0");
//            map.put("PTFault_Fault1","0");
//            map.put("PTFault_Fault2","0");
//            geomesaUpsert.insert("plc_test",map);

//            // 删除表
//            geomesaAdmin.delete("plc_test2");
//            System.out.println(geomesaAdmin.exist("plc_test2"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
