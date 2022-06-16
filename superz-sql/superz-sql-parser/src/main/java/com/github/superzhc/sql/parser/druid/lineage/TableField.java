package com.github.superzhc.sql.parser.druid.lineage;

/**
 * @author superz
 * @create 2022/6/16 16:43
 **/
public class TableField {
    private String schema;
    private Integer table;
    private String field;

    public TableField(String field) {
        this(null, null, field);
    }

    public TableField(Integer table, String field) {
        this(null, table, field);
    }

    public TableField(String schema, Integer table, String field) {
        this.schema = schema;
        this.table = table;
        this.field = field;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Integer getTable() {
        return table;
    }

    public void setTable(Integer table) {
        this.table = table;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "TableField{" +
                "schema='" + schema + '\'' +
                ", table='" + table + '\'' +
                ", field='" + field + '\'' +
                '}';
    }
}
