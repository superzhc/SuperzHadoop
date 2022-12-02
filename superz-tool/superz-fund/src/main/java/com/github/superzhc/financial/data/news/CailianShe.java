package com.github.superzhc.financial.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * @author superz
 * @create 2022/8/13 15:17
 **/
public class CailianShe {
    public static Table telegraph() {
        List<String> columnNames = Arrays.asList("id", "ctime", "title", /*"type",*/ "brief", "content", "shareurl", "subjects");
        List<Map<String,Object>> data= com.github.superzhc.data.news.CailianShe.telegraph();
        Table table = TableUtils.buildByMap(columnNames,data);
        return table;
    }

    public static void main(String[] args) {
        Table table = telegraph();

        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
