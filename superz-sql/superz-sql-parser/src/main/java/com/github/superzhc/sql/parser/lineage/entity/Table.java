package com.github.superzhc.sql.parser.lineage.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author superz
 * @create 2022/6/17 16:25
 **/
public class Table {
    private static AtomicInteger counter = new AtomicInteger();

    private String name;
    private String alias;
    private List<String> fields;
    private String expr;

    public Table(String expr) {
        this(null, null, null, expr);
    }

    public Table(String name, String expr) {
        this(name, null, null, expr);
    }

    public Table(String name, String alias, String expr) {
        this(name, alias, null, expr);
    }

    public Table(String name, String alias, List<String> fields, String expr) {
        this.name = name;
        this.alias = alias;
        this.fields = fields;
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

    public Table addField(String field) {
        getFields().add(field);
        return this;
    }

    public Table addFields(List<String> fields) {
        getFields().addAll(fields);
        return this;
    }

    public List<String> clearFields() {
        List<String> originFields = getFields();
        setFields(new ArrayList<>());
        return originFields;
    }

    public List<String> getFields() {
        if (null == fields) {
            fields = new ArrayList<>();
        }
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", fields=" + fields +
                ", expr=" + expr +
                '}';
    }
}
