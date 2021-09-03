package com.github.superzhc.gis.geomesa.direct;

import com.github.superzhc.gis.geomesa.source.GeomesaDataStore;
import com.github.superzhc.gis.geomesa.source.config.Cloud4ControlSourceConfig;
import org.geotools.data.DataStore;

import java.io.IOException;

/**
 * <a>https://www.geomesa.org/documentation/3.0.0/user/cli/ingest.html</a>
 * @author superz
 * @create 2021/8/27 14:04
 */
public class IngestQuery {
    public static void main(String[] args) {
        try(GeomesaDataStore dataStore=new GeomesaDataStore(new Cloud4ControlSourceConfig())) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void delete(DataStore dataStore,String schema,String ecql){
        // dataStore.
    }
}
