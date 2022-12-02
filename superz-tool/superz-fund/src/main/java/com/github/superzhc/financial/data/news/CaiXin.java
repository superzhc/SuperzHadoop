package com.github.superzhc.financial.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.*;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * @author superz
 * @create 2022/8/16 16:29
 **/
public class CaiXin {

    public static Table articles() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.news.CaiXin.articles();
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table yixian() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.news.CaiXin.yixian();
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) {
        Table table = yixian();
        System.out.println(table.print());
        System.out.println(table.shape());

    }
}
