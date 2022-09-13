package com.github.superzhc.financial.data.stock;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.ColumnUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/6/10 13:45
 **/
public class EastMoneyStock {
    public static Table newShares() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.stock.EastMoneyStock.newShares();

        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) {

        Table table = newShares();

        System.out.println(table.print());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
    }
}
