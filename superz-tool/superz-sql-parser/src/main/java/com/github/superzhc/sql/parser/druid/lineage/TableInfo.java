package com.github.superzhc.sql.parser.druid.lineage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 逻辑表信息
 *
 * @author superz
 * @create 2022/6/16 16:38
 **/
public class TableInfo {
    private static AtomicInteger counter = new AtomicInteger();

    private Integer id;
    // private String schema;
    private String table = null;
    private String alias;
    private String expr;
    /* 逻辑表的所有列，缺省时为表的所有字段 */
    private List<TableField> fields;

    /* table 和 dependenceTables 是一组互斥的量，它们都是源的表现 */
    // Note：依赖表会重复，去重操作，但可阅读性会变差
    private List<String> dependenceTables = null;

//    /* 子表信息 */
//    private TableInfo childTableInfo;

    public TableInfo() {
        this.id = counter.incrementAndGet()/*UUID.randomUUID().toString()*/;
    }

    public TableInfo(String expr) {
        this();
        this.expr = expr;
    }

    public Integer getId() {
        return id;
    }

//    public void setId(Integer id) {
//        this.id = id;
//    }

//    public String getSchema() {
//        return schema;
//    }
//
//    public TableInfo setSchema(String schema) {
//        this.schema = schema;
//        return this;
//    }

    public String getTable() {
        return table;
    }

    public TableInfo setTable(String table) {
        this.table = table;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public TableInfo setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getExpr() {
        return expr;
    }

    public TableInfo setExpr(String expr) {
        this.expr = expr;
        return this;
    }

    public TableInfo addDependenceTable(String dependenceTable) {
        if (null != dependenceTable) {
            getDependenceTables().add(dependenceTable);
        }
        return this;
    }

    public TableInfo addDependenceTables(List<String> dependenceTables) {
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

    public TableInfo addField(TableField field) {
        getFields().add(field);
        return this;
    }

    public TableInfo addFields(List<TableField> anotherFields) {
        getFields().addAll(anotherFields);
        return this;
    }

    public List<TableField> clearFields() {
        List<TableField> removeFields = getFields();
        setFields(new ArrayList<>());
        return removeFields;
    }

    public List<TableField> getFields() {
        if (null == fields) {
            fields = new ArrayList<>();

//            // 2022年6月17日 14点02分 新增缺省列 ?????
//            TableField field = new TableField(getId(),"*");
//            fields.add(field);
        }
        return fields;
    }

    public void setFields(List<TableField> fields) {
        this.fields = fields;
    }

//    public TableInfo getChildTableInfo() {
//        return childTableInfo;
//    }
//
//    public TableInfo setChildTableInfo(TableInfo childTableInfo) {
//        this.childTableInfo = childTableInfo;
//        return this;
//    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "id=" + id +
                ", table='" + table + '\'' +
                ", alias='" + alias + '\'' +
                ", dependenceTables=" + dependenceTables +
                ", fields=" + fields +
                ", expr=" + (expr.contains("\n") ? "\n" : "") + expr/*.replace('\n', ' ').replace('\t', ' ')*/ +
                '}';
    }
}
