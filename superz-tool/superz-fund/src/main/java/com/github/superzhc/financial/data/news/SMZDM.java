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
 * @create 2022/8/15 19:58
 */
public class SMZDM {
    public static Table userArticles(String uid) {
        Table table = TableUtils.buildByMap(com.github.superzhc.data.shopping.SMZDM.userArticles(uid));
        return table;
    }

    public static Table haowen() {
        return haowen("all");
    }

    /**
     * @param period all,1,7,30,365
     *
     * @return
     */
    public static Table haowen(String period) {
        Table table = TableUtils.buildByMap(com.github.superzhc.data.shopping.SMZDM.haowen(period));
        return table;
    }

    public static Table search(String keyword) {
        List<Map<String, Object>> dataRows = com.github.superzhc.data.shopping.SMZDM.search(keyword);
        Table table = TableUtils.buildByMap(dataRows);
        return table;

    }

    public static void main(String[] args) {
        Table table = search("自行车");

        System.out.println(table.print());
        System.out.println(table.shape());
    }
}
