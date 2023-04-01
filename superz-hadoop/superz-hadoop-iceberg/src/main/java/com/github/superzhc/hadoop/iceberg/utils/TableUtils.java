package com.github.superzhc.hadoop.iceberg.utils;

import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.UpdateProperties;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.hive.HiveCatalog;
import org.apache.iceberg.io.FileIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author superz
 * @create 2023/3/10 17:11
 **/
public class TableUtils {
    private static final Logger LOG = LoggerFactory.getLogger(TableUtils.class);

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
        return catalog.createTable(tableIdentifier, schema, spec);
    }

    public static Table create(Catalog catalog, TableIdentifier tableIdentifier, Schema schema, PartitionSpec spec, Map<String, String> tableProperties) {
        return catalog.createTable(tableIdentifier, schema, spec, tableProperties);
    }

    public static boolean drop(Catalog catalog, Table table) {
        String[] names = table.name().split("\\.");
        names = Arrays.copyOfRange(names, 1, names.length);
        return drop(catalog, names);
    }

    public static boolean drop(Catalog catalog, String... names) {
        return drop(catalog, TableIdentifier.of(names));
    }

    public static boolean drop(Catalog catalog, TableIdentifier tableIdentifier) {
        boolean isSuccess = catalog.dropTable(tableIdentifier);

        // 2023年4月1日 对于HiveCatalog来说，删除表只是删除了HMS中的元数据，并未删除实际存储介质中的文件
        if (catalog instanceof HiveCatalog) {

        }
        return isSuccess;
    }

    public static void rename(Catalog catalog, Table table, String newName) {
        String[] names = table.name().split("\\.");
        names = Arrays.copyOfRange(names, 1, names.length);
        TableIdentifier from = TableIdentifier.of(names);
        names[names.length - 1] = newName;
        TableIdentifier to = TableIdentifier.of(names);
        rename(catalog, from, to);
    }

    public static void rename(Catalog catalog, Namespace namespace, String oldName, String newName) {
        TableIdentifier from = TableIdentifier.of(namespace, oldName);
        TableIdentifier to = TableIdentifier.of(namespace, newName);
        rename(catalog, from, to);
    }

    /**
     * 不一定支持
     *
     * @param catalog
     * @param from
     * @param to
     */
    public static void rename(Catalog catalog, TableIdentifier from, TableIdentifier to) {
        catalog.renameTable(from, to);
    }

    public static Table loadTable(Catalog catalog, String... names) {
        return loadTable(catalog, TableIdentifier.of(names));
    }

    public static Table loadTable(Catalog catalog, TableIdentifier tableIdentifier) {
        return catalog.loadTable(tableIdentifier);
    }

    public static void setProperties(Table table, String key, String value) {
        UpdateProperties update = table.updateProperties();

        update.set(key, value);

        update.commit();
    }

    public static void setProperties(Table table, Map<String, String> properties) {
        UpdateProperties update = table.updateProperties();

        for (Map.Entry<String, String> property : properties.entrySet()) {
            update.set(property.getKey(), property.getValue());
        }

        update.commit();
    }

    public static void removeProperties(Table table, String... properties) {
        if (null == properties || properties.length == 0) {
            return;
        }

        UpdateProperties update = table.updateProperties();

        for (String property : properties) {
            update.remove(property);
        }

        update.commit();
    }
}
