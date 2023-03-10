package com.github.superzhc.hadoop.iceberg.utils;

import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author superz
 * @create 2023/3/10 17:11
 **/
public class TableUtils {
    private TableUtils() {
    }

    public static List<String> tables(Catalog catalog, String... db) {
        Namespace namespace = Namespace.of(db);
        return tables(catalog, namespace);
    }

    public static List<String> tables(Catalog catalog, Namespace namespace) {
        List<TableIdentifier> tableIdentifiers = catalog.listTables(namespace);
        return tableIdentifiers.stream().map(tableIdentifier -> tableIdentifier.name()).collect(Collectors.toList());
    }

    public static Table create(Catalog catalog, TableIdentifier tableIdentifier, Schema schema, PartitionSpec spec) {
        return catalog.createTable(tableIdentifier,schema,spec);
    }

    public static void drop(Catalog catalog,Table table){

//        catalog.dropTable()
    }
}
