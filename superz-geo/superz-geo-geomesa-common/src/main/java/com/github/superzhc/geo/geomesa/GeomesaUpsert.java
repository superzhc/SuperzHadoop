package com.github.superzhc.geo.geomesa;

import com.github.superzhc.geo.geomesa.source.GeomesaDataStore;
import com.google.common.base.CaseFormat;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.util.factory.Hints;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

/**
 * @author superz
 * @create 2021/11/15 19:46
 */
public class GeomesaUpsert {
    private static final Logger log = LoggerFactory.getLogger(GeomesaUpsert.class);

    private GeomesaDataStore dataStore;

    public GeomesaUpsert(GeomesaDataStore dataStore) {
        this.dataStore = dataStore;
    }

    public <T> void insert(String schema, T attributes) {
        try {
            SimpleFeatureType sft = dataStore.getDataStore().getSchema(schema);
            if (null == sft) {
                throw new RuntimeException("schema[" + schema + "] not exist");
            }

            SimpleFeatureBuilder builder = new SimpleFeatureBuilder(sft);

            Class clazz = attributes.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                builder.set(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()), field.get(attributes));
            }

            SimpleFeature simpleFeature = builder.buildFeature(UUID.randomUUID().toString());
            insert(schema, simpleFeature);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(String schema, Map<String, Object> attributes) {
//        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getDataStore().getFeatureWriterAppend(schema, Transaction.AUTO_COMMIT)) {
//            // repeat as needed, once per feature
//            // note: hasNext() will always return false, but can be ignored
//            SimpleFeature next = writer.next();
//            // 唯一标识
//            next.getUserData().put(Hints.PROVIDED_FID, UUID.randomUUID().toString());
//            next.setAttribute("field1", "value1");
//            next.setAttribute("field2", 100);
//            // attributes will be converted to the appropriate type if needed
//            next.setAttribute("date", "2020-01-01T00:00:00.000Z");
//            next.setAttribute("location", "POINT (-82.379 34.1782)");
//            for (Map.Entry<String, Object> attribute : attributes.entrySet()) {
//                next.setAttribute(attribute.getKey(), attribute.getValue());
//            }
//            writer.write();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            SimpleFeatureType sft = dataStore.getDataStore().getSchema(schema);
            if (null == sft) {
                throw new RuntimeException("schema[" + schema + "] not exist");
            }

            SimpleFeatureBuilder builder = new SimpleFeatureBuilder(sft);
            for (Map.Entry<String, Object> attribute : attributes.entrySet()) {
                /* 使用驼峰命名法 */
                builder.set(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, attribute.getKey()), attribute.getValue());
            }
            SimpleFeature simpleFeature = builder.buildFeature(UUID.randomUUID().toString());
            insert(schema, simpleFeature);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insert(String schema, SimpleFeature simpleFeature) {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getDataStore().getFeatureWriterAppend(schema, Transaction.AUTO_COMMIT)) {
            // repeat as needed, once per feature
            // note: hasNext() will always return false, but can be ignored
            SimpleFeature next = writer.next();
            // 唯一标识
            next.getUserData().put(Hints.PROVIDED_FID, /*UUID.randomUUID().toString()*/simpleFeature.getID());
            next.setAttributes(simpleFeature.getAttributes());
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
    public void update(String schema, String ecql, Map<String, Object> attributes) {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                     dataStore.getDataStore().getFeatureWriter(schema, ECQL.toFilter(/*"IN ('id-01')"*/ecql), Transaction.AUTO_COMMIT)) {
            while (writer.hasNext()) {
                SimpleFeature next = writer.next();
                for (Map.Entry<String, Object> attribute : attributes.entrySet()) {
                    next.setAttribute(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, attribute.getKey()), attribute.getValue());
                }
                writer.write();
            }
        } catch (IOException | CQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据查询条件，查询出数据进行删除
     *
     * @param schema
     * @param ecql
     */
    public void delete(String schema, String ecql) {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                     dataStore.getDataStore().getFeatureWriter(schema, ECQL.toFilter(/*"IN ('id-01')"*/ecql), Transaction.AUTO_COMMIT)) {
            while (writer.hasNext()) {
                SimpleFeature next = writer.next();
                writer.remove();
            }
        } catch (IOException | CQLException e) {
            e.printStackTrace();
        }
    }
}
