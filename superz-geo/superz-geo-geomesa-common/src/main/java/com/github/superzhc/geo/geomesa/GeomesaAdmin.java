package com.github.superzhc.geo.geomesa;

import com.github.superzhc.geo.geomesa.source.GeomesaDataStore;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.locationtech.geomesa.utils.geotools.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;

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
     *
     * @param schema
     * @return
     */
    public boolean exist(String schema) {
        try {
            SimpleFeatureType sft = sft(schema);
            return null != sft;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取 Schema 的列表
     *
     * @return
     */
    public String[] list() {
        try {
            return dataStore.getDataStore().getTypeNames();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Describe a specific SimpleFeatureType
     *
     * @param schema
     */
    public SimpleFeatureType sft(String schema) {
        try {
            return dataStore.getDataStore().getSchema(schema);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String formatSft(String schema) {
        SimpleFeatureType sft = sft(schema);
        if (null == sft) {
            return null;
        } else {
            return DataUtilities.encodeType(sft);
        }
    }

    /**
     * create a new SimpleFeatureType
     *
     * @param schema
     * @param spec   : The SimpleFeatureType specification to create
     *               1. A string of attributes, for example name:String,dtg:Date,*geom:Point:srid=4326
     *               2. 示例：
     *               StringBuilder attributes = new StringBuilder();
     *               attributes.append("GLOBALEVENTID:String,");
     *               attributes.append("Actor1Name:String,");
     *               attributes.append("Actor1CountryCode:String,");
     *               attributes.append("Actor2Name:String,");
     *               attributes.append("Actor2CountryCode:String,");
     *               attributes.append("EventCode:String:index=true,"); // marks this attribute for indexing
     *               attributes.append("NumMentions:Integer,");
     *               attributes.append("NumSources:Integer,");
     *               attributes.append("NumArticles:Integer,");
     *               attributes.append("ActionGeo_Type:Integer,");
     *               attributes.append("ActionGeo_FullName:String,");
     *               attributes.append("ActionGeo_CountryCode:String,");
     *               attributes.append("dtg:Date,");
     *               attributes.append("*geom:Point:srid=4326");
     * @throws IOException
     */
    public void create(String schema, String spec) throws IOException {
        SimpleFeatureType sft = SimpleFeatureTypes.createType(schema, spec);
        dataStore.getDataStore().createSchema(sft);
    }

    /**
     * 删除 Schema
     *
     * @param schema
     * @throws IOException
     */
    public void delete(String schema) throws IOException {
        dataStore.getDataStore().removeSchema(schema);
    }
}
