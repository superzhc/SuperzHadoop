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

    public String show(String... schemas) {
        if (null == schemas || schemas.length == 0) {
            schemas = list();
        }

        /* 进行二次判定 geomesa 中是否有表 */
        if (null == schemas || schemas.length == 0) {
            return "no schema";
        }

        StringBuilder sb = new StringBuilder();
        StringBuilder detail = new StringBuilder();
        int total = 0;
        for (String schema : schemas) {
            SimpleFeatureType sft = sft(schema);
            if (null == sft) {
                continue;
            }

            sb.append(",").append("\"").append(schema).append("\"");
            /* 格式形如："column":"attribute1:string,attribute2:int..." */
            detail.append(",").append("\"").append(schema).append("\"").append(":").append("\"").append(DataUtilities.encodeType(sft)).append("\"");

            total++;
        }

        return String.format("{\"schemas\":[%s],\"detail\":{%s},\"total\":%d}", sb.substring(1), detail.substring(1), total);
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

    @Deprecated
    public String formatSft(String schema) {
        return show(schema);
    }

    /**
     * create a new SimpleFeatureType
     * FIXME:创建会报错误 org.apache.hadoop.hbase.ipc.RemoteWithExtrasException(org.apache.hadoop.hbase.DoNotRetryIOException): org.apache.hadoop.hbase.DoNotRetryIOException: Call From datanode2/10.80.2.191 to datanode1:8020 failed on connection exception: java.net.ConnectException: Connection refused; For more details see:  http://wiki.apache.org/hadoop/ConnectionRefused Set hbase.table.sanity.checks to false at conf or table descriptor if you want to bypass sanity checks
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
        create(sft);
    }

    public void create(SimpleFeatureType sft) throws IOException {
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
