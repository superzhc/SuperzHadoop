package com.github.superzhc.gis.geomesa.direct;

import com.github.superzhc.gis.geomesa.tool.GeoMesa2MD;
import org.geotools.data.*;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.util.factory.Hints;
import org.locationtech.geomesa.hbase.data.HBaseDataStoreParams;
import org.locationtech.geomesa.utils.geotools.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * SimpleFeatureType: 定义给定Schema中属性的名称和类型，它类似关系型数据库的表定义
 * GeoMesa 支持所有标准 GeoTools 属性类型，以及一些附加属性类型，详细见<a>https://www.geomesa.org/documentation/stable/user/datastores/attributes.html#attribute-types</a>
 * 注意：创建用于 GeoMesa 的 SimpleFeatureType 时，确保使用 GeoMesa 提供的类而不是使用标准的 GeoTools 的 DataUtilities 类
 * SimpleFeature: 是一种结构数据类型，相当于关系型数据库表中的一行
 * 支持两种写入方式：追加写和修改写
 * 每个 SimpleFeature 都与一个 SimpleFeatureType 相关联，并且具有唯一标识符（要素ID）和与 SimpleFeatureType 中的属性对应的值列表
 *
 * @author superz
 * @create 2021/7/16 14:37
 */
public class SimpleFeatureType_and_SimpleFeature {
    private DataStore dataStore;

    public SimpleFeatureType_and_SimpleFeature(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public static void main(String[] args) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("hbase.zookeepers", "namenode:2181,datanode1:2181,datanode1:2181");
        parameters.put("hbase.coprocessor.url", "hdfs://datanode1:8020/hbase/lib/geomesa-hbase-distributed-runtime-hbase2_2.11-3.0.0.jar|org.locationtech.geomesa.hbase.server.coprocessor.GeoMesaCoprocessor|1073741823||org.locationtech.geomesa.hbase.server.coprocessor.GeoMesaCoprocessor|1073741823|");
        parameters.put(HBaseDataStoreParams.HBaseCatalogParam().key, "cloud4control");

        DataStoreInstance dsi = new DataStoreInstance(parameters);
        SimpleFeatureType_and_SimpleFeature ss = new SimpleFeatureType_and_SimpleFeature(dsi.getDataStore());
        // 列出所有schema
        String[] schemas = dsi.getDataStore().getTypeNames();
        StringBuilder result=new StringBuilder();
        for (String schema : schemas) {
            SimpleFeatureType sft = dsi.getDataStore().getSchema(schema);
            result.append(GeoMesa2MD.template(sft));
//            System.out.println(schema);
//            System.out.println("    属性列表[" + sft.getAttributeCount() + "]：");
//            for (AttributeDescriptor attribute : sft.getAttributeDescriptors()) {
//                String attributeName = attribute.getLocalName();
//                String attributeType = Classes.getShortName(attribute.getType().getBinding());
//                System.out.println("        " + attributeName + ":" + attributeType);
//            }
//            System.out.println("    UserData:");
//            for (Map.Entry<Object, Object> entry : sft.getUserData().entrySet()) {
//                System.out.println("        " + entry.getKey() + ":" + entry.getValue());
//            }
//            if ("district.info".equals(schema) || "geomesa.fence".equals(schema) || "vehicle.alarm.section".equals(schema) || "vehicle.risk".equals(schema)) {
//                continue;
//            }
//            System.out.println("-------------------------" + schema + "----------------------------");
//            ss.readingData(schema);
//            System.out.println("-------------------------" + schema + "----------------------------");
        }
        System.out.println(result);
        dsi.close();
    }

    //region=========================SimpleFeatureType====================================

    /**
     * 列出所有的表
     *
     * @return
     */
    public String[] listSchemas() {
        try {
            return dataStore.getTypeNames();
        } catch (IOException e) {
            throw new RuntimeException("list schemas exception .", e);
        }
    }

    /**
     * 获取指定的表
     *
     * @param schema
     * @return
     */
    public SimpleFeatureType getSchema(String schema) {
        try {
            return dataStore.getSchema(schema);
        } catch (IOException e) {
            throw new RuntimeException("get schema '{" + schema + "}' exception .", e);
        }
    }

    public SimpleFeatureType createSimpleFeatureType(String schemaName, String attributes) {
        SimpleFeatureType sft = SimpleFeatureTypes.createType(schemaName, attributes);
        return sft;
    }

    public void removeSchema(String schema) {
        try {
            dataStore.removeSchema(schema);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //endregion======================SimpleFeatureType====================================

    //region=========================SimpleFeature========================================
    public void appendingWrites(String schema) {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriterAppend(schema, Transaction.AUTO_COMMIT)) {
            // repeat as needed, once per feature
            // note: hasNext() will always return false, but can be ignored
            SimpleFeature next = writer.next();
            next.getUserData().put(Hints.PROVIDED_FID, UUID.randomUUID().toString());// 唯一标识
            next.setAttribute("field1", "value1");
            next.setAttribute("field2", 100);
            // attributes will be converted to the appropriate type if needed
            next.setAttribute("date", "2020-01-01T00:00:00.000Z");
            next.setAttribute("location", "POINT (-82.379 34.1782)");
            writer.write();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改写，需要先通过一个filter来获取需要修改的数据
     *
     * @param schema
     */
    public void modifyingWrites(String schema) {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                     dataStore.getFeatureWriter(schema, ECQL.toFilter("IN ('id-01')"), Transaction.AUTO_COMMIT)) {
            while (writer.hasNext()) {
                SimpleFeature next = writer.next();
                next.setAttribute("amount", 21.0);
                writer.write(); // or, to delete it: writer.remove();
            }
        } catch (IOException | CQLException e) {
            e.printStackTrace();
        }
    }

    public void readingData(String schema) {
        try {
            Query query = new Query(schema, ECQL.toFilter("timestamp AFTER 1990-01-01T00:00:00.000Z"));
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                         dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
                int i = 0;
                while (reader.hasNext() && i < 10) {
                    SimpleFeature next = reader.next();
                    System.out.println(DataUtilities.encodeFeature(next));
                    i++;
                }
            }
        } catch (CQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion======================SimpleFeature========================================


}
