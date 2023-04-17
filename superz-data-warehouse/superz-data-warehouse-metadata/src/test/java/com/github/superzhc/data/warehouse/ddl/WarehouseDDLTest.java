package com.github.superzhc.data.warehouse.ddl;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.jackson.JsonUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/4/17 11:52
 **/
public class WarehouseDDLTest {

    JsonNode json;
    MySQLDDLDialect mysql;
    ClickHouseDDLDialect clickhouse;

    @Before
    public void setUp() throws Exception {
        json = JsonUtils.loads(this.getClass().getResourceAsStream("/finance/ddl_table_index.json"));

        mysql = new MySQLDDLDialect();
        clickhouse = new ClickHouseDDLDialect();
        clickhouse.engine("MergeTree");
    }

    @Test
    public void convert() {
        String sql = WarehouseDDL.convert(json, mysql);
        System.out.println(sql);
        sql = WarehouseDDL.convert(json, clickhouse);
        System.out.println(sql);
    }
}