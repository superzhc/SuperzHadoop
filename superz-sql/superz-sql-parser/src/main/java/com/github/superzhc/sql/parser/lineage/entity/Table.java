package com.github.superzhc.sql.parser.lineage.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author superz
 * @create 2022/6/17 16:25
 **/
public class Table {
    private static AtomicInteger identityCounter = new AtomicInteger();
    private static AtomicInteger counter = new AtomicInteger();

    private Integer id;
    private String name;
    private String alias;
    // private List<String> fields;
    private List<Column> columns;
    private String expr;

    /* table 和 dependenceTables 是一组互斥的量，它们都是源的表现 */
    // Note：依赖表会重复，去重操作，但可阅读性会变差
    private List<String> dependenceTables = null;

    public Table(String expr) {
        this(null, null, null, expr);
    }

    public Table(String name, String expr) {
        this(name, null, null, expr);
    }

    public Table(String name, String alias, String expr) {
        this(name, alias, null, expr);
    }

    public Table(String name, String alias, List<Column> columns/*List<String> fields*/, String expr) {
        this.id = identityCounter.incrementAndGet();
        this.name = name;
        this.alias = alias;
        // this.fields = fields;
        this.columns = columns;
        this.expr = expr;
    }

    public static String tempTable() {
        return String.format("%s_%d", "temp", counter.incrementAndGet());
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

//    public Table addField(String field) {
//        getFields().add(field);
//        return this;
//    }
//
//    public Table addFields(List<String> fields) {
//        getFields().addAll(fields);
//        return this;
//    }
//
//    public List<String> clearFields() {
//        List<String> originFields = getFields();
//        setFields(new ArrayList<>());
//        return originFields;
//    }
//
//    public List<String> getFields() {
//        if (null == fields) {
//            fields = new ArrayList<>();
//        }
//        return fields;
//    }
//
//    public void setFields(List<String> fields) {
//        this.fields = fields;
//    }

    public Table addColumn(Column column) {
        getColumns().add(column);
        return this;
    }

    public Table addColumns(List<Column> anotherColumns) {
        getColumns().addAll(anotherColumns);
        return this;
    }

    public List<Column> clearColumns() {
        List<Column> anotherColumns = getColumns();
        setColumns(null);
        return anotherColumns;
    }

    public List<Column> getColumns() {
        if (null == columns) {
            columns = new ArrayList<>();
        }
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    public Table addDependenceTable(String dependenceTable) {
        if (null != dependenceTable) {
            getDependenceTables().add(dependenceTable);
        }
        return this;
    }

    public Table addDependenceTables(List<String> dependenceTables) {
        if (null != dependenceTables) {
            getDependenceTables().addAll(dependenceTables);
        }
        return this;
    }

    public List<String> getDependenceTables() {
        if (null == dependenceTables) {
            dependenceTables = new ArrayList<>();
        }
        return dependenceTables;
    }

    public void setDependenceTables(List<String> dependenceTables) {
        this.dependenceTables = dependenceTables;
    }

    @Override
    public String toString() {
        return "Table{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", dependenceTables=" + dependenceTables +
                // ", fields=" + fields +
                ", columns=" + columns +
                ", expr=" + (expr.contains("\n") ? String.format("\n%s\n", expr) : expr ) +
                '}';
    }
}
