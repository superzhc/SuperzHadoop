package com.github.superzhc.reader.executor.impl;

import com.github.superzhc.reader.datasource.impl.GeomesaDatasource;
import com.github.superzhc.reader.param.impl.GeomesaParam;
import org.geotools.data.*;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * @author superz
 * @create 2021/8/17 17:04
 */
public class GeomesaExecutor {
    private GeomesaDatasource dataSource;
    private GeomesaParam param;

    public GeomesaExecutor(GeomesaDatasource dataSource, GeomesaParam param) {
        this.dataSource = dataSource;
        this.param = param;
    }

    public void execute() {
        DataStore datastore = null;
        try {
            // 创建数据源
            System.out.println("Loading datastore");
            datastore = DataStoreFinder.getDataStore(dataSource.getParams());
            if (datastore == null) {
                throw new RuntimeException("Could not create data store with provided parameters");
            }
            System.out.println("Datastore loaded");

            // 获取Schema
            SimpleFeatureType sft = datastore.getSchema(param.getSchema());
            if (null == sft) {
                System.out.println("Schema '" + param.getSchema() + "' does not exist. ");
                return;
            }

            Query query;
            if (null == param.getFields() || param.getFields().length == 0) {
                query = new Query(param.getSchema(), ECQL.toFilter(param.getEcql()));
            } else {
                query = new Query(param.getSchema(), ECQL.toFilter(param.getEcql()), param.getFields());
            }

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = datastore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
                while (reader.hasNext()) {
                    SimpleFeature next = reader.next();
                    System.out.println(DataUtilities.encodeFeature(next));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // when finished, be sure to clean up the store
        if (datastore != null) {
            datastore.dispose();
        }
    }
}
