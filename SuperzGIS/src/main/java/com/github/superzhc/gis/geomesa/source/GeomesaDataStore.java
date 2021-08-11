package com.github.superzhc.gis.geomesa.source;

import com.github.superzhc.gis.geomesa.source.config.GeomesaSourceConfig;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author superz
 * @create 2021/8/11 21:47
 */
public class GeomesaDataStore implements Closeable {
    private DataStore dataStore = null;

    public GeomesaDataStore(GeomesaSourceConfig config) {
        try {
            dataStore = DataStoreFinder.getDataStore(config.sourceParams());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataStore getDataStore() {
        return this.dataStore;
    }

    @Override
    public void close() throws IOException {
        if (null != dataStore) {
            dataStore.dispose();
        }
    }
}
