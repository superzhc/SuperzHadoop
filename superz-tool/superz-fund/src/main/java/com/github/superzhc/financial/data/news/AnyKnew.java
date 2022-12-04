package com.github.superzhc.financial.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

/**
 * 官网地址：https://www.anyknew.com/
 *
 * @author superz
 * @create 2022/4/20 10:40
 **/
public class AnyKnew {

    public static Table weibo() {
        return execute("weibo");
    }

    public static Table zhihu() {
        return execute("zhihu");
    }

    public static Table smzdm() {
        return execute("smzdm");
    }

    public static Table finance() {
        Table table = xueqiu()
                .append(investing())
                .append(wallstreetcn())
                .append(eastmoney())
                .append(caixin())
                .setName("news_anyknew_finance");

        Table t2 = table.sortDescendingOn("add_date");

        return t2;
    }

    public static Table xueqiu() {
        return execute("xueqiu");
    }

    public static Table investing() {
        return execute("investing");
    }

    public static Table wallstreetcn() {
        return execute("wallstreetcn");
    }

    public static Table eastmoney() {
        return execute("eastmoney");
    }

    public static Table caixin() {
        return execute("caixin");
    }

    /**
     * @param type
     * @return Structure of
     * Index  |  Column Name  |  Column Type  |
     * -----------------------------------------
     * 0  |          iid  |      INTEGER  |
     * 1  |        title  |       STRING  |
     * 2  |     add_date  |      INTEGER  |
     */
    public static Table execute(String type) {
        Table table = TableUtils.buildByMap(com.github.superzhc.data.news.AnyKnew.execute(type));
        // table.setName(String.format("%s[%s]", siteName, siteUrl));

        table.replaceColumn("add_date", table.intColumn("add_date").multiply(1000).asLongColumn().asDateTimes(ZoneOffset.ofHours(+8)).setName("add_date"));

        TableUtils.addConstantColumn(table, "type", type);
        table.setName(String.format("news_anyknew_%s", type));

        return table;
    }

    public static void main(String[] args) {
        Table table = finance();

        System.out.println(table.printAll());
        System.out.println(table.shape());
        System.out.println(table.structure().printAll());
    }
}
