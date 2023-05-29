package com.github.superzhc.sql.parser.druid.lineage;

/**
 * @author superz
 * @create 2022/6/16 16:43
 **/
public class TableField {
    private String schema;
    /* tableInfo 唯一标识 */
    private Integer tableId;
    private String table;
    private String field;
    private String alias;

//    public TableField(){
//        this(null,null,"*");
//    }
//
//    public TableField(String field) {
//        this(null, null, field);
//    }

    public TableField(Integer tableId, String field) {
        this(null, tableId, field);
    }

    public TableField(String schema, Integer tableId, String field) {
        this.schema = schema;
        this.tableId = tableId;
        this.field = field;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "TableField{" +
                "schema='" + schema + '\'' +
                ", tableId=" + tableId +
                ", table='" + table + '\'' +
                ", field='" + field + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
