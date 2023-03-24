package com.github.superzhc.hadoop.iceberg.catalog;

import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.CatalogProperties;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2023/3/24 9:16
 **/
public class IcebergHadoopCatalog implements IcebergCatalog {
    private static final Logger LOG = LoggerFactory.getLogger(IcebergHadoopCatalog.class);

    private String warehouse;

    private Configuration conf = null;

    public IcebergHadoopCatalog(String warehouse) {
        this.warehouse = warehouse;
    }

    public IcebergHadoopCatalog(String warehouse, Configuration conf) {
        this.warehouse = warehouse;
        this.conf = conf;
    }

    @Override
    public Catalog catalog() {
        return catalog("hadoop");
    }

    @Override
    public Catalog catalog(String name) {
        Map<String, String> properties = new HashMap<>();
        properties.put(CatalogProperties.CATALOG_IMPL, HadoopCatalog.class.getName());
        properties.put(CatalogProperties.WAREHOUSE_LOCATION, warehouse);

        HadoopCatalog hadoopCatalog = new HadoopCatalog();
        if (null != conf) {
            hadoopCatalog.setConf(conf);
        }
        hadoopCatalog.initialize(name, properties);

        return hadoopCatalog;
    }
}
