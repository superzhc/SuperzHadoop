package com.github.superzhc.financial.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.financial.utils.XueQiuUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.*;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * @author superz
 * @create 2022/8/17 15:29
 **/
public class XueQiu {
    public static Table hot() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.mixture.XueQiu.hot();
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table = Table.create();

        table = hot();

        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
