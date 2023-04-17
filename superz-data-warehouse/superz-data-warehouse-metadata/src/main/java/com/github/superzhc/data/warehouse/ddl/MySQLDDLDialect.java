package com.github.superzhc.data.warehouse.ddl;

/**
 * @author superz
 * @create 2023/4/17 3:34
 */
public class MySQLDDLDialect extends WarehouseDDLDialect {

    public MySQLDDLDialect() {
        super("MySQL");
    }

    @Override
    public String ddlTemplate() {
        String sql="CREATE TABLE ${table_name}(${fields})";
        return null;
    }

    @Override
    protected String convertTableOptions(String option, Object value) {
        return null;
    }

    @Override
    protected String java2db(Class clazz) {
        return null;
    }

    @Override
    protected String convertFieldOption(String option, Object value) {
        return null;
    }
}
