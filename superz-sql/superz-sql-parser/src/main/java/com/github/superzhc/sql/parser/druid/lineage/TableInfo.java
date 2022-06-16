package com.github.superzhc.sql.parser.druid.lineage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author superz
 * @create 2022/6/16 16:38
 **/
public class TableInfo {
    private static AtomicInteger counter=new AtomicInteger();
    private Integer id;
    private String name;
    private List<String> tables;
    /* 缺省时为表的所有字段 */
    private List<TableField> fields;

    public TableInfo() {
        this.id = counter.incrementAndGet();/*UUID.randomUUID().toString()*/;
    }

    public TableInfo(String name) {
        this();
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TableInfo addTable(String table) {
        getTables().add(table);
        return this;
    }

    public TableInfo addTables(List<String> anotherTables) {
        getTables().addAll(anotherTables);
        return this;
    }

    public List<String> getTables() {
        if (null == tables) {
            tables = new ArrayList<>();
        }
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public TableInfo addField(TableField field) {
        getFields().add(field);
        return this;
    }

    public TableInfo addFields(List<TableField> anotherFields) {
        getFields().addAll(anotherFields);
        return this;
    }

    public List<TableField> getFields() {
        if (null == fields) {
            fields = new ArrayList<>();
        }
        return fields;
    }

    public void setFields(List<TableField> fields) {
        this.fields = fields;
    }

//    /**
//     * join
//     * @param tableInfos
//     * @return
//     */
//    public TableInfo merge(TableInfo... tableInfos){
//
//    }


    @Override
    public String toString() {
        return "TableInfo{" +
                "id='" + id + '\'' +
                ", tables=" + tables +
                ", fields=" + fields +
                ", name='" + name.replace('\n', ' ').replace('\t', ' ') + '\'' +
                '}';
    }
}
