package com.github.superzhc.hadoop.iceberg.catalog;

import org.apache.iceberg.catalog.Catalog;

/**
 * @author superz
 * @create 2023/3/23 15:25
 **/
public interface IcebergCatalog {
    Catalog catalog();

    Catalog catalog(String name);
}
