package com.github.superzhc.sql.parser.lineage.entity;

/**
 * @author superz
 * @create 2022/6/20 9:28
 **/
public class Column {
    private String table;
    private String tableAlias;
    private String name;
    private String alias;

    public Column(String table, String name) {
        this(table, null, name, null);
    }

    public Column(String table, String name, String alias) {
        this(table, null, name, alias);
    }

    public Column(String table, String tableAlias, String name, String alias) {
        this.table = table;
        this.tableAlias = tableAlias;
        this.name = name;
        this.alias = alias;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "Column{" +
                "table='" + table + '\'' +
                ", tableAlias='" + tableAlias + '\'' +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
