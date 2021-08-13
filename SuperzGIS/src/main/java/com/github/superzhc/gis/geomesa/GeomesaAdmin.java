package com.github.superzhc.gis.geomesa;

import com.github.superzhc.gis.geomesa.source.GeomesaDataStore;
import org.opengis.feature.simple.SimpleFeatureType;

import java.util.Map;

/**
 * @author superz
 * @create 2021/8/13 21:04
 */
public class GeomesaAdmin {
    private GeomesaDataStore dataStore;

    public GeomesaAdmin(GeomesaDataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * 判断 schema 是否存在
     * @param schema
     * @return
     */
    public boolean exist(String schema) {
        try {
            SimpleFeatureType sft = dataStore.getDataStore().getSchema(schema);
            return null != sft;
        } catch (Exception e) {
            return false;
        }
    }

}
