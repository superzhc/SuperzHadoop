package com.github.superzhc.financial.data.news;

import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

import java.util.*;

/**
 * @author superz
 * @create 2022/8/17 14:24
 **/
public class YiCai {
    public static Table brief() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.news.YiCai.brief();
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table author(String id) {
        return TableUtils.buildByMap(com.github.superzhc.data.news.YiCai.author(id));
    }

    public static Table feeds(String id) {
        return TableUtils.buildByMap(com.github.superzhc.data.news.YiCai.feeds(id));
    }

    public static Table toutiao() {
        return TableUtils.buildByMap(com.github.superzhc.data.news.YiCai.toutiao());
    }

    public static Table latest() {
        return TableUtils.buildByMap(com.github.superzhc.data.news.YiCai.latest());
    }

    public static Table channel(String cid) {
        return TableUtils.buildByMap(com.github.superzhc.data.news.YiCai.channel(cid));
    }

    public static Table news() {
        return TableUtils.buildByMap(com.github.superzhc.data.news.YiCai.news());
    }

    public static Table video() {
        return TableUtils.buildByMap(com.github.superzhc.data.news.YiCai.video());
    }

    public static void main(String[] args) {
        Table table = Table.create();

//         table = author("100005663");
//        table = feeds("669");
//        table = toutiao();
        table = latest();

        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
