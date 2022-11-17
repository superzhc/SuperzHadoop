package com.github.superzhc.financial.data.news;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.tablesaw.utils.TableUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tech.tablesaw.api.Table;

import java.util.*;

import static com.github.superzhc.common.HttpConstant.UA_CHROME;

/**
 * @author superz
 * @create 2022/8/15 23:39
 */
public class GuangDiu {
    /**
     * 日常
     *
     * @return
     */
    public static Table daily() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.shopping.GuangDiu.daily();
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    /**
     * 家电
     *
     * @return
     */
    public static Table electrical() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.shopping.GuangDiu.electrical();
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table food() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.shopping.GuangDiu.food();
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table personCare() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.shopping.GuangDiu.personCare();
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    /**
     * 配饰
     *
     * @return
     */
    public static Table accessory() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.shopping.GuangDiu.accessory();
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    /**
     * 家具家装
     *
     * @return
     */
    public static Table furniture() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.shopping.GuangDiu.furniture();
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    /**
     * 医疗保健
     *
     * @return
     */
    public static Table medical() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.shopping.GuangDiu.medical();
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    /**
     * 男装
     *
     * @return
     */
    public static Table menswear() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.shopping.GuangDiu.menswear();
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    /**
     * 男鞋
     *
     * @return
     */
    public static Table mensshoes() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.shopping.GuangDiu.mensshoes();
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table sport() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.shopping.GuangDiu.sport();
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static Table all() {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.shopping.GuangDiu.all();
        Table table = TableUtils.buildByMap(dataRows);
        return table;
    }

    public static void main(String[] args) throws Exception {
        Table table = food();
        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
