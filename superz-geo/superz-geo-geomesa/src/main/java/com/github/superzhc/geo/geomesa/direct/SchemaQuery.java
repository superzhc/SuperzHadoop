package com.github.superzhc.geo.geomesa.direct;

import com.github.superzhc.geo.geomesa.source.GeomesaDataStore;
import com.github.superzhc.geo.geomesa.source.config.Cloud4ControlSourceConfig;
import org.geotools.data.DataStore;
import org.locationtech.geomesa.utils.geotools.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;

/**
 * @author superz
 * @create 2021/8/27 11:28
 */
public class SchemaQuery {
    public static void main(String[] args) {
        try(GeomesaDataStore dataStore=new GeomesaDataStore(new Cloud4ControlSourceConfig())){

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * create a new SimpleFeatureType
     * @param dataStore
     * @param schema
     * @param spec : The SimpleFeatureType specification to create
     *             1. A string of attributes, for example name:String,dtg:Date,*geom:Point:srid=4326
     * @throws IOException
     */
    static void create(DataStore dataStore,String schema,String spec) throws IOException {
        SimpleFeatureType sft= SimpleFeatureTypes.createType(schema,spec);
        dataStore.createSchema(sft);
    }



    static void delete(DataStore dataStore,String schema) throws IOException {
        dataStore.removeSchema(schema);
    }
}
