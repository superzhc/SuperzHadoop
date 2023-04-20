package com.github.superzhc.data.warehouse.ddl;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.jackson.JsonUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/4/19 14:43
 **/
public class FlinkSQLDDLDialectTest {
    JsonNode json;

    FlinkSQLDDLDialect flinkSql;

    @Before
    public void setUp() throws Exception {
        json = JsonUtils.loads(this.getClass().getResourceAsStream("/finance/ddl_table_fund_basic.json"));
        flinkSql = new FlinkSQLDDLDialect();
        flinkSql.set("catalog_name","hive_catalog");
        // flinkSql.set("database_name", "default");
        flinkSql.metadata("timestamp", "BIGINT");
        flinkSql.connector("kafka");
    }

    @Test
    public void testConvert() {
        String sql = WarehouseDDL.convert(json, flinkSql);
        System.out.println(sql);
    }
}
