package com.github.superzhc.gis.geomesa.direct;

import com.github.superzhc.gis.geomesa.source.GeomesaDataStore;
import com.github.superzhc.gis.geomesa.source.config.Cloud4ControlSourceConfig;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.locationtech.geomesa.index.geotools.GeoMesaDataStoreFactory;
import org.locationtech.geomesa.index.geotools.GeoMesaDataStoreFactory$;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;

/**
 * 环境和状态命令行，详细见:<a>https://www.geomesa.org/documentation/3.0.0/user/cli/status.html</a>
 *
 * @author superz
 * @create 2021/8/27 10:42
 */
public class EnvQuery {
    public static void main(String[] args) {
        try (GeomesaDataStore dataStore = new GeomesaDataStore(new Cloud4ControlSourceConfig())) {
            /*sfts(dataStore.getDataStore(), "bsm.speed");*/
            /*describeSfts(dataStore.getDataStore());*/
            listSfts(dataStore.getDataStore());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Describe a specific SimpleFeatureType
     *
     * @param dataStore
     * @param schema
     * @throws IOException
     */
    static void sfts(DataStore dataStore, String schema) throws IOException {
        SimpleFeatureType sft = dataStore.getSchema(schema);
        System.out.println(DataUtilities.encodeType(sft));
    }

    /**
     * Describe all SimpleFeatureTypes
     *
     * @param dataStore
     * @throws IOException
     */
    static void describeSfts(DataStore dataStore) throws IOException {
        String[] types = dataStore.getTypeNames();
        for (String schema : types) {
            SimpleFeatureType sft = dataStore.getSchema(schema);
            System.out.printf("%s={%s}\n", schema, DataUtilities.encodeType(sft));
        }
    }

    /**
     * List all available type names
     *
     * @param dataStore
     * @throws IOException
     */
    static void listSfts(DataStore dataStore) throws IOException {
        String[] types = dataStore.getTypeNames();
        System.out.println(String.join(",", types));
    }
}
