package com.github.superzhc.geo.geomesa.direct;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2021/7/16 14:06
 */
public class DataStoreInstance {
    private static final Logger log = LoggerFactory.getLogger(DataStoreInstance.class);

    private Map<String, String> params = null;
    private DataStore dataStore = null;

    public DataStoreInstance(String hbase_zookeepers, String hbase_catalog) {
        this(hbase_zookeepers, hbase_catalog, null);
    }

    public DataStoreInstance(String hbase_zookeepers, String hbase_catalog, String hbase_coprocessor_url) {
        this.params = new HashMap<>();
        params.put("hbase.zookeepers", hbase_zookeepers);
        params.put("hbase.catalog", hbase_catalog);
        if (null != hbase_coprocessor_url && hbase_coprocessor_url.length() != 0) {
            params.put("hbase.coprocessor.url", hbase_coprocessor_url);
        }
    }

    public DataStoreInstance(Map<String, String> params) {
        this.params = params;
    }

    /**
     * 可重复使用
     * @return
     */
    public DataStore getDataStore() {
        if (null == dataStore) {
            try {
                dataStore = DataStoreFinder.getDataStore(params);
                if (dataStore == null) {
                    throw new RuntimeException("Could not create data store with provided parameters");
                }
            } catch (IOException e) {
                log.error("Could not create data store with exception，exception:", e);
                throw new RuntimeException("Could not create data store with provided parameters");
            }
        }
        return dataStore;
    }

    /**
     * 当不在需要的时候，关掉dataStore
     */
    public void close() {
        if (dataStore != null) {
            dataStore.dispose();
            dataStore = null;// 再将dataStore置为null
        }
    }
}
