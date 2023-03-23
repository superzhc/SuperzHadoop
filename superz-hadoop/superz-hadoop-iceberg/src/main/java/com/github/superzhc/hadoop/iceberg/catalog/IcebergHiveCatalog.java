package com.github.superzhc.hadoop.iceberg.catalog;

import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.CatalogProperties;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.hadoop.HadoopConfigurable;
import org.apache.iceberg.hive.HiveCatalog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/23 15:24
 **/
public class IcebergHiveCatalog implements IcebergCatalog {
    private static final Logger LOG = LoggerFactory.getLogger(IcebergHiveCatalog.class);

    private String uri;
    private String warehouse;
    private Configuration conf = null;

    public IcebergHiveCatalog(String uri, String warehouse) {
        this.uri = uri;
        this.warehouse = warehouse;
    }

    public IcebergHiveCatalog(String uri, String warehouse, Configuration conf) {
        this.uri = uri;
        this.warehouse = warehouse;
        this.conf = conf;
    }

    @Override
    public Catalog catalog() {
        return catalog("hive");
    }

    @Override
    public Catalog catalog(String name) {
        Map<String, String> properties = new HashMap<>();
        properties.put(CatalogProperties.CATALOG_IMPL, HiveCatalog.class.getName());
        properties.put(CatalogProperties.URI, uri);
        properties.put(CatalogProperties.WAREHOUSE_LOCATION, warehouse);

        HiveCatalog hiveCatalog = new HiveCatalog();
        if (null != conf) {
            hiveCatalog.setConf(conf);
        }
        hiveCatalog.initialize(name, properties);
        return hiveCatalog;
    }
}
